package com.manonero.ecommerce.repositories;

import com.manonero.ecommerce.entities.ProductArticle;

public interface IProductArticleRepository {
    ProductArticle selectByProductId(String productId);
    ProductArticle save(ProductArticle productArticle);
}
