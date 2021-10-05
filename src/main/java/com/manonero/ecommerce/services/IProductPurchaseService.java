package com.manonero.ecommerce.services;

import com.manonero.ecommerce.entities.ProductPurchased;

public interface IProductPurchaseService {
    int checkUserEvaluated(String productId, int userId);
    ProductPurchased save(ProductPurchased productPurchased);
}
