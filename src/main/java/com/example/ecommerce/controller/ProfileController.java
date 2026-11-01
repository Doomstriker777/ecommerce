package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProfileUpdateRequest;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final OrderService orderService;
    private final WishlistService wishlistService;

    public ProfileController(UserService userService, OrderService orderService, WishlistService wishlistService) {
        this.userService = userService;
        this.orderService = orderService;
        this.wishlistService = wishlistService;
    }


    @GetMapping
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<Order> recentOrders = orderService.getUserOrders(userDetails.getUsername());
        long wishlistCount = wishlistService.getWishlistCount(userDetails.getUsername());

        ProfileUpdateRequest profileForm = ProfileUpdateRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .zipCode(user.getZipCode())
                .build();

        model.addAttribute("user", user);
        model.addAttribute("profileForm", profileForm);
        model.addAttribute("recentOrders", recentOrders.stream().limit(5).toList());
        model.addAttribute("orderCount", recentOrders.size());
        model.addAttribute("wishlistCount", wishlistCount);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("profileForm") ProfileUpdateRequest request,
                                BindingResult result,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "profile";
        }

        try {
            userService.updateProfile(userDetails.getUsername(), request);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update profile: " + e.getMessage());
        }
        return "redirect:/profile";
    }
}
