package com.example.ecommerce.controller;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ShopController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }


    @GetMapping
    public String shop(Model model,
                       @RequestParam(required = false) String sort,
                       @RequestParam(required = false) BigDecimal minPrice,
                       @RequestParam(required = false) BigDecimal maxPrice) {
        List<Product> products;

        if (minPrice != null && maxPrice != null) {
            products = productService.findByPriceRange(minPrice, maxPrice);
        } else {
            products = productService.findActive();
        }

        products = sortProducts(products, sort);

        Map<Category, Long> categoryWithCounts = categoryService.findActiveWithProductCounts();
        List<Category> categories = categoryService.findActive();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryWithCounts", categoryWithCounts);
        model.addAttribute("currentSort", sort);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("pageTitle", "All Products");
        return "shop";
    }

    @GetMapping("/category/{categoryId}")
    public String shopByCategory(@PathVariable Long categoryId, Model model,
                                 @RequestParam(required = false) String sort) {
        Category category = categoryService.findById(categoryId);
        List<Product> products = productService.findByCategory(category);
        products = sortProducts(products, sort);

        Map<Category, Long> categoryWithCounts = categoryService.findActiveWithProductCounts();
        List<Category> categories = categoryService.findActive();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryWithCounts", categoryWithCounts);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("currentSort", sort);
        model.addAttribute("pageTitle", category.getName());
        return "shop";
    }

    @GetMapping("/search")
    public String search(@RequestParam("q") String query, Model model,
                         @RequestParam(required = false) String sort) {
        List<Product> products = productService.searchByName(query);
        products = sortProducts(products, sort);

        Map<Category, Long> categoryWithCounts = categoryService.findActiveWithProductCounts();
        List<Category> categories = categoryService.findActive();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryWithCounts", categoryWithCounts);
        model.addAttribute("searchQuery", query);
        model.addAttribute("currentSort", sort);
        model.addAttribute("pageTitle", "Search: " + query);
        return "shop";
    }

    private List<Product> sortProducts(List<Product> products, String sort) {
        if (sort == null || sort.isEmpty()) return products;

        List<Product> sorted = new java.util.ArrayList<>(products);
        switch (sort) {
            case "price-asc" -> sorted.sort(Comparator.comparing(Product::getPrice));
            case "price-desc" -> sorted.sort(Comparator.comparing(Product::getPrice).reversed());
            case "name-asc" -> sorted.sort(Comparator.comparing(Product::getName));
            case "name-desc" -> sorted.sort(Comparator.comparing(Product::getName).reversed());
            case "rating" -> sorted.sort(Comparator.comparing(Product::getRating).reversed());
            case "newest" -> sorted.sort(Comparator.comparing(Product::getCreatedAt).reversed());
        }
        return sorted;
    }
}
