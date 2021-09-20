package com.manonero.ecommerce.configs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.services.IUserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserSessionInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserAccountService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        Object u = session.getAttribute("user");
        if (u == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!(auth instanceof AnonymousAuthenticationToken)) {
                String userName = auth.getName();
                System.out.println("User name is: " + userName);
                UserAccount userAccount = userService.findByUserName(userName);
                System.out.println("Full name is: " + userAccount.getFullName());
                session.setAttribute("user", userAccount);
            }
        }

        return true;
    }
}
