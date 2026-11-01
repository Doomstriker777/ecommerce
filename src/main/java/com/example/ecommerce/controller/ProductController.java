package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }


    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model,
                                @AuthenticationPrincipal UserDetails userDetails) {
        Product product = productService.findById(id);
        List<Review> reviews = reviewService.getReviewsByProduct(id);

        // Get related products from the same category
        List<Product> relatedProducts = productService.findByCategory(product.getCategory())
                .stream()
                .filter(p -> !p.getId().equals(id))
                .limit(4)
                .toList();

        boolean hasReviewed = false;
        if (userDetails != null) {
            try {
                hasReviewed = reviewService.hasUserReviewed(userDetails.getUsername(), id);
            } catch (Exception ignored) {
            }
        }

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("hasReviewed", hasReviewed);
        return "product-detail";
    }
}
