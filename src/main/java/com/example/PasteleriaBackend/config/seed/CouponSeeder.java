package com.example.PasteleriaBackend.config.seed;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.model.Coupon;
import com.example.PasteleriaBackend.domain.model.CouponType;
import com.example.PasteleriaBackend.domain.repository.CouponRepository;

@Component
@Order(30)
@Transactional
public class CouponSeeder implements ApplicationRunner {

    private final CouponRepository couponRepository;

    public CouponSeeder(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (couponRepository.count() > 0) {
            return;
        }

        Coupon envioGratis = new Coupon();
        envioGratis.setCode("ENVIOGRATIS");
        envioGratis.setType(CouponType.SHIP);
        envioGratis.setValue(0d);
        envioGratis.setLabel("Envío gratis");
        envioGratis.setActive(true);

        Coupon descuentoCinco = new Coupon();
        descuentoCinco.setCode("5000OFF");
        descuentoCinco.setType(CouponType.AMOUNT);
        descuentoCinco.setValue(5000d);
        descuentoCinco.setLabel("Descuento de $5.000");
        descuentoCinco.setActive(true);

        Coupon felicesCincuenta = new Coupon();
        felicesCincuenta.setCode("FELICES50");
        felicesCincuenta.setType(CouponType.AMOUNT);
        felicesCincuenta.setValue(0d);
        felicesCincuenta.setLabel("Beneficio FELICES50");
        felicesCincuenta.setActive(true);

        couponRepository.saveAll(List.of(envioGratis, descuentoCinco, felicesCincuenta));
    }
}
