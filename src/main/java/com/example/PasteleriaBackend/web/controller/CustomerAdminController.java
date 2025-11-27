package com.example.PasteleriaBackend.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PasteleriaBackend.service.CustomerService;
import com.example.PasteleriaBackend.web.dto.CustomerStatusRequest;
import com.example.PasteleriaBackend.web.dto.CustomerSummaryResponse;
import com.example.PasteleriaBackend.web.dto.CustomerUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/customers")
@Validated
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administración de clientes")
public class CustomerAdminController {

    private final CustomerService customerService;

    public CustomerAdminController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Listar clientes registrados")
    public ResponseEntity<List<CustomerSummaryResponse>> listCustomers() {
        return ResponseEntity.ok(customerService.listCustomers());
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "Actualizar los datos de un cliente por un administrador")
    public ResponseEntity<CustomerSummaryResponse> updateCustomer(
        @PathVariable String customerId,
        @Valid @RequestBody CustomerUpdateRequest request
    ) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, request));
    }

    @PatchMapping("/{customerId}/status")
    @Operation(summary = "Actualizar el estado activo/inactivo de un cliente")
    public ResponseEntity<Void> updateStatus(
        @PathVariable String customerId,
        @Valid @RequestBody CustomerStatusRequest request
    ) {
        customerService.updateCustomerStatus(customerId, request.status());
        return ResponseEntity.noContent().build();
    }
}
