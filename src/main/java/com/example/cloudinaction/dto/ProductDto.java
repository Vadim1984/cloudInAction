package com.example.cloudinaction.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class ProductDto {
    private Long id;
    @NotNull(message = "{validation.product.name.dto}")
    private String name;
    @NotNull(message = "{validation.product.price.dto}")
    private BigDecimal price;

    private List<Long> categories;

    public ProductDto() {
    }

    public ProductDto(Long id, @NotNull(message = "name is mandatory") String name, @NotNull(message = "price is mandatory") BigDecimal price, List<Long> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
