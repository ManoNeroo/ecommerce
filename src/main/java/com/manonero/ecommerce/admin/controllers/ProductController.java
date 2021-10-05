package com.manonero.ecommerce.admin.controllers;

import java.util.List;

import com.manonero.ecommerce.entities.Brand;
import com.manonero.ecommerce.entities.Category;
import com.manonero.ecommerce.entities.Product;
import com.manonero.ecommerce.services.IBrandService;
import com.manonero.ecommerce.services.ICategoryService;
import com.manonero.ecommerce.services.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IBrandService brandService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public String index(@RequestParam(required = false) Integer page, @RequestParam(required = false) int[] brand,
            @RequestParam(required = false) int[] category, @RequestParam(required = false) int[] priceRange,
            @RequestParam(required = false) String name, @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Boolean isSort, Model model) {
        int limit = 10;
        if (page == null) {
            page = 1;
        }
        if (page < 1) {
            page = 1;
        }
        int offset = (page * limit) - limit + 1;
        List<Product> list = productService.filterProduct(offset, limit, category, brand, priceRange, name, status,
                isSort, null);
        int total = productService.getProductCount();
        List<Brand> brands = brandService.getAllBrand(null, true);
        List<Category> categories = categoryService.getAllCategory(null, true);
        model.addAttribute("list", list);
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryIds", category);
        model.addAttribute("brandIds", brand);
        model.addAttribute("priceRangeIxs", priceRange);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", limit);
        model.addAttribute("totalItem", total);
        model.addAttribute("searchName", name);
        model.addAttribute("searchStatus", status);
        model.addAttribute("searchSort", isSort);
        return "admin/product/index";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public String add(Model model) {
        List<Brand> brands = brandService.getAllBrand(null, true);
        List<Category> categories = categoryService.getAllCategory(null, true);
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        return "admin/product/add";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public String edit(Model model, @PathVariable String id) {
        Product product = productService.getProductById(id);
        if(product == null) {
            return "admin/error/404";
        }
        model.addAttribute("product", product);
        return "admin/product/edit";
    }
}
