package com.example.PasteleriaBackend.service.impl;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.config.JwtProperties;
import com.example.PasteleriaBackend.security.UserPrincipal;
import com.example.PasteleriaBackend.security.jwt.JwtTokenProvider;
import com.example.PasteleriaBackend.service.AuthService;
import com.example.PasteleriaBackend.web.dto.LoginRequest;
import com.example.PasteleriaBackend.web.dto.LoginResponse;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final JwtProperties jwtProperties;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, JwtProperties jwtProperties) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = tokenProvider.generateToken(principal);
        long expiresAt = Instant.now().plusSeconds(jwtProperties.getExpirationMinutes() * 60L).toEpochMilli();
        return new LoginResponse(token, expiresAt, principal.getRole().name(), principal.getId());
    }
}
