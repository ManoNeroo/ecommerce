package com.manonero.ecommerce.repositories;

import java.util.List;

import com.manonero.ecommerce.entities.ProductComment;

public interface IProductCommentRepository {
    List<ProductComment> selectLevel1(String productId, Integer userId, Boolean isOfUser);
    ProductComment selectById(int id);
    ProductComment insert(ProductComment productComment);
    ProductComment update(ProductComment productComment);
    ProductComment delete(int id);
    List<ProductComment> selectByParentId(int parentId);
}
