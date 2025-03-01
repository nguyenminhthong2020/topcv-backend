package com.example.Job.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép tất cả đường dẫn API
                        .allowedOrigins("*") // Cho phép tất cả các nguồn
                        // .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(false); // ⚠ Nếu true, "*" không dùng được trong allowedOrigins
            }
        };
    }
}
