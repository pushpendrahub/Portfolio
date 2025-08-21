package com.portfolio.pushpendra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    private final Environment env;

    public StaticResourceConfig(Environment env) {
        this.env = env;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = env.getProperty("file.upload-dir");

        if (uploadDir == null) {
            throw new IllegalStateException("Property file.upload-dir is not set!");
        }

        if (!uploadDir.endsWith("/")) {
            uploadDir = uploadDir + "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
