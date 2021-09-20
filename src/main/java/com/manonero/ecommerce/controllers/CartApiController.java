package com.manonero.ecommerce.controllers;

import com.manonero.ecommerce.entities.Cart;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.ICartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {
    @Autowired 
    private ICartService cartService;

    @GetMapping("/{id}")
    public Response getById(@PathVariable String id) {
        Cart cart = cartService.getById(id);
        return new Response(cart, true);
    }

    @GetMapping("/{id}/numberitem")
    public Response getNumberItem(@PathVariable String id) {
        int numberItem = cartService.getNumberItem(id);
        return new Response(numberItem, true);
    }

}
