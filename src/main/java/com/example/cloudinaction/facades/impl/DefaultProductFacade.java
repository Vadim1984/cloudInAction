package com.example.cloudinaction.facades.impl;

import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.facades.ProductFacade;
import com.example.cloudinaction.models.Product;
import com.example.cloudinaction.services.ProductService;
import com.example.cloudinaction.util.ProductUtil;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultProductFacade implements ProductFacade {

    @Autowired
    @Qualifier("mvcConversionService")
    private ConversionService conversionService;
    @Autowired
    private ProductService productService;

    @Override
    public ProductDto saveProduct(ProductDto product) {
        Product productModel = productService.saveProduct(conversionService.convert(product, Product.class));

        return conversionService.convert(productModel, ProductDto.class);
    }

    @Override
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productService.getProductById(id);

        return conversionService.convert(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productService.updateProduct(id, conversionService.convert(productDto, Product.class));

        return conversionService.convert(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> getProductNameContains(String nameSubString, Pageable pageData) {
        List<Product> products = productService.getProductNameContains(nameSubString, pageData);

        return ProductUtil.convertListModelToListDto(products);
    }

    @Override
    public List<ProductDto> getProductsPriceBetween(BigDecimal min, BigDecimal max, Pageable pageData) {
        List<Product> products = productService.getProductsPriceBetween(min, max, pageData);

        return ProductUtil.convertListModelToListDto(products);
    }

    @Override
    public List<ProductDto> getAllProducts(Pageable pageData) {
        List<Product> allProducts = productService.getAllProducts(pageData);

        return ProductUtil.convertListModelToListDto(allProducts);
    }
}
