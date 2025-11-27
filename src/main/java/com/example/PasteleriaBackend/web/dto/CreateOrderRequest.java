package com.example.PasteleriaBackend.web.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
    @NotEmpty @Valid List<CreateOrderItemRequest> items,
    @NotNull Double shippingCost,
    List<String> benefitsApplied,
    String couponCode,
    String couponLabel,
    String notes,
    UUID shippingAddressId
) {
}
