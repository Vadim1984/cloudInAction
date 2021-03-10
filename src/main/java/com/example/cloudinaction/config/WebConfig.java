package com.example.cloudinaction.config;

import com.example.cloudinaction.converters.CategoryDtoToModelConverter;
import com.example.cloudinaction.converters.CategoryModelToDtoConverter;
import com.example.cloudinaction.converters.ProductDtoToModelConverter;
import com.example.cloudinaction.converters.ProductModelToDtoConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ProductModelToDtoConverter());
        registry.addConverter(new ProductDtoToModelConverter());
        registry.addConverter(new CategoryDtoToModelConverter());
        registry.addConverter(new CategoryModelToDtoConverter());
    }
}
