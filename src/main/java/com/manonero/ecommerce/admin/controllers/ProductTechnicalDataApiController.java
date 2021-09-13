package com.manonero.ecommerce.admin.controllers;

import com.manonero.ecommerce.entities.ProductTechnicalData;
import com.manonero.ecommerce.models.ProductTechnicalDataRequest;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.IProductTechnicalDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/technicaldata")
public class ProductTechnicalDataApiController {
    @Autowired
    private IProductTechnicalDataService technicalDataService;

    @GetMapping("/product/{id}")
    public Response getByProductId(@PathVariable String id) {
        ProductTechnicalData technicalData = technicalDataService.getByProductId(id);
        return new Response(technicalData, true);
    }

    @PostMapping()
    public Response save(@RequestBody ProductTechnicalDataRequest request) {
        ProductTechnicalData technicalData = technicalDataService.save(request);
        return new Response(technicalData, true);
    }
}
