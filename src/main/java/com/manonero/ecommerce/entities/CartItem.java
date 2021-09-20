package com.manonero.ecommerce.entities;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cart_item")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "CartItem.insertCartItem", procedureName = "usp_insertCartItem", parameters = {
                @StoredProcedureParameter(name = "cartId", type = String.class),
                @StoredProcedureParameter(name = "productId", type = String.class),
                @StoredProcedureParameter(name = "quanlity", type = Integer.class),
                @StoredProcedureParameter(name = "checked", type = Boolean.class) }),
        @NamedStoredProcedureQuery(name = "CartItem.updateCartItem", procedureName = "usp_updateCartItem", parameters = {
                @StoredProcedureParameter(name = "cartId", type = String.class),
                @StoredProcedureParameter(name = "productId", type = String.class),
                @StoredProcedureParameter(name = "quanlity", type = Integer.class),
                @StoredProcedureParameter(name = "checked", type = Boolean.class) }),
        @NamedStoredProcedureQuery(name = "CartItem.deleteCartItem", procedureName = "usp_deleteCartItem", parameters = {
                @StoredProcedureParameter(name = "cartId", type = String.class),
                @StoredProcedureParameter(name = "productId", type = String.class) }) })
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "quanlity")
    private int quanlity;

    @Column(name = "checked")
    private Boolean checked;

    @Id
    @JoinColumn(name = "cart_id", referencedColumnName = "cart_id", foreignKey = @ForeignKey(name = "cart_item_cart_id_FK"))
    @ManyToOne(optional = false)
    private Cart cart;

    @Id
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", foreignKey = @ForeignKey(name = "cart_item_product_id_FK"))
    @ManyToOne(optional = false)
    private Product product;

    public CartItem() {
    }

    public CartItem(int quanlity, Boolean checked, Cart cart, Product product) {
        this.quanlity = quanlity;
        this.checked = checked;
        this.cart = cart;
        this.product = product;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getQuanlity() {
        return quanlity;
    }

    public void setQuanlity(int quanlity) {
        this.quanlity = quanlity;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @JsonIgnore
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
