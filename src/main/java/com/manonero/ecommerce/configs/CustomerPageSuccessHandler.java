package com.manonero.ecommerce.configs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.services.IUserAccountService;

@Component
public class CustomerPageSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private IUserAccountService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		String userName = authentication.getName();
		
		System.out.println("userName=" + userName);

		UserAccount userAccount = userService.findByUserName(userName);
		
		System.out.println("Full Name Is: " + userAccount.getFullName());
		
		HttpSession session = request.getSession();
		session.setAttribute("user", userAccount);
		
		response.sendRedirect(request.getContextPath() + "/");
	}

}
