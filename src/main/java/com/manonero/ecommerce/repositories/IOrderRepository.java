package com.manonero.ecommerce.repositories;

import java.util.Date;
import java.util.List;

import com.manonero.ecommerce.entities.Order;

public interface IOrderRepository {
    List<Order> selectByUserId(int userId);
    List<Order> selectFilter(Integer userId, String orderId, int[] orderStatuses, String productName, 
    String phoneNumber, Date beginDate, Date endDate);
    Order save(Order order);
    Integer updateOrderStatus(String orderId, int status);
    Order selectById(String id);
}
