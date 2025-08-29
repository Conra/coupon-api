package com.conrado.couponapi.service;

import com.conrado.couponapi.model.dto.CouponResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface CouponService {
    CouponResponse calculateCoupon (List<String> itemIds, BigDecimal amount);
}
