package com.manonero.ecommerce.customer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerErrorController {
	@GetMapping("/404")
	public String notfound() {
		return "customer/error/404";
	}
	
	@GetMapping("/403")
	public String forbidden() {
		return "customer/error/403";
	}
}