package com.manonero.ecommerce.entities;

import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "Cart.selectNumberCartItem", procedureName = "usp_selectNumberCartItem", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "cartId", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "numberItem", type = Integer.class) })
})
public class Cart {
    @Id
    @Column(name = "cart_id")
    private String id;

    @Column(name = "number_item")
    private int numberItem;

    @Column(name = "user_id")
    private int userId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private Collection<CartItem> cartItems;

    public Cart() {
    }

    public Cart(String id, int numberItem, int userId, Collection<CartItem> cartItems) {
        this.id = id;
        this.numberItem = numberItem;
        this.userId = userId;
        this.cartItems = cartItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumberItem() {
        return numberItem;
    }

    public void setNumberItem(int numberItem) {
        this.numberItem = numberItem;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Collection<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Collection<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

}
