package com.cellinfo.filters;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cellinfo.entity.TlGammaLog;
import com.cellinfo.security.JwtTokenHandler;
import com.cellinfo.service.SysLogService;
import com.cellinfo.service.SysUserService;
import com.cellinfo.util.ExceptionDesc;
import com.cellinfo.utils.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


	private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
	private static String CONST_CAS_ASSERTION ="_const_cas_assertion_";

    @Autowired
    private JwtTokenHandler jwtTokenHandler; 
    
    @Autowired
    private SysUserService  sysUserService;
    
    @Autowired
    private SysLogService sysLogService;

    @Value("${jwt.tokenName}")
    private String tokenName;
    
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String auth_token = request.getHeader(this.tokenName);
        if (!StringUtils.isEmpty(auth_token) && auth_token.startsWith(this.tokenHead)) {
            auth_token = auth_token.substring(this.tokenHead.length());
        } else {
            // 不按规范,不允许通过验证
            auth_token = null;
        }
        
        final HttpSession session = request.getSession(false);
        try {
			String username = jwtTokenHandler.getUsernameFromToken(auth_token);
			logger.info(String.format("Checking authentication for user %s.", username));

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			    // It is not compelling necessary to load the use details from the database. You could also store the information
			    // in the token and read it from it. It's up to you ;)
				UserDetails userDetails = this.sysUserService.loadUserByUsername(username);
			    //UserDetails userDetails = jwtTokenHandler.getUserFromToken(auth_token);

			    // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
			    // the database compellingly. Again it's up to you ;)
			    if (jwtTokenHandler.validateToken(auth_token, userDetails)) {
			        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			        logger.info(String.format("Authenticated user %s, setting security context", username));
			        SecurityContextHolder.getContext().setAuthentication(authentication);
			    }
			    chain.doFilter(request, response);
			}
			else
			{
				new ObjectMapper().writeValue(response.getWriter(), ResultUtil.error(403,ExceptionDesc.NO_AUTHENTICATION)); 
				try {
					TlGammaLog syslog = new TlGammaLog();
					syslog.setModuleName("*");
					syslog.setOprateName("*");
					syslog.setOprateDesc(ExceptionDesc.NO_AUTHENTICATION);
					syslog.setUserName(username);
					syslog.setIpStr(request.getRemoteAddr());
					syslog.setUrlStr(request.getRequestURL().toString());
					syslog.setLogTime(new Timestamp(System.currentTimeMillis()));
					
					this.sysLogService.save(syslog);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    new ObjectMapper().writeValue(response.getWriter(), ResultUtil.error(402,e.toString()));  
		    try {
		    	TlGammaLog syslog = new TlGammaLog();
				syslog.setModuleName("*");
				syslog.setOprateName("*");
				syslog.setOprateDesc(ExceptionDesc.ERR1000+e.getMessage().substring(0, 500));
				syslog.setUserName("");
				syslog.setIpStr(request.getRemoteAddr());
				syslog.setUrlStr(request.getRequestURL().toString());
				syslog.setLogTime(new Timestamp(System.currentTimeMillis()));
				this.sysLogService.save(syslog);
			} catch (Exception exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
		}
    }

}