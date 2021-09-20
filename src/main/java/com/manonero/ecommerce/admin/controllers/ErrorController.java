package com.manonero.ecommerce.admin.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
public class ErrorController {
	@GetMapping("/404")
	public String notfound() {
		return "admin/error/404";
	}
	
	@GetMapping("/403")
	public String forbidden() {
		return "admin/error/403";
	}
}
