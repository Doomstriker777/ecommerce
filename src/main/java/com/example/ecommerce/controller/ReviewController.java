package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ReviewRequest;
import com.example.ecommerce.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PostMapping("/product/{productId}/review")
    public String addReview(@PathVariable Long productId,
                            @Valid @ModelAttribute ReviewRequest reviewRequest,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        try {
            reviewService.addReview(
                    userDetails.getUsername(),
                    productId,
                    reviewRequest.getRating(),
                    reviewRequest.getComment()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Review submitted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/product/" + productId;
    }
}
