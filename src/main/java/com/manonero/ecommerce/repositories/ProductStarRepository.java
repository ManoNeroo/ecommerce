package com.manonero.ecommerce.repositories;

import javax.persistence.EntityManager;

import com.manonero.ecommerce.entities.ProductStarView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductStarRepository implements IProductStarRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public ProductStarView selectById(String id) {
        return entityManager.find(ProductStarView.class, id);
    }
    
}
