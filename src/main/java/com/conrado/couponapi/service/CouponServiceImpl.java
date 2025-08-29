package com.conrado.couponapi.service;

import com.conrado.couponapi.model.CouponResult;
import com.conrado.couponapi.model.Item;
import com.conrado.couponapi.model.dto.CouponResponse;
import com.conrado.couponapi.service.client.MercadoLibreClient;
import com.conrado.couponapi.service.solver.CouponSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    private final MercadoLibreClient mercadoLibreClient;
    private final CouponSolver couponSolver;

    @Autowired
    public CouponServiceImpl(MercadoLibreClient mercadoLibreClient, CouponSolver couponSolver) {
        this.mercadoLibreClient = mercadoLibreClient;
        this.couponSolver = couponSolver;
    }

    @Override
    public CouponResponse calculateCoupon(List<String> itemIds, BigDecimal amount) {
        List<Item> items = mercadoLibreClient.fetchItems(itemIds);
        CouponResult result = couponSolver.solve(items, amount);

        return new CouponResponse(result.getItems(), result.getTotal());
    }
}
