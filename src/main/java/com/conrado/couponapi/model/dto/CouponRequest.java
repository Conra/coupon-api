package com.conrado.couponapi.model.dto;

import java.math.BigDecimal;
import java.util.List;

public class CouponRequest {
    private List<String> item_ids;
    private BigDecimal amount;

    public List<String> getItemIds() {
        return item_ids;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
