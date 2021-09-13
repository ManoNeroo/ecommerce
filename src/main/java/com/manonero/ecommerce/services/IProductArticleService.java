package com.manonero.ecommerce.services;

import com.manonero.ecommerce.entities.ProductArticle;
import com.manonero.ecommerce.models.ProductArticleRequest;

public interface IProductArticleService {
    ProductArticle getArticleByProductId(String productId);
    ProductArticle save(ProductArticleRequest request);
}
