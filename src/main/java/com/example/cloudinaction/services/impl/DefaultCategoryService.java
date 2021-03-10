package com.example.cloudinaction.services.impl;

import com.example.cloudinaction.dao.CategoryRepository;
import com.example.cloudinaction.dao.ProductRepository;
import com.example.cloudinaction.models.Category;
import com.example.cloudinaction.models.Product;
import com.example.cloudinaction.services.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.example.cloudinaction.services.impl.DefaultProductService.PRODUCT_NOT_FOUND_MESSAGE;

@Service
public class DefaultCategoryService implements CategoryService {

    private static final String CATEGORY_NOT_FOUND_MESSAGE = "category with %s [%s] not found";
    private static final String CATEGORY_IS_NOT_CONTAINS_PRODUCT = "Category id: [%s] not contains product id: [%s]";
    private static final String ID_FIELD = "id";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(CATEGORY_NOT_FOUND_MESSAGE, ID_FIELD, id)));

        category.getProducts().clear();

        categoryRepository.delete(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(CATEGORY_NOT_FOUND_MESSAGE, ID_FIELD, id)));
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category targetCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(CATEGORY_NOT_FOUND_MESSAGE, ID_FIELD, id)));

        targetCategory.setName(category.getName());
        targetCategory.setProducts(category.getProducts());

        return categoryRepository.save(targetCategory);
    }

    @Override
    public List<Category> getAllCategories(Pageable pageData) {
        return categoryRepository.findAll(pageData).getContent();
    }

    @Override
    public List<Product> getProductsOfCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(CATEGORY_NOT_FOUND_MESSAGE, ID_FIELD, id)));

        return category.getProducts();
    }

    @Override
    public Category assignProductToCategory(Long catId, Long prdId) {
        Product targetProduct = productRepository.findById(prdId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(PRODUCT_NOT_FOUND_MESSAGE, ID_FIELD, prdId)));

        Category targetCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(CATEGORY_NOT_FOUND_MESSAGE, ID_FIELD, catId)));

        targetProduct.getCategories().add(targetCategory);
        targetCategory.getProducts().add(targetProduct);

        return categoryRepository.save(targetCategory);
    }

    @Override
    public Category unAssignProductToCategory(Long catId, Long prdId) {
        Product targetProduct = productRepository.findById(prdId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(PRODUCT_NOT_FOUND_MESSAGE, ID_FIELD, prdId)));

        Category targetCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(CATEGORY_NOT_FOUND_MESSAGE, ID_FIELD, catId)));

        boolean contains = targetCategory.getProducts().contains(targetProduct);

        if (!contains) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(CATEGORY_IS_NOT_CONTAINS_PRODUCT, catId, prdId));
        }

        targetProduct.getCategories().remove(targetCategory);
        targetCategory.getProducts().remove(targetProduct);

        return categoryRepository.save(targetCategory);
    }
}
