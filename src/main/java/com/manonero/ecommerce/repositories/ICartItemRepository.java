package com.manonero.ecommerce.repositories;

import com.manonero.ecommerce.entities.CartItem;
import com.manonero.ecommerce.models.CartItemRequest;

public interface ICartItemRepository {
    void updateCartItem(CartItemRequest request);
    void insertCartItem(CartItemRequest request);
    void deleteCartItem(CartItemRequest request);
    CartItem selectByCartIdAndProductId(String cartId, String productId);
}
