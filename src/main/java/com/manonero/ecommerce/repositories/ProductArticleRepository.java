package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.manonero.ecommerce.entities.ProductArticle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductArticleRepository implements IProductArticleRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    public ProductArticle selectByProductId(String productId) {
        String queryStr = "SELECT * FROM product_article WHERE product_id=?";
        Query query = entityManager.createNativeQuery(queryStr, ProductArticle.class);
        query.setParameter(1, productId);
        @SuppressWarnings("unchecked")
        List<ProductArticle> articles = query.getResultList();
        if(articles.size() > 0) {
            return articles.get(0);
        }
        return null;
    }

    @Override
    public ProductArticle save(ProductArticle productArticle) {
        ProductArticle pa = entityManager.merge(productArticle);
        return pa;
    }
    
}
