package com.manonero.ecommerce.customer.controllers;

import java.util.List;

import com.manonero.ecommerce.entities.Product;
import com.manonero.ecommerce.entities.ProductArticle;
import com.manonero.ecommerce.entities.ProductPicture;
import com.manonero.ecommerce.entities.ProductTechnicalData;
import com.manonero.ecommerce.services.IProductArticleService;
import com.manonero.ecommerce.services.IProductPictureService;
import com.manonero.ecommerce.services.IProductService;
import com.manonero.ecommerce.services.IProductTechnicalDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class CustomerProductController {

    @Autowired
    private IProductService productService;
    @Autowired
    private IProductArticleService articleService;
    @Autowired
    private IProductTechnicalDataService technicalDataService;
    @Autowired
    private IProductPictureService pictureService;

    @GetMapping("/{id}")
    public String detail(@PathVariable String id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            if (product.isStatus()) {
                ProductArticle article = articleService.getArticleByProductId(id);
                ProductTechnicalData technicalData = technicalDataService.getByProductId(id);
                List<ProductPicture> pictures = pictureService.getByProductId(id);
                model.addAttribute("product", product);
                model.addAttribute("article", article);
                model.addAttribute("technical", technicalData);
                model.addAttribute("pictures", pictures);
                return "customer/product/detail";
            }
        }
        return "customer/error/404";
    }

    @GetMapping()
    private String index() {
        return "customer/product/index";
    }
}
