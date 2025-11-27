package com.example.PasteleriaBackend.web.dto;

import java.time.LocalDate;

import com.example.PasteleriaBackend.domain.model.UserStatus;

public record CustomerSummaryResponse(
    String id,
    String run,
    String firstName,
    String lastName,
    String email,
    LocalDate birthDate,
    String region,
    String commune,
    String customerType,
    UserStatus status,
    boolean newsletter,
    boolean felices50,
    Integer birthdayRedeemedYear,
    Double defaultShippingCost
) {
}
