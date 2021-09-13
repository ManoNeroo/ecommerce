package com.manonero.ecommerce.admin.controllers;

import java.util.List;

import com.manonero.ecommerce.entities.Brand;
import com.manonero.ecommerce.models.BrandRequest;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.IBrandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/brand")
public class BrandApiController {
    @Autowired
	private IBrandService brandService;

	@GetMapping()
	public Response getAll() {
		List<Brand> list = brandService.getAllBrand(null);
		return new Response(list, true);
	}
    
    @PostMapping()
	public Response addBrand(@RequestBody BrandRequest request) {
		Brand b = brandService.save(request);
		return new Response(b, true);
	}

	@PutMapping("{id}")
	public Response updateBrand(@RequestBody BrandRequest request, @PathVariable int id) {
		Brand b = brandService.save(request);
		return new Response(b, true);
	}

	@GetMapping("{id}")
	public Response getById(@PathVariable int id) {
		Brand brand = brandService.getBrandById(id);
		return new Response(brand, true);
	}

	@GetMapping("/togglestatus")
	public Response toggleStatus(@RequestParam int id, @RequestParam boolean status) {
		boolean rs = brandService.toggleBrandStatus(id, status);
		return new Response(rs);
	}
}
