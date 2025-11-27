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
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final int accessExpirationMinutes;
    private final int refreshExpirationMinutes;

    public JwtTokenProvider(JwtProperties properties) {
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMinutes = properties.getExpirationMinutes();
        this.refreshExpirationMinutes = properties.getRefreshExpirationMinutes();
    }

    public TokenDetails generateAccessToken(UserPrincipal principal) {
        return buildToken(principal, TokenType.ACCESS, accessExpirationMinutes);
    }

    public TokenDetails generateRefreshToken(UserPrincipal principal) {
        return buildToken(principal, TokenType.REFRESH, refreshExpirationMinutes);
    }

    public Jws<Claims> validateAccessToken(String token) {
        return validateToken(token, TokenType.ACCESS);
    }

    public Jws<Claims> validateRefreshToken(String token) {
        return validateToken(token, TokenType.REFRESH);
    }

    private TokenDetails buildToken(UserPrincipal principal, TokenType type, int expirationMinutes) {
        Instant now = Instant.now();
        Instant expiry = now.plus(expirationMinutes, ChronoUnit.MINUTES);
        String token = Jwts.builder()
            .setSubject(principal.getId())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .addClaims(Map.of(
                "email", principal.getUsername(),
                "role", principal.getRole().name(),
                "tokenType", type.name()
            ))
            .signWith(secretKey)
            .compact();
        return new TokenDetails(token, expiry);
    }

    private Jws<Claims> validateToken(String token, TokenType expectedType) {
        Jws<Claims> claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token);
        String type = claims.getBody().get("tokenType", String.class);
        if (type == null || !expectedType.name().equalsIgnoreCase(type)) {
            throw new JwtException("Invalid token type");
        }
        return claims;
    }

    public enum TokenType {
        ACCESS,
        REFRESH
    }

    public record TokenDetails(String token, Instant expiresAt) { }
}
