package com.example.cloudinaction.converters;

import com.example.cloudinaction.dao.CategoryRepository;
import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.models.Product;
import org.springframework.core.convert.converter.Converter;

public class ProductDtoToModelConverter implements Converter<ProductDto, Product> {

    private CategoryRepository categoryRepository;

    @Override
    public Product convert(ProductDto source) {
        Product product = new Product();
        product.setName(source.getName());
        product.setPrice(source.getPrice());

        return product;
    }
}
