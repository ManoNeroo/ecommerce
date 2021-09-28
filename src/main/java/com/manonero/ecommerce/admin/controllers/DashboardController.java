package com.manonero.ecommerce.admin.controllers;

import java.util.Calendar;
import java.util.List;

import com.manonero.ecommerce.configs.AppSettings;
import com.manonero.ecommerce.entities.EarningView;
import com.manonero.ecommerce.entities.Order;
import com.manonero.ecommerce.entities.Product;
import com.manonero.ecommerce.services.IEarningService;
import com.manonero.ecommerce.services.IOrderService;
import com.manonero.ecommerce.services.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {

	@Autowired
	private IEarningService earningService;

	@Autowired
	private IProductService productService;

	@Autowired 
	private IOrderService orderService;

	@GetMapping()
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
	public String index(Model model) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
        List<EarningView> earningViews = earningService.getByYear(year);
		List<Order> orders = orderService.filter(0, 8, null, null, null, null, null, null, null);
		List<Product> topProduct = productService.getTopSale(8);
		model.addAttribute("topOrders", orders);
		model.addAttribute("statusNames", AppSettings.orderStatusNames);
		model.addAttribute("earningList", earningViews);
		model.addAttribute("topSales", topProduct);
		return "admin/dashboard/index";
	}

}