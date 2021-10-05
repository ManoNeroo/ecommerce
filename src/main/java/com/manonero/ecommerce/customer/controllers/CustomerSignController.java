package com.manonero.ecommerce.customer.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerSignController {


	@GetMapping("/login")
	public String signin(@RequestParam(defaultValue = "false") boolean error) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			return "customer/sign/signin";
		}
		return "redirect:/";
	}

	@GetMapping("/register")
	public String signup() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			return "customer/sign/signup";
		}
		return "redirect:/";
	}
	
}
