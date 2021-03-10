package com.example.cloudinaction.services;

import com.example.cloudinaction.models.Category;
import com.example.cloudinaction.models.Product;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Category createCategory(Category category);
    void deleteCategory(Long id);
    Category getCategoryById(Long id);
    Category updateCategory(Long id, Category category);
    List<Category> getAllCategories(Pageable pageData);
    List<Product> getProductsOfCategory(Long id);
    Category assignProductToCategory(Long catId, Long prdId);
    Category unAssignProductToCategory(Long catId, Long prdId);
}
