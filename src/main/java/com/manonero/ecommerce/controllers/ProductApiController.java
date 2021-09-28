package com.manonero.ecommerce.controllers;

import java.util.List;

import com.manonero.ecommerce.entities.Product;
import com.manonero.ecommerce.models.PaginationResponse;
import com.manonero.ecommerce.models.ProductRequest;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.models.UpdateProductStatusRequest;
import com.manonero.ecommerce.services.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductApiController {
    @Autowired
    private IProductService productService;

    @GetMapping("/filter")
    public PaginationResponse filterProduct(@RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit, @RequestParam(required = false) int[] brand,
            @RequestParam(required = false) int[] category, @RequestParam(required = false) int[] priceRange,
            @RequestParam(required = false) String name, @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Boolean isSort, @RequestParam(required = false) Boolean onlyEnable) {
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
        List<Product> products = productService.filterProduct(offset, limit, category, brand, priceRange, name, status,
                isSort, onlyEnable);
        int totalItem = productService.getProductCount();
        return new PaginationResponse(products, true, limit, page, totalItem);
    }

    @PostMapping
    public Response addProduct(@RequestBody ProductRequest request) {
        Product prod = productService.save(request);
        return new Response(prod, true);
    }

    @PutMapping("{id}")
    public Response updateProduct(@RequestBody ProductRequest request, @PathVariable String id) {
        Product prod = productService.save(request);
        return new Response(prod, true);
    }

    @PatchMapping("/togglestatus")
    public Response toggleStatus(@RequestBody UpdateProductStatusRequest request) {
        productService.updateProductStatus(request);
        return new Response(true);
    }

    @PatchMapping("/avatar")
    public Response toggleStatus(@RequestBody ProductRequest request) {
        productService.updateProductAvatar(request);
        return new Response(true);
    }

    @GetMapping("/top")
    public Response getTopProduct(@RequestParam int top, @RequestParam int[] categoryIds) {
        List<Object> list = productService.getTopProduct(top, categoryIds);
        return new Response(list, true);
    }

    @GetMapping("/topbyname")
    public Response getTopByName(@RequestParam int top,  @RequestParam String name, @RequestParam(required = false) Boolean status) {
        List<Product> list = productService.getTopByName(top, name, status);
        return new Response(list, true);
    }
}
