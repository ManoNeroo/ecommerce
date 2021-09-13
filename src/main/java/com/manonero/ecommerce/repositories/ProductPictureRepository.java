package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.manonero.ecommerce.entities.ProductPicture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductPictureRepository implements IProductPictureRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ProductPicture> selectByProductId(String productId) {
        String queryStr = "SELECT * FROM product_picture WHERE product_id='"+ productId + "'";
        Query query = entityManager.createNativeQuery(queryStr, ProductPicture.class);
        @SuppressWarnings("unchecked")
        List<ProductPicture> list = query.getResultList();
        return list;
    }

    @Override
    public ProductPicture save(ProductPicture productPicture) {
        ProductPicture pp = entityManager.merge(productPicture);
        return pp;
    }

    @Override
    public ProductPicture delete(int pictureId) {
        ProductPicture picture = entityManager.find(ProductPicture.class, pictureId);
        entityManager.remove(picture);
        return picture;
    }
    
}
