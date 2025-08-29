package com.conrado.couponapi.service.solver;

import com.conrado.couponapi.model.CouponResult;
import com.conrado.couponapi.model.Item;

import java.math.BigDecimal;
import java.util.List;

public interface CouponSolver {
    CouponResult solve(List<Item> items, BigDecimal amount);
}
