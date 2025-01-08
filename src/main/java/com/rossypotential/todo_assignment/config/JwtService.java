package com.rossypotential.todo_assignment.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtService {
    @Value("${app.jwt-secret}")
    private String secretKey;

    @Value("${app.jwt-expiration}")
    private Long jwtExpirationDate;

    @Value("${app.jwt-refresh-expiration}")
    private Long refreshTokenExpirationDate;

    public String generateToken(UserDetails user){
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("authorities", populateAuthorities(user.getAuthorities()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationDate))
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities){
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority: authorities){
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

    public String getUserName(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();


    }
    //this method help us validate that the token belongs to the right person
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            System.out.println("Token is valid: " + token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Token validation failed: Token expired");
            return false;
        } catch (MalformedJwtException e) {
            System.err.println("Token validation failed: Malformed token");
            return false;
        } catch (SecurityException e) {
            System.err.println("Token validation failed: Invalid signature");
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Token validation failed: Token is null or empty");
            return false;
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public String extractUsernameFromToken(String token) {

        String jwtToken = token.replace("Bearer ", "");

        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }
    public String generateRefreshToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationDate))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}