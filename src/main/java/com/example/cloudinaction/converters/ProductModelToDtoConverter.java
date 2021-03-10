package com.example.cloudinaction.converters;

import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.models.Category;
import com.example.cloudinaction.models.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;

public class ProductModelToDtoConverter implements Converter<Product, ProductDto> {

    @Override
    public ProductDto convert(Product source) {
        ProductDto productDto = new ProductDto();
        productDto.setId(source.getId());
        productDto.setName(source.getName());
        productDto.setPrice(source.getPrice());

        List<Long> categoryIds = Optional.ofNullable(source.getCategories())
                .orElseGet(ArrayList::new)
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        productDto.setCategories(categoryIds);

        return productDto;
    }
}
