package com.example.PasteleriaBackend.service;

import java.util.List;

import com.example.PasteleriaBackend.web.dto.CreateOrderRequest;
import com.example.PasteleriaBackend.web.dto.OrderResponse;
import com.example.PasteleriaBackend.web.dto.UpdateOrderStatusRequest;

public interface OrderService {

    OrderResponse createOrder(String userId, CreateOrderRequest request);

    List<OrderResponse> findOrdersForCustomer(String userId);

    List<OrderResponse> findAllOrders();

    OrderResponse updateStatus(String orderId, UpdateOrderStatusRequest request);
}
