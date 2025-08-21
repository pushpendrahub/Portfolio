package com.portfolio.pushpendra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private VisitorLoggingInterceptor visitorLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitorLoggingInterceptor)
                .addPathPatterns("/**") // log all URLs
                .excludePathPatterns("/admin/**"); // optionally skip admin pages
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/img/profile/**")
                .addResourceLocations(
                        "classpath:/static/assets/img/profile/",  // default images
                        "file:D:/uploads/profile/"                // uploaded images
                );
    }
}