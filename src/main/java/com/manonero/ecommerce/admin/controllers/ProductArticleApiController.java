package com.manonero.ecommerce.admin.controllers;

import com.manonero.ecommerce.entities.ProductArticle;
import com.manonero.ecommerce.models.ProductArticleRequest;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.IProductArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productarticle")
public class ProductArticleApiController {
    @Autowired
    private IProductArticleService articleService;

    @GetMapping("/product/{id}")
    public Response getArticleByProductId(@PathVariable String id) {
        ProductArticle article = articleService.getArticleByProductId(id);
        return new Response(article, true);
    }

    @PostMapping()
    public Response addArticle(@RequestBody ProductArticleRequest request) {
        ProductArticle article = articleService.save(request);
        return new Response(article, true);
    }
}
