package com.manonero.ecommerce.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_item")
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", foreignKey = @ForeignKey(name = "order_item_order_id_FK"))
    @ManyToOne(optional = false)
    private Order order;

    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_promo_price")
    private int productPromoPrice;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "product_avatar")
    private String productAvatar;

    @Column(name = "quanlity")
    private int quanlity;

    @Column(name = "created_at")
    private Date createdAt;

    public OrderItem() {
    }

    public OrderItem(Order order, String productId, String productName, int productPromoPrice, int productPrice,
            String productAvatar, int quanlity, Date createdAt) {
        this.order = order;
        this.productId = productId;
        this.productName = productName;
        this.productPromoPrice = productPromoPrice;
        this.productPrice = productPrice;
        this.productAvatar = productAvatar;
        this.quanlity = quanlity;
        this.createdAt = createdAt;
    }

    @JsonIgnore
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPromoPrice() {
        return productPromoPrice;
    }

    public void setProductPromoPrice(int productPromoPrice) {
        this.productPromoPrice = productPromoPrice;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductAvatar() {
        return productAvatar;
    }

    public void setProductAvatar(String productAvatar) {
        this.productAvatar = productAvatar;
    }

    public int getQuanlity() {
        return quanlity;
    }

    public void setQuanlity(int quanlity) {
        this.quanlity = quanlity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
