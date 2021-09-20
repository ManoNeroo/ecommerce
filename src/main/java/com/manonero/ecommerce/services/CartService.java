package com.manonero.ecommerce.services;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.Cart;
import com.manonero.ecommerce.repositories.ICartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Override
    @Transactional
    public int getNumberItem(String cartId) {
        return cartRepository.selectNumberItem(cartId);
    }

    @Override
    @Transactional
    public Cart getById(String cartId) {
        return cartRepository.selectById(cartId);
    }
    
}
