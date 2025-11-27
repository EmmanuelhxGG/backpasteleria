package com.example.PasteleriaBackend.web.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRegistrationRequest(
    @NotBlank @Size(max = 20) String run,
    @NotBlank @Size(max = 100) String firstName,
    @NotBlank @Size(max = 120) String lastName,
    @NotBlank @Email @Size(max = 150) String email,
    @NotBlank @Size(min = 6, max = 60) String password,
    @Size(max = 40) String customerType,
    LocalDate birthDate,
    @Size(max = 100) String region,
    @Size(max = 100) String commune,
    @Size(max = 255) String address,
    @Size(max = 30) String phone,
    @Size(max = 50) String promoCode,
    Double defaultShippingCost,
    Boolean newsletter,
    Boolean saveAddress,
    Integer birthdayRedeemedYear,
    @Valid List<CustomerAddressRequest> addresses
) {
}
