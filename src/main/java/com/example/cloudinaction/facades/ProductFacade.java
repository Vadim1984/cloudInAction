package com.example.cloudinaction.facades;

import com.example.cloudinaction.dto.ProductDto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductFacade {
    ProductDto saveProduct(ProductDto product);
    void deleteProduct(Long id);
    ProductDto getProductById(Long id);
    ProductDto updateProduct(Long id, ProductDto product);
    List<ProductDto> getProductNameContains(String nameSubString, Pageable pageData);
    List<ProductDto> getProductsPriceBetween(BigDecimal min, BigDecimal max, Pageable pageData);
    List<ProductDto> getAllProducts(Pageable pageData);
}
