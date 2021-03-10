package com.example.cloudinaction.converters;

import com.example.cloudinaction.dto.CategoryDto;
import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.models.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;

public class CategoryModelToDtoConverter implements Converter<Category, CategoryDto> {

    private ProductModelToDtoConverter productModelToDtoConverter = new ProductModelToDtoConverter();

    @Override
    public CategoryDto convert(Category source) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(source.getId());
        categoryDto.setName(source.getName());

        final List<ProductDto> products = Optional.ofNullable(source.getProducts())
                .orElseGet(ArrayList::new)
                .stream()
                .map(productModelToDtoConverter::convert)
                .collect(Collectors.toList());

        categoryDto.setProducts(products);

        return categoryDto;
    }
}
