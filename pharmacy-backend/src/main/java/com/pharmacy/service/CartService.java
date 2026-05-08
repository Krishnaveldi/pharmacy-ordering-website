package com.pharmacy.service;

import com.pharmacy.dto.AddToCartRequest;
import com.pharmacy.entity.Cart;

public interface CartService {

    Cart getCart();

    Cart addToCart(AddToCartRequest request);

    void removeCartItem(Long itemId);
}