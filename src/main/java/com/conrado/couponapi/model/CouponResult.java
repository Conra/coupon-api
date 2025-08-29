package com.conrado.couponapi.model;

import java.math.BigDecimal;
import java.util.List;

public class CouponResult {
    private List<String> items;
    private BigDecimal total;

    public CouponResult() {
    }

    public CouponResult(List<String> items, BigDecimal total) {
        this.items = items;
        this.total = total;
    }

    public List<String> getItems() { return items; }
    public BigDecimal getTotal() { return total; }
}
