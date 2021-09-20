package com.manonero.ecommerce.controllers;

import com.manonero.ecommerce.models.CartItemRequest;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.ICartItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cartitem")
public class CartItemApiController {

    @Autowired
    private ICartItemService cartItemService;

    @PostMapping()
    public Response add(@RequestBody CartItemRequest request) {
        cartItemService.addCartItem(request);
        return new Response(true);
    }

    @PutMapping()
    public Response edit(@RequestBody CartItemRequest request) {
        cartItemService.editCartItem(request);
        return new Response(true);
    }

    @DeleteMapping()
    public Response delete(@RequestBody CartItemRequest request) {
        cartItemService.deleteCartItem(request);
        return new Response(true);
    }
}
