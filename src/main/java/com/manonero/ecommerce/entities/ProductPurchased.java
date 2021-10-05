package com.manonero.ecommerce.entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "product_purchased")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "ProductPurchased.saveProductPurchased", procedureName = "usp_saveProductPurchased", parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "productId", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "userId", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "hasEvaluated", type = Boolean.class) }) })
public class ProductPurchased implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "has_evaluated")
    private boolean hasEvaluated;

    public ProductPurchased() {
    }

    public ProductPurchased(int userId, String productId, boolean hasEvaluated) {
        this.userId = userId;
        this.productId = productId;
        this.hasEvaluated = hasEvaluated;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isHasEvaluated() {
        return hasEvaluated;
    }

    public void setHasEvaluated(boolean hasEvaluated) {
        this.hasEvaluated = hasEvaluated;
    }

}
