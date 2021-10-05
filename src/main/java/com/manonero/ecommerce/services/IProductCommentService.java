package com.manonero.ecommerce.services;

import java.util.List;

import com.manonero.ecommerce.entities.ProductComment;
import com.manonero.ecommerce.models.ProductCommentRequest;

public interface IProductCommentService {
    List<ProductComment> filter(Integer offset, Integer limit, String productId, Integer userId);
    ProductComment add(ProductCommentRequest request);
    ProductComment edit(ProductCommentRequest request);
    ProductComment remove(int id);
    int getNumberComment();
    ProductComment getById(int id);
}
