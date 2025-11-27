package com.example.PasteleriaBackend.service;

import java.util.List;

import com.example.PasteleriaBackend.web.dto.CouponResponse;

public interface CouponService {

    List<CouponResponse> findAllActive();

    CouponResponse findByCode(String code);
}
