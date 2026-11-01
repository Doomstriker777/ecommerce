package com.example.ecommerce.controller;

import com.example.ecommerce.model.Wishlist;
import com.example.ecommerce.service.WishlistService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }


    @GetMapping
    public String viewWishlist(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Wishlist> wishlistItems = wishlistService.getUserWishlist(userDetails.getUsername());
        model.addAttribute("wishlistItems", wishlistItems);
        return "wishlist";
    }

    @PostMapping("/toggle/{productId}")
    public String toggleWishlist(@PathVariable Long productId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam(required = false) String redirectUrl,
                                 RedirectAttributes redirectAttributes) {
        boolean added = wishlistService.toggleWishlist(userDetails.getUsername(), productId);
        if (added) {
            redirectAttributes.addFlashAttribute("successMessage", "Added to wishlist!");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Removed from wishlist.");
        }

        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            return "redirect:" + redirectUrl;
        }
        return "redirect:/wishlist";
    }

    @GetMapping("/check/{productId}")
    @ResponseBody
    public Map<String, Boolean> checkWishlist(@PathVariable Long productId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Boolean> response = new HashMap<>();
        if (userDetails != null) {
            response.put("inWishlist", wishlistService.isInWishlist(userDetails.getUsername(), productId));
        } else {
            response.put("inWishlist", false);
        }
        return response;
    }
}
