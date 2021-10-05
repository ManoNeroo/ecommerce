package com.manonero.ecommerce.repositories;

import java.util.List;

import com.manonero.ecommerce.entities.ProductEvaluation;

public interface IProductEvaluationRepository {
    List<ProductEvaluation> selectLevel1(String productId, Integer userId, Boolean isOfUser);
    ProductEvaluation selectById(int id);
    ProductEvaluation insert(ProductEvaluation productEvaluation);
    ProductEvaluation update(ProductEvaluation productEvaluation);
    ProductEvaluation delete(int id);
    List<ProductEvaluation> selectByParentId(int parentId);
}
