package com.example.cloudinaction.converters;

import com.example.cloudinaction.dto.CategoryDto;
import com.example.cloudinaction.models.Category;
import com.example.cloudinaction.models.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;

public class CategoryDtoToModelConverter implements Converter<CategoryDto, Category> {

    private ProductDtoToModelConverter productDtoToModelConverter = new ProductDtoToModelConverter();

    @Override
    public Category convert(CategoryDto source) {

        Category category = new Category();
        category.setName(source.getName());

        List<Product> products = Optional.ofNullable(source.getProducts())
                .orElseGet(ArrayList::new)
                .stream()
                .map(productDtoToModelConverter::convert)
                .collect(Collectors.toList());

        category.setProducts(products);

        return category;
    }
}
