package com.manonero.ecommerce.repositories;

import com.manonero.ecommerce.entities.ProductTechnicalData;

public interface IProductTechnicalDataRepository {
    ProductTechnicalData selectByProductId(String productId);
    ProductTechnicalData save(ProductTechnicalData technicalData);
}
