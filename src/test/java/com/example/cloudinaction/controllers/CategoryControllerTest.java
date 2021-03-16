package com.example.cloudinaction.controllers;

import com.example.cloudinaction.dto.CategoryDto;
import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.facades.CategoryFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.cloudinaction.TestUtils.createUrl;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/refresh_db.sql")
public class CategoryControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CategoryFacade categoryFacade;
    @Autowired
    @Qualifier("mvcConversionService")
    private ConversionService conversionService;

    @Test
    public void testGetAllCategories() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List<CategoryDto>> response = restTemplate.exchange(createUrl(port, "/categories"),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<CategoryDto>>() {
                });

        List<CategoryDto> categories = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(categories).isNotNull();
        assertThat(categories).isNotEmpty();
    }

    @Test
    public void testGetCategoryById() {
        String categoryId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(createUrl(port, "/category/" + categoryId),
                HttpMethod.GET,
                entity,
                CategoryDto.class);

        CategoryDto category = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(category).isNotNull();
        assertThat(category.getId()).isEqualTo(Long.parseLong(categoryId));
    }

    @Test
    public void testGetCategoryByIdShouldReturnNotFoundIfCategoryWithGivenIdIdNotExistInDB() {
        String categoryId = "1000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, "/category/" + categoryId),
                HttpMethod.GET,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteCategoryById() {
        List<CategoryDto> initialData = categoryFacade.getAllCategories(PageRequest.of(0, 10));

        String categoryId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, "/category/" + categoryId),
                HttpMethod.DELETE,
                entity,
                String.class);

        List<CategoryDto> afterDeleteData = categoryFacade.getAllCategories(PageRequest.of(0, 10));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(afterDeleteData.size()).isEqualTo(initialData.size() - 1);
    }

    @Test
    public void testDeleteCategoryByIdShouldReturnNotFoundIfCategoryWithGivenIdIdNotExistInDB() {
        String categoryId = "1000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, "/category/" + categoryId),
                HttpMethod.DELETE,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateCategory() {
        List<CategoryDto> initialData = categoryFacade.getAllCategories(PageRequest.of(0, 10));

        CategoryDto category = new CategoryDto();
        category.setName("Hardtail");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(category, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(createUrl(port, "/category"),
                HttpMethod.POST,
                entity,
                CategoryDto.class);

        List<CategoryDto> finalData = categoryFacade.getAllCategories(PageRequest.of(0, 10));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(finalData).isNotNull();
        assertThat(finalData.size()).isEqualTo(initialData.size() + 1);
        assertThat(finalData).contains(category);
    }

    @Test
    public void testCreateCategoryShouldReturnBadRequestIfCategoryNameIsNotSet() {
        CategoryDto category = new CategoryDto();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(category, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, "/category"),
                HttpMethod.POST,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testUpdateCategory() {
        String categoryId = "1";

        CategoryDto category = new CategoryDto();
        category.setName("BMX");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(category, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(createUrl(port, "/category/" + categoryId),
                HttpMethod.PUT,
                entity,
                CategoryDto.class);

        CategoryDto updatedCategory = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory).isEqualTo(category);
    }

    @Test
    public void testCategoryProducts() {
        String categoryId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(createUrl(port, "/category/" + categoryId + "/products"),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ProductDto>>() {
                });

        List<ProductDto> products = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(products).isNotNull();
        assertThat(products).isNotEmpty();
    }

    @Test
    public void testAssignNewProductToCategory() {
        String categoryId = "1";
        String productId = "3";

        CategoryDto initialCategoryState = categoryFacade.getCategoryById(Long.parseLong(categoryId));

        boolean isInitialCategoryContainsProduct = Optional.ofNullable(initialCategoryState.getProducts())
                .orElseGet(ArrayList::new)
                .stream()
                .map(product -> conversionService.convert(product, ProductDto.class))
                .anyMatch(productDto -> Long.valueOf(productId).equals(productDto.getId()));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(createUrl(port, String.format("/category/%s/product/%s/assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                CategoryDto.class);

        CategoryDto updatedCategory = response.getBody();

        boolean isUpdatedCategoryContainsProduct = Optional.ofNullable(updatedCategory.getProducts())
                .orElseGet(ArrayList::new)
                .stream()
                .anyMatch(productDto -> Long.valueOf(productId).equals(productDto.getId()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getProducts()).isNotNull();
        assertThat(updatedCategory.getProducts()).isNotEmpty();
        assertThat(isInitialCategoryContainsProduct).isFalse();
        assertThat(isUpdatedCategoryContainsProduct).isTrue();
    }

    @Test
    public void testAssignNewProductToCategoryShouldReturnNotFoundIfCategoryWithGivenIdIdNotExistInDB() {
        String categoryId = "1000";
        String productId = "3";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, String.format("/category/%s/product/%s/assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testAssignNewProductToCategoryShouldReturnNotFoundIfProductWithGivenIdIdNotExistInDB() {
        String categoryId = "1";
        String productId = "3000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, String.format("/category/%s/product/%s/assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testAssignNewProductToCategoryShouldReturnBadRequestIfProductAlreadyAssignedToThisCategory() {
        String categoryId = "1";
        String productId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, String.format("/category/%s/product/%s/assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testUnAssignProductFromCategory() {
        String categoryId = "1";
        String productId = "1";

        CategoryDto initialCategoryState = categoryFacade.getCategoryById(Long.parseLong(categoryId));

        boolean isInitialCategoryContainsProduct = Optional.ofNullable(initialCategoryState.getProducts())
                .orElseGet(ArrayList::new)
                .stream()
                .map(product -> conversionService.convert(product, ProductDto.class))
                .anyMatch(productDto -> Long.valueOf(productId).equals(productDto.getId()));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(createUrl(port, String.format("/category/%s/product/%s/un-assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                CategoryDto.class);

        CategoryDto updatedCategory = response.getBody();

        boolean isUpdatedCategoryContainsProduct = Optional.ofNullable(updatedCategory.getProducts())
                .orElseGet(ArrayList::new)
                .stream()
                .anyMatch(product -> product.getId().equals(Long.parseLong(productId)));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getProducts()).isNotNull();
        assertThat(updatedCategory.getProducts()).isNotEmpty();
        assertThat(isInitialCategoryContainsProduct).isTrue();
        assertThat(isUpdatedCategoryContainsProduct).isFalse();
    }

    @Test
    public void testUnAssignProductFromCategoryShouldReturnNotFoundIfCategoryWithGivenIdIdNotExistInDB() {
        String categoryId = "1000";
        String productId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, String.format("/category/%s/product/%s/un-assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUnAssignProductFromCategoryShouldReturnNotFoundIfProductWithGivenIdIdNotExistInDB() {
        String categoryId = "1";
        String productId = "1000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, String.format("/category/%s/product/%s/un-assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
