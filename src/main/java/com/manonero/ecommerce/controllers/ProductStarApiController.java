package com.manonero.ecommerce.controllers;

import com.manonero.ecommerce.entities.ProductStarView;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.IProductStarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productstar")
public class ProductStarApiController {

    @Autowired
    private IProductStarService starService;
    
    @GetMapping("/{id}")
    public Response getById(@PathVariable String id) {
        ProductStarView productStar = starService.getById(id);
        return new Response(productStar, true);
    }
}
