package com.cellinfo.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenAuthenticationService {

	@Value("${jwt.tokenName}")
	private String tokenName;

    private final JwtTokenHandler jwtTokenHandler;

    @Autowired
    public TokenAuthenticationService(JwtTokenHandler jwtTokenHandler) {
        this.jwtTokenHandler = jwtTokenHandler;
    }

    public String addJwtTokenToHeader(HttpServletResponse response,
                             UserAuthentication authentication) {
        final UserDetails user = authentication.getDetails();
        String tmpToken = jwtTokenHandler.createTokenForUser(user);
        response.addHeader(this.tokenName, tmpToken);
        return tmpToken;
    }

    public Authentication generateAuthenticationFromRequest(HttpServletRequest request) {
        final String token = request.getHeader(this.tokenName);
        if (token == null || token.isEmpty()) return null;
        return jwtTokenHandler
                .parseUserFromToken(token)
                .map(UserAuthentication::new)
                .orElse(null);
    }
}
