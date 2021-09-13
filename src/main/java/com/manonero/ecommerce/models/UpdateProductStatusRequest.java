package com.manonero.ecommerce.models;

public class UpdateProductStatusRequest {
    private Boolean status;
    private String productId;

    public UpdateProductStatusRequest() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
