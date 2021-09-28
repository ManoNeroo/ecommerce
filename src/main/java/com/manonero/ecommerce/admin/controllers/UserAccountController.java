package com.manonero.ecommerce.admin.controllers;

import java.util.List;

import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.services.IUserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/user")
public class UserAccountController {

    @Autowired
    private IUserAccountService accountService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public String index(@RequestParam(required = false) Integer page, @RequestParam(required = false) String search,
            @RequestParam(required = false) int[] roles, @RequestParam(required = false) Boolean status, Model model) {
        int limit = 10;
        if (page == null) {
            page = 1;
        }
        if (page < 1) {
            page = 1;
        }
        int offset = (page * limit) - limit + 1;
        List<UserAccount> accounts = accountService.filter(offset, limit, search, search, roles, status);
        int total = accountService.getNumberAccount();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", limit);
        model.addAttribute("totalItem", total);
        model.addAttribute("roles", roles);
        model.addAttribute("searchStatus", status);
        model.addAttribute("isAdmin", isAdmin);
        return "admin/user/index";
    }
}
