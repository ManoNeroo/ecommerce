package com.manonero.ecommerce.repositories;

import com.manonero.ecommerce.entities.ProductPurchased;

public interface IProductPurchasedRepository {
    ProductPurchased save(ProductPurchased productPurchased);
    ProductPurchased selectByPK(String productId, int userId);
}
