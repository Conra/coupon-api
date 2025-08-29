package com.conrado.couponapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String id;
    private BigDecimal price;

    public Item() {
    }

    public Item(String id, BigDecimal price) {
        this.id = id;
        this.price = price;
    }

    public String getId() { return id; }
    public BigDecimal getPrice() { return price; }
}
