package com.example.PasteleriaBackend.service;

import java.util.List;

import com.example.PasteleriaBackend.domain.model.UserStatus;
import com.example.PasteleriaBackend.web.dto.CustomerProfileResponse;
import com.example.PasteleriaBackend.web.dto.CustomerRegistrationRequest;
import com.example.PasteleriaBackend.web.dto.CustomerSummaryResponse;
import com.example.PasteleriaBackend.web.dto.CustomerUpdateRequest;

public interface CustomerService {

    CustomerProfileResponse register(CustomerRegistrationRequest request);

    CustomerProfileResponse getCurrentProfile(String userId);

    CustomerProfileResponse updateCurrentProfile(String userId, CustomerUpdateRequest request);

    List<CustomerSummaryResponse> listCustomers();

    CustomerSummaryResponse updateCustomer(String customerId, CustomerUpdateRequest request);

    void updateCustomerStatus(String customerId, UserStatus status);
}
