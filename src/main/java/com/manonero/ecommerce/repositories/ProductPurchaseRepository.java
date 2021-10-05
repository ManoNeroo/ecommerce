package com.manonero.ecommerce.repositories;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import com.manonero.ecommerce.entities.ProductPurchased;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductPurchaseRepository implements IProductPurchasedRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public ProductPurchased save(ProductPurchased productPurchased) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("ProductPurchased.saveProductPurchased");
        query.setParameter("productId", productPurchased.getProductId());
        query.setParameter("userId", productPurchased.getUserId());
        query.setParameter("hasEvaluated", productPurchased.isHasEvaluated());
        query.executeUpdate();
        return productPurchased;
    }

    @Override
    public ProductPurchased selectByPK(String productId, int userId) {
        TypedQuery<ProductPurchased> query = entityManager
                .createQuery("from ProductPurchased where userId=:uid and productId=:pid", ProductPurchased.class);
        query.setParameter("uid", userId);
        query.setParameter("pid", productId);
        try {
            ProductPurchased productPurchased = query.getSingleResult();
            return productPurchased;
        } catch (NoResultException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

}
