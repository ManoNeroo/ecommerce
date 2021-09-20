package com.manonero.ecommerce.repositories;

import java.util.List;

import com.manonero.ecommerce.entities.Order;

public interface IOrderRepository {
    List<Order> selectByUserId(int userId);
    Order save(Order order);
    Integer updateOrderStatus(String orderId, int status);
}
