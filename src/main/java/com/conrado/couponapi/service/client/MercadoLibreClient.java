package com.conrado.couponapi.service.client;

import com.conrado.couponapi.model.Item;

import java.util.List;

public interface MercadoLibreClient {
    List<Item> fetchItems(List<String> itemIds);
}
