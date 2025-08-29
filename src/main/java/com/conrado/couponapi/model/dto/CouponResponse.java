package com.conrado.couponapi.model.dto;

import java.math.BigDecimal;
import java.util.List;

public class CouponResponse {
    private List<String> item_ids;
    private Double total;

    public CouponResponse(List<String> items, BigDecimal total) {
        this.item_ids = items;
        this.total = total.doubleValue();
    }
}
