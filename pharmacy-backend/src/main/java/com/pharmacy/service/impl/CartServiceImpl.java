package com.pharmacy.service.impl;

import com.pharmacy.dto.AddToCartRequest;
import com.pharmacy.entity.*;
import com.pharmacy.repository.CartItemRepository;
import com.pharmacy.repository.CartRepository;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.UserRepository;
import com.pharmacy.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    @Override
    public Cart getCart() {

        User user = getCurrentUser();

        return cartRepository.findByUser(user)
                .orElseGet(() -> createCart(user));
    }

    @Override
    public Cart addToCart(AddToCartRequest request) {

        User user = getCurrentUser();

        // Get or create the cart for the user
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createCart(user));

        // Find the medicine
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        // Check if enough stock exists
        if (medicine.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }

        // --- NEW LOGIC START ---
        // Look for existing item in the cart
        CartItem item = cartItemRepository
                .findByCartAndMedicine(cart, medicine)
                .orElse(null);

        if (item != null) {
            // If it exists, update the quantity
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            // If it doesn't exist, create a new one
            item = CartItem.builder()
                    .cart(cart)
                    .medicine(medicine)
                    .quantity(request.getQuantity())
                    .price(medicine.getPrice())
                    .build();

            // Add new item to the cart's list (for immediate response consistency)
            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
            }
            cart.getItems().add(item);
        }
        // --- NEW LOGIC END ---

        cartItemRepository.save(item);

        return cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(Long itemId) {

        cartItemRepository.deleteById(itemId);
    }

    private Cart createCart(User user) {

        Cart cart = Cart.builder()
                .user(user)
                .items(new ArrayList<>())
                .build();

        return cartRepository.save(cart);
    }

    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}