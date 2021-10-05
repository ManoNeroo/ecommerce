package com.manonero.ecommerce.admin.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.manonero.ecommerce.configs.AppSettings;
import com.manonero.ecommerce.entities.Order;
import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.models.OrderRequest;
import com.manonero.ecommerce.services.IOrderService;
import com.manonero.ecommerce.services.IUserAccountService;

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
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserAccountService accountService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public String index(@RequestParam(required = false) Integer page, @RequestParam(required = false) int[] statuses,
            @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String search, Model model) {
        int limit = 10;
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
            bDate = dateFormat.parse(startDate);
            eDate = dateFormat.parse(endDate);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        List<Order> orders = orderService.filter(offset, limit, null, search, statuses, search, search, bDate, eDate);
        int total = orderService.getNumberOrder();
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", limit);
        model.addAttribute("totalItem", total);
        model.addAttribute("statuses", statuses);
        model.addAttribute("startDate", bDate);
        model.addAttribute("endDate", eDate);
        model.addAttribute("statusNames", AppSettings.orderStatusNames);
        return "admin/order/index";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public String getById(@PathVariable String id, Model model) {
        Order order = orderService.getById(id);
        if (order != null) {
            UserAccount userAccount = accountService.getById(order.getUserId());
            model.addAttribute("order", order);
            model.addAttribute("account", userAccount);
            model.addAttribute("statusNames", AppSettings.orderStatusNames);
            return "admin/order/detail";
        }
        return "admin/error/404";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public String updateCusInfo(@PathVariable String id, HttpServletRequest request) {
        Order dbOrder = orderService.getById(id);
        String rqId = request.getParameter("orderId");
        if (dbOrder.getId().equals(rqId)) {
            if (dbOrder.getStatus() < 2) {
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setUserId(Integer.parseInt(request.getParameter("userId")));
                orderRequest.setOrderId(rqId);
                orderRequest.setFullName(request.getParameter("fullName"));
                orderRequest.setAddress(request.getParameter("address"));
                orderRequest.setPhoneNumber(request.getParameter("phoneNumber"));
                orderRequest.setGender(Boolean.parseBoolean(request.getParameter("gender")));
                orderRequest.setDescription(request.getParameter("description"));
                orderService.save(orderRequest);
            }
        }
        return "redirect:/admin/order/" + id;
    }
}
