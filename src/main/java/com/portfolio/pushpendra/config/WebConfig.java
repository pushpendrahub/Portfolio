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
        // Default profile images (packaged inside /static)
        registry.addResourceHandler("/assets/img/profile/**")
                .addResourceLocations("classpath:/static/assets/img/profile/");

        // Default certificate images
        registry.addResourceHandler("/assets/img/certificates/**")
                .addResourceLocations("classpath:/static/assets/img/certificates/");

        // Default project images
        registry.addResourceHandler("/assets/img/project/**")
                .addResourceLocations("classpath:/static/assets/img/project/");
    }
}