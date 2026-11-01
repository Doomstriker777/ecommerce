package com.example.ecommerce.controller;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;

    public OrderController(OrderService orderService, CartService cartService, UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
    }


    @GetMapping
    public String listOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Order> orders = orderService.getUserOrders(userDetails.getUsername());
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        Order order = orderService.getOrderById(id);

        // Ensure user can only see their own orders (unless admin)
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!order.getUser().getUsername().equals(userDetails.getUsername()) && !isAdmin) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "order-detail";
    }

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        List<CartItem> cartItems = cartService.getCartItems(username);
        BigDecimal cartTotal = cartService.getCartTotal(username);
        User user = userService.findByUsername(username);

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("user", user);
        return "checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam String shippingAddress,
                             @RequestParam String shippingCity,
                             @RequestParam String shippingState,
                             @RequestParam String shippingZip,
                             @RequestParam String shippingPhone,
                             @RequestParam(required = false) String notes,
                             RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.createOrderFromCart(
                    userDetails.getUsername(),
                    shippingAddress, shippingCity, shippingState, shippingZip, shippingPhone, notes
            );
            redirectAttributes.addFlashAttribute("successMessage",
                    "Order #" + order.getId() + " placed successfully!");
            return "redirect:/orders/" + order.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/orders/checkout";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", "Order cancelled successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }
}
