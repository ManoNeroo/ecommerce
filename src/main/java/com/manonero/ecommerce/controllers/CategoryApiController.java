package com.manonero.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.manonero.ecommerce.entities.Category;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.ICategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryApiController {
	@Autowired
	private ICategoryService categoryService;

	@GetMapping()
	public Response getAll(@RequestParam(required = false) Boolean isEnable, @RequestParam(required = false) Boolean isSortByName) {
		List<Category> categories = categoryService.getAllCategory(isEnable, isSortByName);
		return new Response(categories, true);
	}

	@PostMapping()
	public Response insertCategory(@RequestBody Category category) {
		boolean rs = categoryService.addCategory(category);
		return new Response(rs);
	}

	@PutMapping("{id}")
	public Response updateCategory(@RequestBody Category category, @PathVariable int id) {
		boolean rs = categoryService.editCategory(category);
		return new Response(rs);
	}

	@GetMapping("{id}")
	public Response getById(@PathVariable int id) {
		Category category = categoryService.getCategoryById(id);
		return new Response(category, true);
	}

	@GetMapping("/togglestatus")
	public Response toggleStatus(@RequestParam int id, @RequestParam boolean status) {
		boolean rs = categoryService.toggleCategoryStatus(id, status);
		return new Response(rs);
	}
}
