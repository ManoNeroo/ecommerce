package com.manonero.ecommerce.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "user_order")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "Order.updateOrderStatus", procedureName = "usp_updateOrderStatus", parameters = {
                @StoredProcedureParameter(name = "orderId", type = String.class),
                @StoredProcedureParameter(name = "status", type = Integer.class) }) })
public class Order {
    @Id
    @Column(name = "order_id")
    private String id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "order_address")
    private String address;

    @Column(name = "order_status")
    private int status;

    @Column(name = "order_total")
    private int total;

    @Column(name = "buyer_full_name")
    private String fullName;

    @Column(name = "buyer_phone_number")
    private String phoneNumber;

    @Column(name = "order_description")
    private String description;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Collection<OrderItem> orderItems;

    public Order() {
    }

    public Order(String id, int userId, String address, int status, int total, String fullName, String phoneNumber,
            String description, Date createdAt, Date updatedAt, Collection<OrderItem> orderItems) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.status = status;
        this.total = total;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderItems = orderItems;
    }

    public Order(String id, int userId, String address, int status, int total, String fullName, String phoneNumber,
            String description, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.status = status;
        this.total = total;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Collection<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Collection<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

}
