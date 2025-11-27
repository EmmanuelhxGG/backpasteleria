package com.example.PasteleriaBackend.service.impl;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.PasteleriaBackend.config.JwtProperties;
import com.example.PasteleriaBackend.security.UserPrincipal;
import com.example.PasteleriaBackend.security.jwt.JwtTokenProvider;
import com.example.PasteleriaBackend.service.AuthService;
import com.example.PasteleriaBackend.web.dto.LoginRequest;
import com.example.PasteleriaBackend.web.dto.LoginResponse;
import com.example.PasteleriaBackend.web.dto.TokenRefreshRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    public AuthServiceImpl(
        AuthenticationManager authenticationManager,
        JwtTokenProvider tokenProvider,
        JwtProperties jwtProperties,
        UserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return issueTokens(principal);
    }

    @Override
    public LoginResponse refresh(TokenRefreshRequest request) {
        if (!StringUtils.hasText(request.getRefreshToken())) {
            throw new IllegalArgumentException("El token de refresco es obligatorio");
        }
        try {
            Jws<Claims> parsed = tokenProvider.validateToken(request.getRefreshToken());
            Claims claims = parsed.getBody();
            if (!"refresh".equals(claims.get("type", String.class))) {
                throw new IllegalArgumentException("Token de refresco inválido");
            }
            String userId = claims.getSubject();
            String email = claims.get("email", String.class);
            if (!StringUtils.hasText(email)) {
                throw new IllegalArgumentException("Token de refresco inválido");
            }
            UserDetails details = userDetailsService.loadUserByUsername(email);
            if (!(details instanceof UserPrincipal principal)) {
                throw new IllegalArgumentException("Usuario no soportado");
            }
            if (!principal.getId().equals(userId)) {
                throw new IllegalArgumentException("Token de refresco inválido");
            }
            return issueTokens(principal);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new IllegalArgumentException("Token de refresco inválido o expirado", ex);
        }
    }

    private LoginResponse issueTokens(UserPrincipal principal) {
        String token = tokenProvider.generateToken(principal);
        String refreshToken = tokenProvider.generateRefreshToken(principal);
        long expiresAt = Instant.now().plusSeconds(jwtProperties.getExpirationMinutes() * 60L).toEpochMilli();
        long refreshExpiresAt = Instant.now().plusSeconds(jwtProperties.getRefreshExpirationMinutes() * 60L).toEpochMilli();
        return new LoginResponse(token, expiresAt, refreshToken, refreshExpiresAt, principal.getRole().name(), principal.getId());
    }
}
