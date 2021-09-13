package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.manonero.ecommerce.entities.ProductTechnicalData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductTechnicalDataRepository implements IProductTechnicalDataRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    public ProductTechnicalData selectByProductId(String productId) {
        String queryStr = "SELECT * FROM product_technical_data WHERE product_id=?";
        Query query = entityManager.createNativeQuery(queryStr, ProductTechnicalData.class);
        query.setParameter(1, productId);
        @SuppressWarnings("unchecked")
        List<ProductTechnicalData> articles = query.getResultList();
        if (articles.size() > 0) {
            return articles.get(0);
        }
        return null;
    }

    @Override
    public ProductTechnicalData save(ProductTechnicalData technicalData) {
        ProductTechnicalData ptd = entityManager.merge(technicalData);
        return ptd;
    }

}
