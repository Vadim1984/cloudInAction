package com.example.cloudinaction.controllers;

import com.example.cloudinaction.dto.CategoryDto;
import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.facades.CategoryFacade;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CategoryController {

    @Autowired
    private CategoryFacade categoryFacade;

    @PostMapping("/category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto category) {
        CategoryDto categoryDto = categoryFacade.createCategory(category);

        //ResponseStatusException
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(value = "id") Long id) {
        categoryFacade.deleteCategory(id);

        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/category/{id}")
    public CategoryDto getCategoryById(@PathVariable(value = "id") Long id) {
        return categoryFacade.getCategoryById(id);
    }

    @PutMapping("/category/{id}")
    public CategoryDto updateCategory(@PathVariable(value = "id") Long id, @Valid @RequestBody CategoryDto category) {
        return categoryFacade.updateCategory(id, category);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int offset,
                                              @RequestParam(value = "size", defaultValue = "10", required = false) int limit) {
        //TODO: Sort sort = Sort.by("name").and(Sort.by("products.size()"));
        Sort sort = Sort.by("name");
        Pageable pageData = PageRequest.of(offset, limit, sort);
        return categoryFacade.getAllCategories(pageData);
    }

    @GetMapping("/category/{id}/products")
    public List<ProductDto> getProductOfCategory(@PathVariable(value = "id") Long id) {
        return categoryFacade.getProductsOfCategory(id);
    }

    @PutMapping("/category/{catId}/product/{prdId}/assign")
    public CategoryDto assignProductToCategory(@PathVariable(value = "catId") Long catId,
                                               @PathVariable(value = "prdId") Long prdId) {
        return categoryFacade.assignProductToCategory(catId, prdId);
    }

    @PutMapping("/category/{catId}/product/{prdId}/un-assign")
    public CategoryDto unAssignProductToCategory(@PathVariable(value = "catId") Long catId,
                                                 @PathVariable(value = "prdId") Long prdId) {
        return categoryFacade.unAssignProductToCategory(catId, prdId);
    }
}
