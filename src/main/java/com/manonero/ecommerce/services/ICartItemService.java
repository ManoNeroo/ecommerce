package com.manonero.ecommerce.services;

import com.manonero.ecommerce.models.CartItemRequest;

public interface ICartItemService {
    void addCartItem(CartItemRequest request);
    void editCartItem(CartItemRequest request);
    void deleteCartItem(CartItemRequest request);
}
