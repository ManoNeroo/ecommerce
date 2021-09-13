package com.manonero.ecommerce.services;

import com.manonero.ecommerce.entities.ProductTechnicalData;
import com.manonero.ecommerce.models.ProductTechnicalDataRequest;

public interface IProductTechnicalDataService {
    ProductTechnicalData getByProductId(String productId);
    ProductTechnicalData save(ProductTechnicalDataRequest request);
}
