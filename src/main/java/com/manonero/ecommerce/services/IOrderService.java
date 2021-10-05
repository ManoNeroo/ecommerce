package com.manonero.ecommerce.services;

import java.util.Date;
import java.util.List;

import com.manonero.ecommerce.entities.Order;
import com.manonero.ecommerce.models.OrderRequest;

public interface IOrderService {
    List<Order> getByUserId(int userId);
    List<Order> filter(Integer offset, Integer limit, Integer userId, String orderId, int[] orderStatuses, String productName,
    String phoneNumber, Date beginDate, Date endDate);
    Order save(OrderRequest request);
    Order updateOrderStatus(OrderRequest request);
    int getNumberOrder();
    Order getById(String id);
}
