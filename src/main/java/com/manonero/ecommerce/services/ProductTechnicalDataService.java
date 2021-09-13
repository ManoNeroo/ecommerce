package com.manonero.ecommerce.services;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.Product;
import com.manonero.ecommerce.entities.ProductTechnicalData;
import com.manonero.ecommerce.models.ProductTechnicalDataRequest;
import com.manonero.ecommerce.repositories.IProductRepository;
import com.manonero.ecommerce.repositories.IProductTechnicalDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductTechnicalDataService implements IProductTechnicalDataService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IProductTechnicalDataRepository technicalDataRepository;

    @Override
    @Transactional
    public ProductTechnicalData getByProductId(String productId) {
        return technicalDataRepository.selectByProductId(productId);
    }

    @Override
    @Transactional
    public ProductTechnicalData save(ProductTechnicalDataRequest request) {
        ProductTechnicalData technicalData = new ProductTechnicalData();
        technicalData.setId(request.getId());
        technicalData.setContent(request.getContent());
        Product product = productRepository.selectProductById(request.getProductId());
        technicalData.setProduct(product);
        return technicalDataRepository.save(technicalData);
    }
    
}
