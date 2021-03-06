package com.cellinfo.filters;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.cellinfo.entity.TlGammaGroup;
import com.cellinfo.entity.TlGammaLog;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.exception.GroupDisableException;
import com.cellinfo.security.TokenAuthenticationService;
import com.cellinfo.security.UserAuthentication;
import com.cellinfo.service.SysLogService;
import com.cellinfo.service.SysUserService;
import com.cellinfo.utils.ExceptionDesc;
import com.cellinfo.utils.FuncDesc;
import com.cellinfo.utils.ModuleDesc;
import com.cellinfo.utils.ResultUtil;
import com.cellinfo.utils.StatusDesc;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

	private final static Logger logger = LoggerFactory.getLogger(StatelessLoginFilter.class);
    private final TokenAuthenticationService tokenAuthenticationService;

    private SysUserService sysUserService;
    
    private SysLogService sysLogService;

    public StatelessLoginFilter(String urlMapping,TokenAuthenticationService tokenAuthenticationService,
    		SysUserService userService,SysLogService sysLogService, AuthenticationManager authenticationManager) {
        super(urlMapping);
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.sysUserService = userService;
        this.sysLogService = sysLogService;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) 
        throws AuthenticationException, IOException, ServletException {
    	
    	final TlGammaUser user = toUser(request);
        
    	final Optional<TlGammaUser> realUser = this.sysUserService.findOne(user.getUsername());
    	if(realUser.isPresent())
    	{
    		Optional<TlGammaGroup>  optinalgroup = this.sysUserService.findGroupById(realUser.get().getGroupGuid());
    		if(optinalgroup.isPresent() && optinalgroup.get().getGroupStatus()!= 1)
    		{
    			throw new GroupDisableException (ExceptionDesc.GROUP_IS_DISABLED);
    		}
    	}

        final UsernamePasswordAuthenticationToken loginToken = user.toAuthenticationToken();
        return getAuthenticationManager().authenticate(loginToken);
    }

    private TlGammaUser toUser(HttpServletRequest request) throws IOException {
    	logger.warn(request.getInputStream().toString());
        return new ObjectMapper().readValue(request.getInputStream(), TlGammaUser.class);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        final UserDetails authenticatedUser = sysUserService.loadUserByUsername(authResult.getName());
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        
        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("userName", authenticatedUser.getUsername());
        tokenMap.put("x-auth-token", tokenAuthenticationService.addJwtTokenToHeader(response, userAuthentication));
        //添加路由
        String roleName = "组织用户";
		Integer roleKey = 300;
		String  roleId = "ROLE_USER";
        for( GrantedAuthority auth : authenticatedUser.getAuthorities())
        {
        	roleId = auth.getAuthority();
        }
        
		if(roleId.indexOf("ROLE_ADMIN") >= 0)
		{
			roleName= "系统管理员";
			roleKey = 100;
		}
		else if(roleId.indexOf("ROLE_GROUP_ADMIN") >= 0)
		{
			roleName = "组织管理员";
			roleKey = 200;
		}
		else
		{
			roleName = "组织用户";
			roleKey = 300;
		}
		tokenMap.put("roleName", roleName);
		tokenMap.put("roleKey", String.valueOf(roleKey));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), ResultUtil.success(tokenMap));
        
        Optional<TlGammaUser> userOptional =  this.sysUserService.findOne(authenticatedUser.getUsername());
        if(userOptional.isPresent())
        {
        	TlGammaUser rUser = userOptional.get();
        	rUser.setLoginTime(new Timestamp(System.currentTimeMillis()));
        	this.sysUserService.save(rUser);
        }
        
        try {
        	
        	TlGammaLog syslog = new TlGammaLog();
			syslog.setModuleName(ModuleDesc.SYSTEM_LOGIN);
			syslog.setOprateName(FuncDesc.LONGIN);
			syslog.setOprateDesc(StatusDesc.LOGIN_SUCCESS);
			syslog.setUserName(authenticatedUser.getUsername());
			syslog.setIpStr(request.getRemoteAddr());
			syslog.setUrlStr(request.getRequestURL().toString());
			syslog.setLogTime(new Timestamp(System.currentTimeMillis()));
			
			this.sysLogService.save(syslog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		SecurityContextHolder.clearContext();

		String failDesc = "Authentication request failed: " + failed.toString();
		 
		Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("failDesc", failDesc);
        
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
//				"Authentication Failed: " +failed.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), ResultUtil.error(401, failed.toString()));
        
        try {
        	
        	TlGammaLog syslog = new TlGammaLog();
			syslog.setModuleName(ModuleDesc.SYSTEM_LOGIN);
			syslog.setOprateName(FuncDesc.LONGIN);
			syslog.setOprateDesc(StatusDesc.LOGIN_FAILURE+failed.toString());
			syslog.setUserName("");
			syslog.setIpStr(request.getRemoteAddr());
			syslog.setUrlStr(request.getRequestURL().toString());
			syslog.setLogTime(new Timestamp(System.currentTimeMillis()));
			
			this.sysLogService.save(syslog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}