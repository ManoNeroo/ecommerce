package com.manonero.ecommerce.controllers;

import java.util.List;

import com.manonero.ecommerce.entities.Order;
import com.manonero.ecommerce.models.OrderRequest;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.IOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/userid/{id}")
    public Response getByUserId(@PathVariable int id) {
        List<Order> orders = orderService.getByUserId(id);
        return new Response(orders, true);
    }

    @PostMapping()
    public Response add(@RequestBody OrderRequest request) {
        Order order = orderService.save(request);
        if(order != null) {
            return new Response(order, true);
        }
        return new Response(false);
    }

    @PutMapping("/status")
    public Response updateOrderStatus(@RequestBody OrderRequest request) {
        Integer status = orderService.updateOrderStatus(request);
        return new Response(status, true);
    }
}
