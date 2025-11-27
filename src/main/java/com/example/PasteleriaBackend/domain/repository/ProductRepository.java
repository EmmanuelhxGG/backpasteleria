package com.example.PasteleriaBackend.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PasteleriaBackend.domain.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByActiveTrue();

    List<Product> findByCategoryIgnoreCase(String category);
}
