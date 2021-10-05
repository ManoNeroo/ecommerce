package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import com.manonero.ecommerce.entities.ProductComment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductCommentRepository implements IProductCommentRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductComment> selectLevel1(String productId, Integer userId, Boolean isOfUser) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("ProductComment.selectProductComment");
        query.setParameter("productId", productId);
        query.setParameter("userId", userId);
        query.setParameter("isOfUser", isOfUser);
        return query.getResultList();
    }

    @Override
    public ProductComment selectById(int id) {
        return entityManager.find(ProductComment.class, id);
    }

    @Override
    public ProductComment insert(ProductComment productComment) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("ProductComment.insertProductComment");
        query.setParameter("productId", productComment.getProductId());
        query.setParameter("userId", productComment.getUserAccount().getId());
        query.setParameter("content", productComment.getContent());
        query.setParameter("parentId", productComment.getParentId());
        query.setParameter("createdAt", productComment.getCreatedAt());
        query.execute();
        Integer id = (Integer) query.getOutputParameterValue("id");
        if (id == null) {
            id = 0;
        }
        productComment.setId(id);
        return productComment;
    }

    @Override
    public ProductComment update(ProductComment productComment) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("ProductComment.updateProductComment");
        query.setParameter("id", productComment.getId());
        query.setParameter("content", productComment.getContent());
        query.execute();
        return selectById(productComment.getId());
    }

    @Override
    public ProductComment delete(int id) {
        ProductComment comment = selectById(id);
        if (comment != null) {
            StoredProcedureQuery query = entityManager
                    .createNamedStoredProcedureQuery("ProductComment.deleteProductComment");
            query.setParameter("id", id);
            query.execute();
            return comment;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductComment> selectByParentId(int parentId) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("ProductComment.selectProductCommentByParentId");
        query.setParameter("parentId", parentId);
        return query.getResultList();
    }

}
