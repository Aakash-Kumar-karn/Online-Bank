package com.bank.card.CardService.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    //  This should be same as User MS secret
    @Value("${jwt.secret}")
    private String SECERT;




    // Parse the token and get claims
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECERT.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    //  Check token expiration
    public boolean isTokenExpired(String token) {

        return extractClaims(token).getExpiration().before(new Date());
    }

    // Get username/email from token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    //  Get role from token
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}
