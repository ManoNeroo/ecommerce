package com.manonero.ecommerce.admin.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class SignController {

	@GetMapping()
	public String index() {
		return "redirect:/admin/login";
	}

	@GetMapping("/login")
	public String signin(@RequestParam(defaultValue = "false") boolean error) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			return "admin/sign/signin";
		}
		return "redirect:/admin/dashboard";
	}
}
