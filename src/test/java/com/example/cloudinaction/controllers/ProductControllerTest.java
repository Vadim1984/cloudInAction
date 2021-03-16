package com.example.cloudinaction.controllers;

import com.example.cloudinaction.dao.ProductRepository;
import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.models.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
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
public class ProductControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testGetAllProducts() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(createUrl(port, "/products"),
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
    public void testShouldReturnProductsWhichNameContainsInput() {
        String input = "boa";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(createUrl(port, String.format("/products/name?nameSubString=%s", input)),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ProductDto>>() {
                });

        List<ProductDto> products = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(products).isNotNull();
        assertThat(products).allMatch(product -> product.getName().contains(input));
    }

    @Test
    public void testShouldReturn404StatusCodeWhenProductsWithInputNameAreNotExistInDB() {
        String input = "not_found";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, String.format("/products/name?nameSubString=%s", input)),
                HttpMethod.GET,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetProductById() {
        String productId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ProductDto> response = restTemplate.exchange(createUrl(port, "/product/" + productId),
                HttpMethod.GET,
                entity,
                ProductDto.class);

        ProductDto product = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(Long.parseLong(productId));
    }

    @Test
    public void testShouldReturn404StatusCodeForGetProductIfProductNotExistForInputId() {
        String productId = "1000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, "/product/" + productId),
                HttpMethod.GET,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteProductById() {
        List<Product> initialData = productRepository.findAll();

        String productId = "1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, "/product/" + productId),
                HttpMethod.DELETE,
                entity,
                String.class);

        List<Product> afterDeleteData = productRepository.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(afterDeleteData.size()).isEqualTo(initialData.size() - 1);
    }

    @Test
    public void testShouldReturn404StatusCodeForDeleteProductIfProductNotExistForInputId() {
        String productId = "1000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, "/product/" + productId),
                HttpMethod.DELETE,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetProductsWhichPriceIsInRange() {
        String priceMin = "500";
        String priceMax = "1200";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(createUrl(port, String.format("/products/price?pricemin=%s&pricemax=%s", priceMin, priceMax)),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ProductDto>>() {
                });

        List<ProductDto> products = response.getBody();

        Predicate<ProductDto> isProductPriceInRange = product -> BigDecimal.valueOf(Long.parseLong(priceMin)).compareTo(product.getPrice()) < 0 &&
                BigDecimal.valueOf(Long.parseLong(priceMax)).compareTo(product.getPrice()) > 0;

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(products).allMatch(isProductPriceInRange);
    }

    @Test
    public void testShouldReturn404StatusCodeForGetProductsWhichPriceIsInRangeIfProductsForRangeNotFound() {
        String priceMin = "500000";
        String priceMax = "1200000";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, String.format("/products/price?pricemin=%s&pricemax=%s", priceMin, priceMax)),
                HttpMethod.GET,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateProduct() {
        List<Product> initialData = productRepository.findAll();

        ProductDto product = new ProductDto();
        product.setName("Santa Cruz Bullit");
        product.setPrice(BigDecimal.valueOf(749900, 2));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ProductDto> entity = new HttpEntity<>(product, headers);

        ResponseEntity<ProductDto> response = restTemplate.exchange(createUrl(port, "/product"),
                HttpMethod.POST,
                entity,
                ProductDto.class);

        List<Product> finalData = productRepository.findAll();
        ProductDto createdProduct = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(finalData).isNotNull();
        assertThat(finalData.size()).isEqualTo(initialData.size() + 1);
        assertThat(createdProduct).isEqualTo(product);
    }

    @Test
    public void testCreateProductWithoutMandatoryFieldNameShouldReturnBadRequest() {
        ProductDto product = new ProductDto();
        product.setPrice(BigDecimal.valueOf(749900, 2));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ProductDto> entity = new HttpEntity<>(product, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, "/product"),
                HttpMethod.POST,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testCreateProductWithoutMandatoryFieldPriceShouldReturnBadRequest() {
        ProductDto product = new ProductDto();
        product.setName("Santa Cruz Bullit");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ProductDto> entity = new HttpEntity<>(product, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, "/product"),
                HttpMethod.POST,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void testUpdateProduct() {
        String productId = "1";

        ProductDto product = new ProductDto();
        product.setName("Santa Cruz Bullit");
        product.setPrice(BigDecimal.valueOf(749900, 2));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ProductDto> entity = new HttpEntity<>(product, headers);

        ResponseEntity<ProductDto> response = restTemplate.exchange(createUrl(port, "/product/" + productId),
                HttpMethod.PUT,
                entity,
                ProductDto.class);

        ProductDto updatedProduct = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct).isEqualTo(product);
    }

    @Test
    public void testShouldReturn404StatusCodeForUpdateProductIfProductForIdIsNotFound() {
        String productId = "10000";

        ProductDto product = new ProductDto();
        product.setName("Santa Cruz Bullit");
        product.setPrice(BigDecimal.valueOf(749900, 2));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ProductDto> entity = new HttpEntity<>(product, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrl(port, "/product/" + productId),
                HttpMethod.PUT,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
