package com.example.cloudinaction.facades.impl;

import com.example.cloudinaction.dto.CategoryDto;
import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.facades.CategoryFacade;
import com.example.cloudinaction.models.Category;
import com.example.cloudinaction.models.Product;
import com.example.cloudinaction.services.CategoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
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
        return convertCategoryListModelToListDto(categoryService.getAllCategories(pageData));
    }

    @Override
    public List<ProductDto> getProductsOfCategory(Long id) {
        return convertProductListModelToListDto(categoryService.getProductsOfCategory(id));
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


    private List<CategoryDto> convertCategoryListModelToListDto(List<Category> categories){
        return Optional.ofNullable(categories)
                .orElseGet(ArrayList::new)
                .stream()
                .map(category -> conversionService.convert(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    private List<ProductDto> convertProductListModelToListDto(List<Product> products){
        return Optional.ofNullable(products)
                .orElseGet(ArrayList::new)
                .stream()
                .map(product -> conversionService.convert(product, ProductDto.class))
                .collect(Collectors.toList());
    }
}
