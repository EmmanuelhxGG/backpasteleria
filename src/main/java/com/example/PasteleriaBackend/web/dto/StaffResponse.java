package com.example.PasteleriaBackend.web.dto;

import com.example.PasteleriaBackend.domain.model.UserStatus;

public record StaffResponse(
    String id,
    String run,
    String firstName,
    String lastName,
    String email,
    String staffRole,
    String region,
    String commune,
    String address,
    String phone,
    UserStatus status
) {
}
