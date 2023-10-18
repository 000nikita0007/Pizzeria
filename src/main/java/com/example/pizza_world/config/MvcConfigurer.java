package com.example.pizza_world.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//Приложение понимало откуда брать ресурсы
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/static/**", "/resources/**", "/js/**", "/loaded_images/**")
                .addResourceLocations("classpath:/css/", "classpath:/static/", "classpath:/resources/", "classpath:/js/");
    }
}
