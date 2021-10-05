package com.manonero.ecommerce.customer.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.manonero.ecommerce.configs.AppSettings;
import com.manonero.ecommerce.configs.OrderStatusEnum;
import com.manonero.ecommerce.entities.Order;
import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.models.OrderRequest;
import com.manonero.ecommerce.services.IOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/order")
public class CustomerOrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String index(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String search, HttpServletRequest request, Model model) {
        int limit = 10;
        if (page == null) {
            page = 1;
        }
        if (page < 1) {
            page = 1;
        }
        int offset = (page * limit) - limit + 1;
        UserAccount account = (UserAccount) request.getSession().getAttribute("user");
        int[] statues = null;
        if (type != null) {
            statues = new int[] { type };
        }
        List<Order> orders = orderService.filter(offset, limit, account.getId(), search, statues, search, null, null,
                null);
        int total = orderService.getNumberOrder();
        model.addAttribute("orders", orders);
        model.addAttribute("statusNames", AppSettings.orderStatusNames);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", limit);
        model.addAttribute("totalItem", total);
        return "customer/order/index";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String detail(@PathVariable String id, Model model) {
        Order order = orderService.getById(id);
        if (order != null) {
            model.addAttribute("order", order);
            model.addAttribute("statusNames", AppSettings.orderStatusNames);
            return "customer/order/detail";
        }
        return "customer/error/404";
    }

    @GetMapping("/success")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String success(@RequestParam String id, Model model) {
        Order order = orderService.getById(id);
        if (order != null) {
            model.addAttribute("order", order);
            return "customer/order/success";
        }
        return "customer/error/404";
    }

    @GetMapping("/cancel/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String cancel(@PathVariable String id) {
        Order order = orderService.getById(id);
        if(order != null) {
            if(order.getStatus() == OrderStatusEnum.UNCONFIRM.getValue()) {
                OrderRequest request = new OrderRequest();
                request.setOrderId(id);
                request.setStatus(4);
                orderService.updateOrderStatus(request);
                return "redirect:/user/order?type=4";
            }
        }
        return "customer/error/404";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String updateCusInfo(@PathVariable String id, HttpServletRequest request) {
        Order dbOrder = orderService.getById(id);
        String rqId = request.getParameter("orderId");
        if (dbOrder.getId().equals(rqId)) {
            if (dbOrder.getStatus() == 0) {
                UserAccount account = (UserAccount) request.getSession().getAttribute("user");
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setUserId(account.getId());
                orderRequest.setOrderId(rqId);
                orderRequest.setFullName(request.getParameter("fullName"));
                orderRequest.setAddress(request.getParameter("address"));
                orderRequest.setPhoneNumber(request.getParameter("phoneNumber"));
                orderRequest.setGender(Boolean.parseBoolean(request.getParameter("gender")));
                orderRequest.setDescription(request.getParameter("description"));
                orderService.save(orderRequest);
            }
        }
        return "redirect:/user/order/" + id;
    }

}
