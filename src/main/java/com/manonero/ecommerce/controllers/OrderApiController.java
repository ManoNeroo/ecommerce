package com.manonero.ecommerce.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/filter")
    public Response filter(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer userId, @RequestParam(required = false) String orderId,
            @RequestParam(required = false) int[] orderStatuses, @RequestParam(required = false) String productName,
            @RequestParam(required = false) String phoneNumber, @RequestParam(required = false) String beginDate,
            @RequestParam(required = false) String endDate) {
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
        Date bDate = null;
        Date eDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            bDate = dateFormat.parse(beginDate);
            eDate = dateFormat.parse(endDate);
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        List<Order> orders = orderService.filter(offset, limit, userId, orderId, orderStatuses, productName, phoneNumber,
        bDate, eDate);
        return new Response(orders, true);
    }

    @PostMapping()
    public Response add(@RequestBody OrderRequest request) {
        Order order = orderService.save(request);
        if (order != null) {
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
