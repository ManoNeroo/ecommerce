package com.manonero.ecommerce.customer.controllers;

import com.manonero.ecommerce.entities.Cart;
import com.manonero.ecommerce.services.ICartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/cart")
public class CartController {
    @Autowired
    private ICartService cartService;
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Cart cart = cartService.getById(auth.getName());
        model.addAttribute("cart", cart);
        return "customer/cart/index";
    }
}
