package com.example.cloudinaction.dto;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class CategoryDto {
    private Long id;
    @NotNull(message = "{validation.category.name.dto}")
    private String name;
    private List<ProductDto> products;

    public CategoryDto() {
    }

    public CategoryDto(Long id, @NotNull String name, List<ProductDto> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDto that = (CategoryDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
