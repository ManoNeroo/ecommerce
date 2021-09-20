package com.manonero.ecommerce.services;

import java.util.List;

import com.manonero.ecommerce.entities.Order;
import com.manonero.ecommerce.models.OrderRequest;

public interface IOrderService {
    List<Order> getByUserId(int userId);
    Order save(OrderRequest request);
    Integer updateOrderStatus(OrderRequest request);
}
