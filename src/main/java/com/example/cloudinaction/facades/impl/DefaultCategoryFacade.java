package com.example.cloudinaction.facades.impl;

import com.example.cloudinaction.dto.CategoryDto;
import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.facades.CategoryFacade;
import com.example.cloudinaction.models.Category;
import com.example.cloudinaction.services.CategoryService;
import com.example.cloudinaction.util.CategoryUtil;
import com.example.cloudinaction.util.ProductUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultCategoryFacade implements CategoryFacade {

    @Autowired
    @Qualifier("mvcConversionService")
    private ConversionService conversionService;
    @Autowired
    private CategoryService categoryService;


    @Override
    public CategoryDto createCategory(CategoryDto category) {
        Category categoryModel = categoryService.createCategory(conversionService.convert(category, Category.class));

        return conversionService.convert(categoryModel, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryService.deleteCategory(id);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryService.getCategoryById(id);

        return conversionService.convert(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto category) {
        Category categoryModel = categoryService.updateCategory(id, conversionService.convert(category, Category.class));

        return conversionService.convert(categoryModel, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories(Pageable pageData) {
        return CategoryUtil.convertCategoryListModelToListDto(categoryService.getAllCategories(pageData));
    }

    @Override
    public List<ProductDto> getProductsOfCategory(Long id) {
        return ProductUtil.convertListModelToListDto(categoryService.getProductsOfCategory(id));
    }

    @Override
    public CategoryDto assignProductToCategory(Long catId, Long prdId) {
        Category category = categoryService.assignProductToCategory(catId, prdId);

        return conversionService.convert(category, CategoryDto.class);
    }

    @Override
    public CategoryDto unAssignProductToCategory(Long catId, Long prdId) {
        Category category = categoryService.unAssignProductToCategory(catId, prdId);

        return conversionService.convert(category, CategoryDto.class);
    }
}
