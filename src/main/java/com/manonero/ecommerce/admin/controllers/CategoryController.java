package com.manonero.ecommerce.admin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manonero.ecommerce.entities.Category;
import com.manonero.ecommerce.services.ICategoryService;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
	@Autowired
	private ICategoryService categoryService;

	@GetMapping()
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
	public String index(@RequestParam(required = false) Integer page, @RequestParam(required = false) String name,
			@RequestParam(required = false) Boolean status, Model model) {
		int limit = 10;
		if (page == null) {
			page = 1;
		}
		if (page < 1) {
			page = 1;
		}
		int offset = (page * limit) - limit + 1;
		List<Category> categories = categoryService.getCategoryByOffsetLimit(offset, limit, name, status);
		int total = categoryService.getCategoryCount(name, status);
		model.addAttribute("categories", categories);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", limit);
		model.addAttribute("totalItem", total);
		model.addAttribute("searchName", name);
		model.addAttribute("searchStatus", status);
		return "admin/category/index";
	}
}
