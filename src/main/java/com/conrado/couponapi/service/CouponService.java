package com.conrado.couponapi.service;

import com.conrado.couponapi.model.dto.CouponResponse;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService {
    CouponResponse calculateCoupon (List<String> itemIds, BigDecimal amount);
}
