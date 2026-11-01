package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }


    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findActive() {
        return categoryRepository.findByActiveTrue();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    public Map<Category, Long> findActiveWithProductCounts() {
        Map<Category, Long> categoryCountMap = new LinkedHashMap<>();
        List<Category> categories = categoryRepository.findByActiveTrue();
        for (Category category : categories) {
            long count = productRepository.countByCategoryAndActiveTrue(category);
            categoryCountMap.put(category, count);
        }
        return categoryCountMap;
    }

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public long count() {
        return categoryRepository.count();
    }
}
