package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import com.manonero.ecommerce.configs.OrderStatusEnum;
import com.manonero.ecommerce.entities.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements IOrderRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Order> selectByUserId(int userId) {
        TypedQuery<Order> query = entityManager.createQuery("from Order where userId=:uid", Order.class);
        query.setParameter("uid", userId);
        List<Order> orders = query.getResultList();
        return orders;
    }

    @Override
    public Order save(Order order) {
        Order rs = entityManager.merge(order);
        return rs;
    }

    @Override
    public Integer updateOrderStatus(String orderId, int status) {
        try {
            int statusVal = OrderStatusEnum.values()[status].getValue();
            StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Order.updateOrderStatus");
            query.setParameter("orderId", orderId);
            query.setParameter("status", statusVal);
            query.executeUpdate();
            return statusVal;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

}
