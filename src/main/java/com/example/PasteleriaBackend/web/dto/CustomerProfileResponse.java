package com.example.PasteleriaBackend.web.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import com.example.PasteleriaBackend.domain.model.UserStatus;

public record CustomerProfileResponse(
    String id,
    String run,
    String firstName,
    String lastName,
    String email,
    UserStatus status,
    String customerType,
    LocalDate birthDate,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    String region,
    String commune,
    String address,
    String phone,
    String promoCode,
    boolean felices50,
    Integer birthdayRedeemedYear,
    Double defaultShippingCost,
    boolean newsletter,
    boolean saveAddress,
    String staffRole,
    List<CustomerAddressResponse> addresses
) {
}
