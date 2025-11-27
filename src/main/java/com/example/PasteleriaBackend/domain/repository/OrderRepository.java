package com.example.PasteleriaBackend.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PasteleriaBackend.domain.model.Order;
import com.example.PasteleriaBackend.domain.model.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderCode(String code);

    @EntityGraph(attributePaths = {"items", "items.product", "coupon", "shippingAddress"})
    List<Order> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);

    List<Order> findByStatus(OrderStatus status);

    @EntityGraph(attributePaths = {"items", "items.product", "coupon", "shippingAddress"})
    Optional<Order> findDetailedById(UUID id);

    @EntityGraph(attributePaths = {"items", "items.product", "coupon", "shippingAddress"})
    List<Order> findAllByOrderByCreatedAtDesc();
}
