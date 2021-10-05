package com.manonero.ecommerce.customer.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/notification")
public class CustomerNotificationController {

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String index() {
        return "customer/notification/index";
    }
}
