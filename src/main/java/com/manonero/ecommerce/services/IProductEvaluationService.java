package com.manonero.ecommerce.services;

import java.util.List;

import com.manonero.ecommerce.entities.ProductEvaluation;
import com.manonero.ecommerce.models.ProductEvaluationRequest;

public interface IProductEvaluationService {
    List<ProductEvaluation> filter(Integer offset, Integer limit, String productId, Integer userId);
    ProductEvaluation add(ProductEvaluationRequest request);
    ProductEvaluation edit(ProductEvaluationRequest request);
    ProductEvaluation remove(int id);
    int getNumberEvaluation();
    ProductEvaluation getById(int id);
}
