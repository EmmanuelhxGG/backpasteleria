package com.example.PasteleriaBackend.service;

import java.util.List;

import com.example.PasteleriaBackend.web.dto.ProductRequest;
import com.example.PasteleriaBackend.web.dto.ProductResponse;

public interface ProductService {

    List<ProductResponse> findAllActive();

    ProductResponse findById(String id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(String id, ProductRequest request);

    void delete(String id);
}
