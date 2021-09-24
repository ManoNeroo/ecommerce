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

    private int numberOrder;

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
        Order order = null;
        Date dateNow = new Date();
        if (request.getOrderId() != null) {
            order = getById(request.getOrderId());
        }
        if (order == null) {
            order = new Order();
            order.setStatus(OrderStatusEnum.UNCONFIRM.getValue());
            order.setCreatedAt(dateNow);
            order.setUpdatedAt(dateNow);
        } else {
            order.setUpdatedAt(dateNow);
        }
        order.setUserId(request.getUserId());
        order.setAddress(request.getAddress());
        order.setFullName(request.getFullName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setGender(request.getGender());
        order.setDescription(request.getDescription());
        if (order.getId() != null) {
            Order rs = orderRepository.save(order);
            return rs;
        }
        String orderId = AppUtils.generateRandomString(10);
        order.setId(orderId);
        Set<OrderItem> orderItems = generateOrderItems(request.getUserName(), order);
        if (orderItems.size() > 0) {
            Order rs = orderRepository.save(order);
            // phải set order item sau khi save order, hibernate sẽ tự động thêm order item
            // ứng với order vừa save
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

    @Override
    @Transactional
    public List<Order> filter(Integer offset, Integer limit, Integer userId, String orderId, int[] orderStatuses,
            String productName, String phoneNumber, Date beginDate, Date endDate) {
        List<Order> orders = orderRepository.selectFilter(userId, orderId, orderStatuses, productName, phoneNumber,
                beginDate, endDate);
        this.numberOrder = orders.size();
        if (offset != null && limit != null) {
            int ix1 = offset - 1;
            int ix2 = offset + limit - 1;
            ix1 = ix1 >= 0 ? ix1 : 0;
            ix2 = ix2 >= 1 ? ix2 : 1;
            ix1 = this.numberOrder > ix1 ? ix1 : 0;
            ix2 = this.numberOrder > ix2 ? ix2 : this.numberOrder;
            return orders.subList(ix1, ix2);
        }
        return orders;
    }

    public int getNumberOrder() {
        return numberOrder;
    }

    @Override
    @Transactional
    public Order getById(String id) {
        return orderRepository.selectById(id);
    }

}
