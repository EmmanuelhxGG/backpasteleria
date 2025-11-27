package com.example.PasteleriaBackend.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PasteleriaBackend.domain.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(java.util.UUID orderId);
}
