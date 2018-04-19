package com.cellinfo.filters;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.cellinfo.entity.TlGammaLog;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.security.TokenAuthenticationService;
import com.cellinfo.security.UserAuthentication;
import com.cellinfo.service.SysLogService;
import com.cellinfo.service.SysUserService;
import com.cellinfo.util.FuncDesc;
import com.cellinfo.util.ModuleDesc;
import com.cellinfo.util.StatusDesc;
import com.cellinfo.utils.ResultUtil;
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
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        
        // logger.info(username+" "+ password);
    	
    	final TlGammaUser user = toUser(request);
        
    	// final TlGammaUser realUser = this.sysUserService.findOne(user.getUsername());

        
        final UsernamePasswordAuthenticationToken loginToken = user.toAuthenticationToken();
        return getAuthenticationManager().authenticate(loginToken);
    }

    private TlGammaUser toUser(HttpServletRequest request) throws IOException {
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

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), ResultUtil.success(tokenMap));
        
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