package com.manonero.ecommerce.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public UserSessionInterceptor userSessionInterceptor() {
        return new UserSessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userSessionInterceptor()).addPathPatterns("/admin/**", "/**")
                .excludePathPatterns("/admin/login", "/admin/logout", "/login", "/logout");
    }
}
