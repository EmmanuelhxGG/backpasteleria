package com.example.PasteleriaBackend.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.security.UserPrincipal;
import com.example.PasteleriaBackend.security.jwt.JwtTokenProvider;
import com.example.PasteleriaBackend.service.AuthService;
import com.example.PasteleriaBackend.web.dto.LoginRequest;
import com.example.PasteleriaBackend.web.dto.LoginResponse;
import com.example.PasteleriaBackend.security.jwt.JwtTokenProvider.TokenDetails;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public AuthServiceImpl(
        AuthenticationManager authenticationManager,
        JwtTokenProvider tokenProvider,
        UserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        TokenDetails accessToken = tokenProvider.generateAccessToken(principal);
        TokenDetails refreshToken = tokenProvider.generateRefreshToken(principal);
        return buildResponse(principal, accessToken, refreshToken);
    }

    @Override
    public LoginResponse refresh(String refreshTokenValue) {
        var claims = tokenProvider.validateRefreshToken(refreshTokenValue);
        String email = claims.getBody().get("email", String.class);
        String userId = claims.getBody().getSubject();
        if (email == null || userId == null) {
            throw new BadCredentialsException("Token de actualización inválido");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!(userDetails instanceof UserPrincipal principal) || !principal.getId().equals(userId) || !userDetails.isEnabled()) {
            throw new BadCredentialsException("No se pudo validar el token de actualización");
        }
        TokenDetails accessToken = tokenProvider.generateAccessToken(principal);
        TokenDetails refreshToken = tokenProvider.generateRefreshToken(principal);
        return buildResponse(principal, accessToken, refreshToken);
    }

    private LoginResponse buildResponse(UserPrincipal principal, TokenDetails accessToken, TokenDetails refreshToken) {
        return new LoginResponse(
            accessToken.token(),
            accessToken.expiresAt().toEpochMilli(),
            refreshToken.token(),
            refreshToken.expiresAt().toEpochMilli(),
            principal.getRole().name(),
            principal.getId()
        );
    }
}
