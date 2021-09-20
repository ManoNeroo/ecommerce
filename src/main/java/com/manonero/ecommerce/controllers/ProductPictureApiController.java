package com.manonero.ecommerce.controllers;

import java.util.List;

import com.manonero.ecommerce.entities.ProductPicture;
import com.manonero.ecommerce.models.ProductPictureRequest;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.IProductPictureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productpicture")
public class ProductPictureApiController {
    @Autowired
    private IProductPictureService pictureService;

    @GetMapping("/product/{id}")
    public Response getByProductId(@PathVariable String id) {
        List<ProductPicture> listProductPictures = pictureService.getByProductId(id);
        return new Response(listProductPictures, true);
    }

    @PostMapping()
    public Response save(@RequestBody ProductPictureRequest request) {
        ProductPicture picture = pictureService.save(request);
        return new Response(picture, true);
    }

    @DeleteMapping("{id}")
    public Response delete(@PathVariable int id) {
        ProductPicture picture = pictureService.delete(id);
        return new Response(picture, true);
    }
}
