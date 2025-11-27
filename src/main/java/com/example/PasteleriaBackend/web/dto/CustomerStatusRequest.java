package com.example.PasteleriaBackend.web.dto;

import com.example.PasteleriaBackend.domain.model.UserStatus;

import jakarta.validation.constraints.NotNull;

public record CustomerStatusRequest(@NotNull UserStatus status) {
}
