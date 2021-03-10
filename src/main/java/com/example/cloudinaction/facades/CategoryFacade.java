package com.example.cloudinaction.facades;

import com.example.cloudinaction.dto.CategoryDto;
import com.example.cloudinaction.dto.ProductDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryFacade {
    CategoryDto createCategory(CategoryDto category);
    void deleteCategory(Long id);
    CategoryDto getCategoryById(Long id);
    CategoryDto updateCategory(Long id, CategoryDto category);
    List<CategoryDto> getAllCategories(Pageable pageData);
    List<ProductDto> getProductsOfCategory(Long id);
    CategoryDto assignProductToCategory(Long catId, Long prdId);
    CategoryDto unAssignProductToCategory(Long catId, Long prdId);
}
