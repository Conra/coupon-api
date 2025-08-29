package com.conrado.couponapi.controller;

import com.conrado.couponapi.model.dto.CouponRequest;
import com.conrado.couponapi.model.dto.CouponResponse;
import com.conrado.couponapi.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/")
    public ResponseEntity<CouponResponse> calculateCoupon(@RequestBody CouponRequest couponRequest) {
        CouponResponse response = couponService.calculateCoupon(couponRequest.getItemIds(), couponRequest.getAmount());
        return ResponseEntity.ok(response);
    }
}
