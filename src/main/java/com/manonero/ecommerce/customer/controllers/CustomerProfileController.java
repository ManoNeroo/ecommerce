package com.manonero.ecommerce.customer.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.services.IUserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/account")
public class CustomerProfileController {

    @Autowired
    private IUserAccountService accountService;
    
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String index() {
        return "customer/profile/index";
    }

    @GetMapping("/password")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String password() {
        return "/customer/profile/password";
    }

    @GetMapping("/resetusersession")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String updateBasicInfo(@RequestParam String returnUrl, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserAccount oldUserSession = (UserAccount) session.getAttribute("user");
        UserAccount newUserSession = accountService.getById(oldUserSession.getId());
        session.setAttribute("user", newUserSession);
        return "redirect:" + returnUrl;
    }
}
