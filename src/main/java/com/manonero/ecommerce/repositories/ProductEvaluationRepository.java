package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import com.manonero.ecommerce.entities.ProductEvaluation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductEvaluationRepository implements IProductEvaluationRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductEvaluation> selectLevel1(String productId, Integer userId, Boolean isOfUser) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("ProductEvaluation.selectProductEvaluation");
        query.setParameter("productId", productId);
        query.setParameter("userId", userId);
        query.setParameter("isOfUser", isOfUser);
        return query.getResultList();
    }

    @Override
    public ProductEvaluation selectById(int id) {
        return entityManager.find(ProductEvaluation.class, id);
    }

    @Override
    public ProductEvaluation insert(ProductEvaluation productEvaluation) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("ProductEvaluation.insertProductEvaluation");
        query.setParameter("productId", productEvaluation.getProductId());
        query.setParameter("userId", productEvaluation.getUserAccount().getId());
        query.setParameter("content", productEvaluation.getContent());
        query.setParameter("star", productEvaluation.getStar());
        query.setParameter("parentId", productEvaluation.getParentId());
        query.setParameter("createdAt", productEvaluation.getCreatedAt());
        query.execute();
        Integer id = (Integer) query.getOutputParameterValue("id");
        if (id == null) {
            id = 0;
        }
        productEvaluation.setId(id);
        return productEvaluation;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductEvaluation> selectByParentId(int parentId) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("ProductEvaluation.selectProductEvaluationByParentId");
        query.setParameter("parentId", parentId);
        return query.getResultList();
    }

    @Override
    public ProductEvaluation update(ProductEvaluation productEvaluation) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("ProductEvaluation.updateProductEvaluation");
        query.setParameter("id", productEvaluation.getId());
        query.setParameter("content", productEvaluation.getContent());
        query.setParameter("star", productEvaluation.getStar());
        query.execute();
        return selectById(productEvaluation.getId());
    }

    @Override
    public ProductEvaluation delete(int id) {
        ProductEvaluation evaluation = selectById(id);
        if (evaluation != null) {
            StoredProcedureQuery query = entityManager
                    .createNamedStoredProcedureQuery("ProductEvaluation.deleteProductEvaluation");
            query.setParameter("id", id);
            query.execute();
            return evaluation;
        }
        return null;
    }

}
