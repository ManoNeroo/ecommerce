package com.manonero.ecommerce.repositories;

import com.manonero.ecommerce.entities.Cart;

public interface ICartRepository {
    int selectNumberItem(String cartId);
    Cart selectById(String cartId);
}
