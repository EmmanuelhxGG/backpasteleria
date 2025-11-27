package com.example.PasteleriaBackend.web.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
    @Size(max = 100) String firstName,
    @Size(max = 120) String lastName,
    @Email @Size(max = 150) String email,
    @Size(max = 100) String region,
    @Size(max = 100) String commune,
    @Size(max = 255) String address,
    @Size(max = 30) String phone,
    @Size(max = 50) String promoCode,
    String customerType,
    LocalDate birthDate,
    Integer birthdayRedeemedYear,
    Double defaultShippingCost,
    Boolean newsletter,
    Boolean saveAddress,
    Boolean felices50,
    UUID primaryAddressId,
    @Valid List<CustomerAddressRequest> addresses
) {
}
