package com.example.demo.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.exception.GlobalExceptionFilter;
import com.example.demo.service.Iservice.IErrorLogService;

@Configuration
public class FilterConfig {

    @Bean
    FilterRegistrationBean<GlobalExceptionFilter> loggingFilter(IErrorLogService errorLogService) {
        FilterRegistrationBean<GlobalExceptionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new GlobalExceptionFilter(errorLogService));
        registrationBean.addUrlPatterns("/api/*"); // Definisci l'URL pattern dove il filtro deve essere applicato
        return registrationBean;
    }
}