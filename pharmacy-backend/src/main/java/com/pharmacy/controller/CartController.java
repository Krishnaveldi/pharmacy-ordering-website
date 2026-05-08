package com.pharmacy.controller;

import com.pharmacy.dto.AddToCartRequest;
import com.pharmacy.entity.Cart;
import com.pharmacy.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@Valid
            @RequestBody AddToCartRequest request
    ) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Long itemId
    ) {

        cartService.removeCartItem(itemId);

        return ResponseEntity.ok("Item removed successfully");
    }
}