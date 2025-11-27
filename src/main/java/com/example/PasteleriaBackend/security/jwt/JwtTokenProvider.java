package com.example.PasteleriaBackend.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.example.PasteleriaBackend.config.JwtProperties;
import com.example.PasteleriaBackend.security.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final int expirationMinutes;
    private final int refreshExpirationMinutes;

    public JwtTokenProvider(JwtProperties properties) {
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = properties.getExpirationMinutes();
        this.refreshExpirationMinutes = properties.getRefreshExpirationMinutes();
    }

    public String generateToken(UserPrincipal principal) {
        Instant now = Instant.now();
        Instant expiry = now.plus(expirationMinutes, ChronoUnit.MINUTES);
        return Jwts.builder()
            .setSubject(principal.getId())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .addClaims(Map.of(
                "email", principal.getUsername(),
                "role", principal.getRole().name()
            ))
            .signWith(secretKey)
            .compact();
    }

    public String generateRefreshToken(UserPrincipal principal) {
        Instant now = Instant.now();
        Instant expiry = now.plus(refreshExpirationMinutes, ChronoUnit.MINUTES);
        return Jwts.builder()
            .setSubject(principal.getId())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .addClaims(Map.of(
                "type", "refresh",
                "role", principal.getRole().name(),
                "email", principal.getUsername()
            ))
            .signWith(secretKey)
            .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token);
    }

    public int getExpirationMinutes() {
        return expirationMinutes;
    }

    public int getRefreshExpirationMinutes() {
        return refreshExpirationMinutes;
    }
}
