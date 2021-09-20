package com.manonero.ecommerce.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.manonero.ecommerce.services.IUserAccountService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig {
	@Autowired
	private IUserAccountService userService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Configuration
	@Order(1)
	public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private DaoAuthenticationProvider authenticationProvider;

		@Autowired
		private IUserAccountService userService;

		@Autowired
		private AdminPageSuccessHandler adminPageSuccessHandler;

		public AdminSecurityConfig() {
			super();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authenticationProvider);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().antMatcher("/admin/**").authorizeRequests().and().formLogin()
					.loginPage("/admin/login").loginProcessingUrl("/admin/login").failureUrl("/admin/login?error=true")
					.successHandler(adminPageSuccessHandler).permitAll().and().rememberMe()
					.rememberMeCookieName("admin-rememberme").key("61admin*!remember#me@ecommerce$group2")
					.userDetailsService(userService).and().logout().logoutUrl("/admin/logout")
					.logoutSuccessUrl("/admin/login")
					.deleteCookies("JSESSIONID", "admin-rememberme", "customer-rememberme").and().exceptionHandling()
					.accessDeniedPage("/admin/403");
		}
	}

	@Configuration
	@Order(2)
	public static class CustomerSecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private DaoAuthenticationProvider authenticationProvider;

		@Autowired
		private IUserAccountService userService;

		@Autowired
		private CustomerPageSuccessHandler customerPageSuccessHandler;

		public CustomerSecurityConfig() {
			super();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authenticationProvider);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().antMatcher("/**").authorizeRequests().and().formLogin().loginPage("/login")
					.loginProcessingUrl("/login").failureUrl("/login?error=true")
					.successHandler(customerPageSuccessHandler).permitAll().and().rememberMe()
					.rememberMeCookieName("customer-rememberme").key("42customer+=remember#me@ecommerce$group2")
					.userDetailsService(userService).and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
					.deleteCookies("JSESSIONID", "admin-rememberme", "customer-rememberme").and().exceptionHandling()
					.accessDeniedPage("/403");
		}
	}
}
