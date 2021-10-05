package com.manonero.ecommerce.controllers;

import java.util.List;

import com.manonero.ecommerce.entities.ProductEvaluation;
import com.manonero.ecommerce.models.PaginationResponse;
import com.manonero.ecommerce.models.ProductEvaluationRequest;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.IProductEvaluationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evaluation")
public class ProductEvaluationApiController {

    @Autowired
    private IProductEvaluationService evaluationService;

    @GetMapping("/filter")
    public PaginationResponse filter(@RequestParam String productId, @RequestParam(required = false) Integer page,
    @RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer userId) {
        if (limit == null) {
            limit = 1;
        }
        if (page == null) {
            page = 1;
        }
        if (page < 1) {
            page = 1;
        }
        int offset = (page * limit) - limit + 1;
        List<ProductEvaluation> evaluations = evaluationService.filter(offset, limit, productId, userId);
        int totalItem = evaluationService.getNumberEvaluation();
        return new PaginationResponse(evaluations, true, limit, page, totalItem);
    }

    @PostMapping()
    public Response add(@RequestBody ProductEvaluationRequest request) {
        ProductEvaluation evaluation = evaluationService.add(request);
        return new Response(evaluation, true);
    }

    @PutMapping 
    public Response edit(@RequestBody ProductEvaluationRequest request) {
        ProductEvaluation evaluation = evaluationService.edit(request);
        if(evaluation != null) {
            return new Response(evaluation, true);
        }
        return new Response(false);
    }

    @DeleteMapping("{id}")
    public Response delete(@PathVariable int id) {
        ProductEvaluation evaluation = evaluationService.remove(id);
        if(evaluation != null) {
            return new Response(evaluation, true);
        }
        return new Response(false);
    }

    @GetMapping("/{id}")
    public Response getById(@PathVariable int id) {
        return new Response(evaluationService.getById(id), true);
    }
}
