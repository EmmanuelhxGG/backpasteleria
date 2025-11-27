package com.example.PasteleriaBackend.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PasteleriaBackend.service.CouponService;
import com.example.PasteleriaBackend.web.dto.CouponResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/coupons")
@Tag(name = "Cupones")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    @Operation(summary = "Listar cupones activos", description = "Retorna los cupones disponibles para aplicar en compras")
    public ResponseEntity<List<CouponResponse>> listActive() {
        return ResponseEntity.ok(couponService.findAllActive());
    }

    @GetMapping("/{code}")
    @Operation(summary = "Obtener un cupón por código", description = "Permite validar un código de cupón específico")
    public ResponseEntity<CouponResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(couponService.findByCode(code));
    }
}
