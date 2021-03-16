package com.example.cloudinaction.services.impl;

import com.example.cloudinaction.dao.ProductRepository;
import com.example.cloudinaction.exceptions.ProductNotFoundException;
import com.example.cloudinaction.models.Product;
import com.example.cloudinaction.services.ProductService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultProductService implements ProductService {
    static final String PRODUCT_NOT_FOUND_MESSAGE = "product(s) with %s: [%s] not found";
    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_MESSAGE, ID_FIELD, id)));

        product.getCategories().forEach(category -> category.getProducts().remove(product));
        productRepository.delete(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_MESSAGE, ID_FIELD, id)));
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product targetProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_MESSAGE, ID_FIELD, id)));

        targetProduct.setName(product.getName());
        targetProduct.setPrice(product.getPrice());
        targetProduct.setCategories(product.getCategories());

        return productRepository.save(targetProduct);
    }

    @Override
    public List<Product> getProductNameContains(String nameSubString, Pageable pageData) {
        List<Product> products = productRepository.findByNameContains(nameSubString, pageData);

        if (products.isEmpty()) {
            throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_MESSAGE, NAME_FIELD, nameSubString));
        }

        return products;
    }

    @Override
    public List<Product> getProductsPriceBetween(BigDecimal min, BigDecimal max, Pageable pageData) {
        List<Product> products = productRepository.findByPriceBetween(min, max, pageData);

        if (products.isEmpty()) {
            throw new ProductNotFoundException(String.format("Products with price in range: [%s - %s] not found", min, max));
        }

        return products;
    }

    @Override
    public List<Product> getAllProducts(Pageable pageData) {
        return productRepository.findAll(pageData).getContent();
    }
}
