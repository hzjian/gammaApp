package com.cellinfo.security;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.enums.ResultEnum;
import com.cellinfo.exception.NetException;
import com.cellinfo.service.SysUserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public final class JwtTokenHandler {

	public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";
    
    @Autowired
    private SysUserService userService;

    @Value("${jwt.subject}")
    private String subject;
    
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access_token.expiration}")
    private Long access_token_expiration;

    @Value("${jwt.refresh_token.expiration}")
    private Long refresh_token_expiration;

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;


    Optional<UserDetails> parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Optional.ofNullable(userService.loadUserByUsername(username));
    }
 
    
    public String createTokenForUser(UserDetails user) {
        final ZonedDateTime afterOneWeek = ZonedDateTime.now().plusWeeks(1);
        Map<String, Object> claims = new HashMap<>();
        claims.put(TokenField.CLAIM_KEY_AUTHORITIES, user.getAuthorities());
        claims.put(TokenField.CLAIM_KEY_ACCOUNT_ENABLED, user.isEnabled());
        claims.put(TokenField.CLAIM_KEY_ACCOUNT_NON_LOCKED, user.isAccountNonLocked());
        claims.put(TokenField.CLAIM_KEY_ACCOUNT_NON_EXPIRED, user.isAccountNonExpired());
        claims.put(TokenField.CLAIM_KEY_SUBJECT, user.getUsername());
        if( user instanceof TlGammaUser)
        {
        	claims.put(TokenField.CLAIM_KEY_GROUP_GUID, ((TlGammaUser)user).getGroupGuid());
        }
        return Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS512, secret)
                .setClaims(claims)
                .setExpiration(generateExpirationDate(access_token_expiration))
                .compact();
    }
    

    public TlGammaUser getUserFromToken(String token) {
    	TlGammaUser user;
        try {
            final Claims claims = getClaimsFromToken(token);
            String username = claims.getSubject();
            List roles = (List) claims.get(TokenField.CLAIM_KEY_AUTHORITIES);
            Collection<? extends GrantedAuthority> authorities = parseArrayToAuthorities(roles);
            boolean account_enabled = (Boolean) claims.get(TokenField.CLAIM_KEY_ACCOUNT_ENABLED);
            boolean account_non_locked = (Boolean) claims.get(TokenField.CLAIM_KEY_ACCOUNT_NON_LOCKED);
            boolean account_non_expired = (Boolean) claims.get(TokenField.CLAIM_KEY_ACCOUNT_NON_EXPIRED);

            user = new TlGammaUser(username, "password", account_enabled, account_non_expired, account_non_locked, authorities);
        } catch (Exception e) {
            user = null;
            throw e;
        }
        return user;
    }


    public String getUsernameFromToken(String token) {
        String username = null;
        try {
        	if(token == null || token.trim().length()<1)
        		return username;
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (NetException e) {
            username = null;
            throw e;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = claims.getIssuedAt();
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (ExpiredJwtException e) {
        	claims = null;
        	///TODO
        	//throw  exception
        	//e.printStackTrace();
        	throw new NetException(ResultEnum.EXPIREDJWT);
        }
        catch (Exception e) {
            claims = null;
            e.printStackTrace();
            throw new NetException(ResultEnum.UNKONW_ERROR);
        }
        return claims;
    }

    private Date generateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateAccessToken(UserDetails userDetails) {
    	TlGammaUser user = (TlGammaUser) userDetails;
        Map<String, Object> claims = generateClaims(user);
        claims.put(TokenField.CLAIM_KEY_AUTHORITIES, authoritiesToArray(user.getAuthorities()));
        return generateAccessToken(user.getUsername(), claims);
    }

    private Map<String, Object> generateClaims(TlGammaUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TokenField.CLAIM_KEY_SUBJECT, user.getUserName());
        claims.put(TokenField.CLAIM_KEY_ACCOUNT_ENABLED, user.isEnabled());
        claims.put(TokenField.CLAIM_KEY_ACCOUNT_NON_LOCKED, user.isAccountNonLocked());
        claims.put(TokenField.CLAIM_KEY_ACCOUNT_NON_EXPIRED, user.isAccountNonExpired());
        claims.put(TokenField.CLAIM_KEY_GROUP_GUID, user.getGroupGuid());
        return claims;
    }

    private String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, access_token_expiration);
    }

    private List authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
        List<String> list = new ArrayList<>();
        for (GrantedAuthority ga : authorities) {
            list.add(ga.getAuthority());
        }
        return list;
    }

    private Collection<? extends GrantedAuthority> parseArrayToAuthorities(List roles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority;
        for (Object role : roles) {
            authority = new SimpleGrantedAuthority(role.toString());
            authorities.add(authority);
        }
        return authorities;
    }

    public String generateRefreshToken(UserDetails userDetails) {
    	TlGammaUser user = (TlGammaUser) userDetails;
        Map<String, Object> claims = generateClaims(user);
        // 只授于更新 token 的权限
        String roles[] = new String[]{ROLE_REFRESH_TOKEN};
        claims.put(TokenField.CLAIM_KEY_AUTHORITIES, roles);
        return generateRefreshToken(user.getUsername(), claims);
    }

    private String generateRefreshToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, refresh_token_expiration);
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            refreshedToken = generateAccessToken(claims.getSubject(), claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    private String generateToken(String subject, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(expiration))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
    	TlGammaUser user = (TlGammaUser) userDetails;

        final String username = getUsernameFromToken(token);
        // final Date created = getCreatedDateFromToken(token);
        // final Date expiration = getExpirationDateFromToken(token);
        return (username.equalsIgnoreCase(user.getUserName())
                && username.equals(user.getUsername())
                && !isTokenExpired(token)
                /* && !isCreatedBeforeLastPasswordReset(created, userDetails.getLastPasswordResetDate()) */
        );
    }

}