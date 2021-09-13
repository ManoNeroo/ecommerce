package com.manonero.ecommerce.services;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.Product;
import com.manonero.ecommerce.entities.ProductArticle;
import com.manonero.ecommerce.models.ProductArticleRequest;
import com.manonero.ecommerce.repositories.IProductArticleRepository;
import com.manonero.ecommerce.repositories.IProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductArticleService implements IProductArticleService {

    @Autowired
    private IProductArticleRepository articleRepository;
    @Autowired
    private IProductRepository productRepository;

    @Override
    @Transactional
    public ProductArticle getArticleByProductId(String productId) {
        return articleRepository.selectByProductId(productId);
    }

    @Override
    @Transactional
    public ProductArticle save(ProductArticleRequest request) {
        ProductArticle article = new ProductArticle();
        article.setId(request.getId());
        article.setContent(request.getContent());
        Product product = productRepository.selectProductById(request.getProductId());
        article.setProduct(product);
        return articleRepository.save(article);
    }
    
}
