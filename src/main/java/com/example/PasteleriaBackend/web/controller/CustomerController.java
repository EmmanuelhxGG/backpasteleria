package com.example.PasteleriaBackend.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PasteleriaBackend.security.UserPrincipal;
import com.example.PasteleriaBackend.service.CustomerService;
import com.example.PasteleriaBackend.web.dto.CustomerProfileResponse;
import com.example.PasteleriaBackend.web.dto.CustomerRegistrationRequest;
import com.example.PasteleriaBackend.web.dto.CustomerUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Clientes")
@Validated
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo cliente")
    public ResponseEntity<CustomerProfileResponse> register(@Valid @RequestBody CustomerRegistrationRequest request) {
        return new ResponseEntity<>(customerService.register(request), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Obtener el perfil del cliente autenticado")
    public ResponseEntity<CustomerProfileResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(customerService.getCurrentProfile(principal.getId()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Actualizar el perfil del cliente autenticado")
    public ResponseEntity<CustomerProfileResponse> updateMe(
        @AuthenticationPrincipal UserPrincipal principal,
        @Valid @RequestBody CustomerUpdateRequest request
    ) {
        return ResponseEntity.ok(customerService.updateCurrentProfile(principal.getId(), request));
    }
}
