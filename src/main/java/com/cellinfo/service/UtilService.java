package com.cellinfo.service;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cellinfo.security.TokenField;
import com.cellinfo.security.UserInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class UtilService {

    @Value("${jwt.secret}")
    private String secret;
    
	@Value("${jwt.tokenName}")
	private String tokenName;
	    
	@Value("${jwt.tokenHead}")
	private String tokenHead;
	
	public UserInfo getCurrentUser(HttpServletRequest request) {
		
    	String token = request.getHeader(this.tokenName);
    	if (!StringUtils.isEmpty(token) && token.startsWith(this.tokenHead)) {
    		String auth_token = token.substring(this.tokenHead.length());
	        try {
	            final Claims claims = Jwts.parser()
	                    .setSigningKey(secret)
	                    .parseClaimsJws(auth_token)
	                    .getBody();
	            String username = claims.getSubject();
	            String groupid = claims.get(TokenField.CLAIM_KEY_GROUP_GUID).toString();

	
	            return  new UserInfo(username,groupid);
	        } catch (Exception e) {
	            throw e;
	        }
    	}
        return null;
    }
	
	
	private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",  
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",  
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",  
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",  
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
            "W", "X", "Y", "Z" };  
	
	public String generateShortUuid() {  
	    StringBuffer shortBuffer = new StringBuffer();  
	    String uuid = UUID.randomUUID().toString().replace("-", "");  
	    for (int i = 0; i < 8; i++) {  
	        String str = uuid.substring(i * 4, i * 4 + 4);  
	        int x = Integer.parseInt(str, 16);  
	        shortBuffer.append(chars[x % 0x3E]);  
	    }  
	    return shortBuffer.toString().toUpperCase();
	}
}
