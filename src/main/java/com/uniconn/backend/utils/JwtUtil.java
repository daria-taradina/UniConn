// Lillian Foster
// UniConn - COMP 380
// JwtUtil.java - utility class for generating and validating JWT tokens
// Called by UserService after login and by JwtAuthFilter on every request

package com.uniconn.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Secret key used to sign all tokens — must be at least 256 bits for HS256
    // In a real production app this would be stored in environment variables
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // How long the token lasts — 24 hours in milliseconds
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // Generates a JWT token for the given username
    // Called after successful login in UserService
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    // Extracts the username from a token
    // Used by JwtAuthFilter to identify who is making the request
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Checks if the token is still valid (not expired, not tampered with)
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            // Token is invalid or expired
            return false;
        }
    }

    // Helper method - parses the token and returns its claims (contents)
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}