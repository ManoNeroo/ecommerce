package com.manonero.ecommerce.models;

public class CartItemRequest {
    private String cartId;
    private String productId;
    private int quanlity;
    private Boolean checked;

    public CartItemRequest() {
    }

    public CartItemRequest(String cartId, String productId, int quanlity, Boolean checked) {
        this.cartId = cartId;
        this.productId = productId;
        this.quanlity = quanlity;
        this.checked = checked;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

}
