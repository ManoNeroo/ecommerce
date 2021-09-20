package com.manonero.ecommerce.services;

import javax.transaction.Transactional;

import com.manonero.ecommerce.models.CartItemRequest;
import com.manonero.ecommerce.repositories.ICartItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService implements ICartItemService {

    @Autowired
    private ICartItemRepository cartItemRepository;
    @Override
    @Transactional
    public void addCartItem(CartItemRequest request) {
       cartItemRepository.insertCartItem(request);
    }

    @Override
    @Transactional
    public void editCartItem(CartItemRequest request) {
        cartItemRepository.updateCartItem(request);
    }

    @Override
    @Transactional
    public void deleteCartItem(CartItemRequest request) {
        cartItemRepository.deleteCartItem(request);
    }
    
}
