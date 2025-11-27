package com.example.PasteleriaBackend.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PasteleriaBackend.domain.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    @EntityGraph(attributePaths = {"items", "items.product", "coupon"})
    Optional<ShoppingCart> findWithItemsByUserId(UUID userId);

    Optional<ShoppingCart> findByUserId(UUID userId);
}
