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
public class AdminPageSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private IUserAccountService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		boolean isAllow = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN")
				|| r.getAuthority().equals("ROLE_MANAGER") || r.getAuthority().equals("ROLE_EMPLOYEE"));

		if (!isAllow) {
			response.sendRedirect(request.getContextPath() + "/admin/logout");
		} else {
			String userName = authentication.getName();

			System.out.println("User name is: " + userName);

			UserAccount userAccount = userService.findByUserName(userName);

			System.out.println("Full name is: " + userAccount.getFullName());

			if(userAccount.getStatus() != null) {
				if(userAccount.getStatus() == true) {
					HttpSession session = request.getSession();
					session.setAttribute("user", userAccount);
					response.sendRedirect(request.getContextPath() + "/admin/dashboard");
				} else {
					response.sendRedirect(request.getContextPath() + "/admin/logout");
				}
			} else {
				response.sendRedirect(request.getContextPath() + "/admin/logout");
			}
		}
	}

}
