package com.example.config;

import com.example.annotation.Idempotent;
import com.example.interceptor.IdempotentInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IdempotentWebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private IdempotentInterceptor idempotentInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(idempotentInterceptor)
                .addPathPatterns("/**");
    }
}
