package com.manonero.ecommerce.customer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController {

    @GetMapping()
    public String index(@RequestParam(required = false) String keyword) {
        if(keyword != null) {
            if(keyword.trim() != "") {
                return "customer/search/index";
            }
        }
        return "customer/error/404";
    }
}
