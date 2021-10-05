package com.manonero.ecommerce.controllers;

import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.IProductPurchaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchased")
public class ProductPurchasedApiController {
    @Autowired
    private IProductPurchaseService purchaseService;

    @GetMapping("/checkpurchased")
    public Response checkPurchased(@RequestParam String productId, @RequestParam int userId) {
        return new Response(purchaseService.checkUserEvaluated(productId, userId), true);
    }
}
