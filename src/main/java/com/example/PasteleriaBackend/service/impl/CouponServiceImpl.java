package com.example.PasteleriaBackend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.model.Coupon;
import com.example.PasteleriaBackend.domain.repository.CouponRepository;
import com.example.PasteleriaBackend.service.CouponService;
import com.example.PasteleriaBackend.web.dto.CouponResponse;

@Service
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public List<CouponResponse> findAllActive() {
        return couponRepository.findByActiveTrue().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public CouponResponse findByCode(String code) {
        return couponRepository.findByCodeIgnoreCase(code)
            .filter(Coupon::isActive)
            .map(this::toResponse)
            .orElseThrow(() -> new IllegalArgumentException("Cupón no válido"));
    }

    private CouponResponse toResponse(Coupon coupon) {
        String type = coupon.getType() != null ? coupon.getType().name().toLowerCase() : null;
        return new CouponResponse(coupon.getCode(), type, coupon.getValue(), coupon.getLabel());
    }
}
