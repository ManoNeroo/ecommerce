package com.manonero.ecommerce.repositories;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import com.manonero.ecommerce.configs.OrderStatusEnum;
import com.manonero.ecommerce.entities.Order;
import com.manonero.ecommerce.utils.AppUtils;

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

    @Override
    public List<Order> selectFilter(Integer userId, String orderId, int[] orderStatuses, String productName,
            String phoneNumber, Date beginDate, Date endDate) {
        String mainQueryStr = "SELECT DISTINCT od.* FROM user_order od";
        String whereQueryString = "";
        if (userId != null) {
            whereQueryString += " WHERE od.user_id=" + userId;
        }
        if (orderId != null) {
            if (whereQueryString.equals("")) {
                whereQueryString += " WHERE (od.order_id LIKE '%" + orderId + "%'";
            } else {
                whereQueryString += " AND (od.order_id LIKE '%" + orderId + "%'";
            }
            if (phoneNumber != null) {
                whereQueryString += " OR od.buyer_phone_number LIKE '%" + phoneNumber + "%'";
            }
            if (productName != null) {
                mainQueryStr += ", order_item oi";
                whereQueryString += " OR [dbo].[uf_nonUnicode](oi.product_name) LIKE '%"
                        + AppUtils.removeAccent(productName) + "%') AND od.order_id=oi.order_id";
            } else {
                whereQueryString += ")";
            }
        }
        if (orderStatuses != null) {
            if (whereQueryString.equals("")) {
                for (int i = 0; i < orderStatuses.length; i++) {
                    if (i == 0) {
                        whereQueryString += " WHERE (od.order_status=" + orderStatuses[i];
                    } else {
                        whereQueryString += " OR od.order_status=" + orderStatuses[i];
                    }
                }
            } else {
                for (int i = 0; i < orderStatuses.length; i++) {
                    if (i == 0) {
                        whereQueryString += " AND (od.order_status=" + orderStatuses[i];
                    } else {
                        whereQueryString += " OR od.order_status=" + orderStatuses[i];
                    }
                }
            }
            whereQueryString += ")";
        }

        if (beginDate != null) {
            if (whereQueryString.equals("")) {
                whereQueryString += " WHERE od.created_at >= '" + new SimpleDateFormat("yyyy-MM-dd").format(beginDate)
                        + "'";
            } else {
                whereQueryString += " AND od.created_at >= '" + new SimpleDateFormat("yyyy-MM-dd").format(beginDate)
                        + "'";
            }
        }

        if (endDate != null) {
            if (whereQueryString.equals("")) {
                whereQueryString += " WHERE od.created_at <= '" + new SimpleDateFormat("yyyy-MM-dd").format(endDate)
                        + "'";
            } else {
                whereQueryString += " AND od.created_at <= '" + new SimpleDateFormat("yyyy-MM-dd").format(endDate)
                        + "'";
            }
        }
        mainQueryStr += whereQueryString + " ORDER BY od.created_at DESC";
        Query query = entityManager.createNativeQuery(mainQueryStr, Order.class);
        @SuppressWarnings("unchecked")
        List<Order> orders = query.getResultList();
        return orders;
    }

    @Override
    public Order selectById(String id) {
        Order order = entityManager.find(Order.class, id);
        return order;
    }

}
