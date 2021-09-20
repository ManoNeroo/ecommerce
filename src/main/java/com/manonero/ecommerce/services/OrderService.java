package com.manonero.ecommerce.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import com.manonero.ecommerce.configs.OrderStatusEnum;
import com.manonero.ecommerce.entities.Cart;
import com.manonero.ecommerce.entities.CartItem;
import com.manonero.ecommerce.entities.Order;
import com.manonero.ecommerce.entities.OrderItem;
import com.manonero.ecommerce.models.OrderRequest;
import com.manonero.ecommerce.repositories.IOrderRepository;
import com.manonero.ecommerce.utils.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private ICartService cartService;

    @Override
    @Transactional
    public List<Order> getByUserId(int userId) {
        return orderRepository.selectByUserId(userId);
    }

    @Override
    @Transactional
    public Order save(OrderRequest request) {
        Order order = new Order();
        String orderId = AppUtils.generateRandomString(10);
        order.setId(orderId);
        order.setUserId(request.getUserId());
        order.setAddress(request.getAddress());
        order.setFullName(request.getFullName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setStatus(OrderStatusEnum.UNCONFIRM.getValue());
        order.setDescription(request.getDescription());
        Date dateNow = new Date();
        order.setCreatedAt(dateNow);
        order.setUpdatedAt(dateNow);
        Set<OrderItem> orderItems = generateOrderItems(request.getUserName(), order);
        if (orderItems.size() > 0) {
            Order rs = orderRepository.save(order);
            // phải set order item sau khi save order, hibernate sẽ tự động thêm order item ứng với order vừa save
            rs.setOrderItems(orderItems);
            return rs;
        }
        return null;
    }

    private Set<OrderItem> generateOrderItems(String userName, Order order) {
        Cart cart = cartService.getById(userName);
        Set<OrderItem> orderItems = new HashSet<>();
        Date dateNow = new Date();
        if (cart.getCartItems().size() > 0) {
            for (CartItem item : cart.getCartItems()) {
                if (item.getChecked() != null) {
                    if (item.getChecked()) {
                        OrderItem oItem = new OrderItem();
                        oItem.setOrder(order);
                        oItem.setProductId(item.getProduct().getId());
                        oItem.setProductAvatar(item.getProduct().getAvatar());
                        oItem.setProductName(item.getProduct().getName());
                        oItem.setProductPrice(item.getProduct().getPrice());
                        oItem.setProductPromoPrice(item.getProduct().getPromoPrice());
                        oItem.setQuanlity(item.getQuanlity());
                        oItem.setCreatedAt(dateNow);
                        orderItems.add(oItem);
                    }
                }
            }
        }
        return orderItems;
    }

    @Override
    @Transactional
    public Integer updateOrderStatus(OrderRequest request) {
        return orderRepository.updateOrderStatus(request.getOrderId(), request.getStatus());
    }

}
