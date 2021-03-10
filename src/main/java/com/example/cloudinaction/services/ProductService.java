package com.example.cloudinaction.services;

import com.example.cloudinaction.models.Product;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    Product getProductById(Long id);
    Product updateProduct(Long id, Product product);
    List<Product> getProductNameContains(String nameSubString, Pageable pageData);
    List<Product> getProductsPriceBetween(BigDecimal min, BigDecimal max, Pageable pageData);
    List<Product> getAllProducts(Pageable pageData);
}
