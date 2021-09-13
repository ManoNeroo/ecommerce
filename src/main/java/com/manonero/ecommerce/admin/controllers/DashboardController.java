package com.manonero.ecommerce.admin.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {

	@GetMapping()
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
	public String index() {
		return "admin/dashboard/index";
	}

	@GetMapping("/about")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public String about() {
		return "admin/about";
	}

	@GetMapping("/not-found")
	public String notfound() {
		return "admin/404";
	}

}