package com.example.cloudinaction.controllers;

import com.example.cloudinaction.dao.CategoryRepository;
import com.example.cloudinaction.dao.ProductRepository;
import com.example.cloudinaction.dto.CategoryDto;
import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.models.Category;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/refresh_db.sql")
public class CategoryControllerTest {
    @Value("${host}")
    private String host;
    @Value("${protocol}")
    private String protocol;
    @LocalServerPort
    private int port;
    @Autowired
    Environment environment;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testGetAllCategories() throws UnknownHostException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List<CategoryDto>> response = restTemplate.exchange(createURLWithPort("/categories"),
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
    public void testGetCategoryById() throws UnknownHostException {
        String categoryId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(createURLWithPort("/category/" + categoryId),
                HttpMethod.GET,
                entity,
                CategoryDto.class);

        CategoryDto category = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(category).isNotNull();
        assertThat(category.getId()).isEqualTo(Long.parseLong(categoryId));
    }

    @Test
    public void testGetCategoryByIdShouldReturnNotFoundIfCategoryWithGivenIdIdNotExistInDB() throws UnknownHostException {
        String categoryId = "1000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/category/" + categoryId),
                HttpMethod.GET,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteCategoryById() throws UnknownHostException {
        List<Category> initialData = categoryRepository.findAll();

        String categoryId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/category/" + categoryId),
                HttpMethod.DELETE,
                entity,
                String.class);

        List<Category> afterDeleteData = categoryRepository.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(afterDeleteData.size()).isEqualTo(initialData.size() - 1);
    }

    @Test
    public void testDeleteCategoryByIdShouldReturnNotFoundIfCategoryWithGivenIdIdNotExistInDB() throws UnknownHostException {
        String categoryId = "1000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/category/" + categoryId),
                HttpMethod.DELETE,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateCategory() throws UnknownHostException {
        List<Category> initialData = categoryRepository.findAll();

        Category category = new Category();
        category.setName("Hardtail");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Category> entity = new HttpEntity<>(category, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(createURLWithPort("/category"),
                HttpMethod.POST,
                entity,
                CategoryDto.class);

        List<Category> finalData = categoryRepository.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(finalData).isNotNull();
        assertThat(finalData.size()).isEqualTo(initialData.size() + 1);
        assertThat(finalData).contains(category);
    }

    @Test
    public void testCreateCategoryShouldReturnBadRequestIfCategoryNameIsNotSet() throws UnknownHostException {
        List<Category> initialData = categoryRepository.findAll();

        Category category = new Category();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Category> entity = new HttpEntity<>(category, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/category"),
                HttpMethod.POST,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testUpdateCategory() throws UnknownHostException {
        String categoryId = "1";

        CategoryDto category = new CategoryDto();
        category.setName("BMX");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(category, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(createURLWithPort("/category/" + categoryId),
                HttpMethod.PUT,
                entity,
                CategoryDto.class);

        CategoryDto updatedCategory = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory).isEqualTo(category);
    }

    @Test
    public void testCategoryProducts() throws UnknownHostException {
        String categoryId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(createURLWithPort("/category/" + categoryId + "/products"),
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
    public void testAssignNewProductToCategory() throws UnknownHostException {
        String categoryId = "1";
        String productId = "3";

        Optional<Category> initialCategoryState = categoryRepository.findById(Long.parseLong(categoryId));
        boolean isInitialCategoryContainsProduct = initialCategoryState.map(Category::getProducts)
                .orElseThrow(IllegalArgumentException::new)
                .stream()
                .anyMatch(product -> product.getId().equals(Long.parseLong(productId)));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(createURLWithPort(String.format("/category/%s/product/%s/assign", categoryId, productId)),
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
        assertThat(isInitialCategoryContainsProduct).isFalse();
        assertThat(isUpdatedCategoryContainsProduct).isTrue();
    }

    @Test
    public void testAssignNewProductToCategoryShouldReturnNotFoundIfCategoryWithGivenIdIdNotExistInDB() throws UnknownHostException {
        String categoryId = "1000";
        String productId = "3";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(String.format("/category/%s/product/%s/assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testAssignNewProductToCategoryShouldReturnNotFoundIfProductWithGivenIdIdNotExistInDB() throws UnknownHostException {
        String categoryId = "1";
        String productId = "3000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(String.format("/category/%s/product/%s/assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUnAssignProductFromCategory() throws UnknownHostException {
        String categoryId = "1";
        String productId = "1";

        Optional<Category> initialCategoryState = categoryRepository.findById(Long.parseLong(categoryId));
        boolean isInitialCategoryContainsProduct = initialCategoryState.map(Category::getProducts)
                .orElseThrow(IllegalArgumentException::new)
                .stream()
                .anyMatch(product -> product.getId().equals(Long.parseLong(productId)));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(createURLWithPort(String.format("/category/%s/product/%s/un-assign", categoryId, productId)),
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
    public void testUnAssignProductFromCategoryShouldReturnNotFoundIfCategoryWithGivenIdIdNotExistInDB() throws UnknownHostException {
        String categoryId = "1000";
        String productId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(String.format("/category/%s/product/%s/un-assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUnAssignProductFromCategoryShouldReturnNotFoundIfProductWithGivenIdIdNotExistInDB() throws UnknownHostException {
        String categoryId = "1";
        String productId = "1000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CategoryDto> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(String.format("/category/%s/product/%s/un-assign", categoryId, productId)),
                HttpMethod.PUT,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String createURLWithPort(String uri) throws UnknownHostException {
        return protocol + InetAddress.getLoopbackAddress().getHostName() + ":" + port + uri;
    }
}
