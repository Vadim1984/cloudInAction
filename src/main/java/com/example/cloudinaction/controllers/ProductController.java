package com.example.cloudinaction.controllers;

import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.facades.ProductFacade;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private static final String NAME_FIELD = "name";
    private static final String PRICE_FIELD = "price";

    @Autowired
    private ProductFacade productFacade;

    @PostMapping("/product")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto product) {
        ProductDto productDto = productFacade.saveProduct(product);

        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(value = "id") Long id) {
        productFacade.deleteProduct(id);

        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/product/{id}")
    public ProductDto getProductById(@PathVariable(value = "id") Long id) {
        return productFacade.getProductById(id);
    }

    @PutMapping("/product/{id}")
    public ProductDto updateProduct(@PathVariable(value = "id") Long id, @Valid @RequestBody ProductDto product) {
        return productFacade.updateProduct(id, product);
    }

    @GetMapping("/products/name")
    public List<ProductDto> getProductNameContains(@RequestParam(value = "nameSubString") String nameSubString,
                                                   @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int offset,
                                                   @RequestParam(value = "size", defaultValue = "10", required = false) int limit) {
        Sort sort = Sort.by(NAME_FIELD).and(Sort.by(PRICE_FIELD));
        Pageable pageData = PageRequest.of(offset, limit, sort);

        return productFacade.getProductNameContains(nameSubString, pageData);
    }

    @GetMapping("/products/price")
    public List<ProductDto> getProductsPriceBetween(@Valid @RequestParam(value = "pricemin") BigDecimal min,
                                                    @Valid @RequestParam(value = "pricemax") BigDecimal max,
                                                    @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int offset,
                                                    @RequestParam(value = "size", defaultValue = "10", required = false) int limit) {
        Sort sort = Sort.by(NAME_FIELD).and(Sort.by(PRICE_FIELD));
        Pageable pageData = PageRequest.of(offset, limit, sort);

        return productFacade.getProductsPriceBetween(min, max, pageData);
    }

    @GetMapping("/products")
    public List<ProductDto> getAllProducts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int offset,
                                           @RequestParam(value = "size", defaultValue = "10", required = false) int limit) {
        Sort sort = Sort.by(NAME_FIELD).and(Sort.by(PRICE_FIELD));
        Pageable pageData = PageRequest.of(offset, limit, sort);

        return productFacade.getAllProducts(pageData);
    }
}
