package com.airnbn.user.service.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtHelper {
    private final String secret = "secret";
    private final long expiration = 100 * 60 * 60 * 60;

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid token. error={}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid token. error={}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Invalid token. error={}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Invalid token. error={}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Invalid token. error={}", e.getMessage());
        }
        return false;
    }


    public String getUsernameFromToken(String token) {
        String result = null;
        try {
            result = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            log.error("Invalid token. error={}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Invalid token. error={}", e.getMessage());
        }
        return result;
    }
}