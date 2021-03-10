package com.example.cloudinaction.dao;

import com.example.cloudinaction.models.Product;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    List<Product> findByNameContains(String nameSubString, Pageable pageable);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);
}
