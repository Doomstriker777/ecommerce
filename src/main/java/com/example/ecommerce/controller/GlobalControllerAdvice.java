package com.example.ecommerce.controller;

import com.example.ecommerce.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final CartService cartService;

    public GlobalControllerAdvice(CartService cartService) {
        this.cartService = cartService;
    }


    @ModelAttribute("cartItemCount")
    public int cartItemCount(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            try {
                return cartService.getCartCount(userDetails.getUsername());
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
}
