package com.example.PasteleriaBackend.web.dto;

import java.util.List;

import com.example.PasteleriaBackend.domain.model.OrderStatus;

public record OrderResponse(
    String id,
    String orderCode,
    OrderStatus status,
    String customerName,
    String customerEmail,
    double subtotal,
    double discountTotal,
    double shippingCost,
    double total,
    List<String> benefitsApplied,
    String couponCode,
    String couponLabel,
    long createdAt,
    List<OrderItemResponse> items
) {
}
