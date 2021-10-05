package com.manonero.ecommerce.controllers;

import java.util.List;

import com.manonero.ecommerce.entities.ProductComment;
import com.manonero.ecommerce.models.PaginationResponse;
import com.manonero.ecommerce.models.ProductCommentRequest;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.IProductCommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
public class ProductCommentApiController {
    
    @Autowired
    private IProductCommentService commentService;

    @GetMapping("/filter")
    public PaginationResponse filter(@RequestParam String productId, @RequestParam(required = false) Integer page,
    @RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer userId) {
        if (limit == null) {
            limit = 1;
        }
        if (page == null) {
            page = 1;
        }
        if (page < 1) {
            page = 1;
        }
        int offset = (page * limit) - limit + 1;
        List<ProductComment> comments = commentService.filter(offset, limit, productId, userId);
        int totalItem = commentService.getNumberComment();
        return new PaginationResponse(comments, true, limit, page, totalItem);
    }

    @PostMapping()
    public Response add(@RequestBody ProductCommentRequest request) {
        ProductComment comment = commentService.add(request);
        return new Response(comment, true);
    }

    @PutMapping 
    public Response edit(@RequestBody ProductCommentRequest request) {
        ProductComment comment = commentService.edit(request);
        if(comment != null) {
            return new Response(comment, true);
        }
        return new Response(false);
    }

    @DeleteMapping("{id}")
    public Response delete(@PathVariable int id) {
        ProductComment comment = commentService.remove(id);
        if(comment != null) {
            return new Response(comment, true);
        }
        return new Response(false);
    }

    @GetMapping("/{id}")
    public Response getById(@PathVariable int id) {
        return new Response(commentService.getById(id), true);
    }

}
