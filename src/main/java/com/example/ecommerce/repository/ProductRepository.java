package com.example.ecommerce.repository;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    List<Product> findByCategoryAndActiveTrue(Category category);

    List<Product> findByFeaturedTrueAndActiveTrue();

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    List<Product> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    long countByCategoryAndActiveTrue(Category category);

    List<Product> findTop8ByActiveTrueOrderByCreatedAtDesc();
}
