package com.conrado.couponapi.service.client;

import com.conrado.couponapi.model.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoLibreClientImpl implements MercadoLibreClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Item> fetchItems (List<String> itemIds) {
        List<Item> items = new ArrayList<>();
        for (String id : itemIds) {
            String url = "https://api.mercadolibre.com/items/" + id;
            try {
                ResponseEntity<Item> response = restTemplate.getForEntity(url, Item.class);
                items.add(response.getBody());
            } catch (Exception e) {
                // Log error
            }
        }
        return items;
    }
}
