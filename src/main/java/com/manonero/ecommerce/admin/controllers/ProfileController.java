package com.manonero.ecommerce.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.services.IUserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    @Autowired
    private IUserAccountService accountService;

    @GetMapping("/admin/profile")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public String index() {
        return "/admin/profile/index";
    }

    @GetMapping("/admin/password")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public String changePassword() {
        return "/admin/profile/password";
    }

    @GetMapping("/admin/resetusersession")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public String resetUserSession(@RequestParam String returnUrl, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserAccount oldUserSession = (UserAccount) session.getAttribute("user");
        UserAccount newUserSession = accountService.getById(oldUserSession.getId());
        session.setAttribute("user", newUserSession);
        return "redirect:" + returnUrl;
    }
}
