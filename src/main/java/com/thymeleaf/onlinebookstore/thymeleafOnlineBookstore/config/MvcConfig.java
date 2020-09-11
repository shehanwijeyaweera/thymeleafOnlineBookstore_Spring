package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path logoUploadDir = Paths.get("./logos");
        String logoUploadPath = logoUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/logos/**").addResourceLocations("file:/" + logoUploadPath + "/");
    }
}
