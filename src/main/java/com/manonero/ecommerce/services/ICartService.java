package com.manonero.ecommerce.services;

import com.manonero.ecommerce.entities.Cart;

public interface ICartService {
    int getNumberItem(String cartId);
    Cart getById(String cartId);
}
