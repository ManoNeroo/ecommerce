package com.manonero.ecommerce.services;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.CartItem;
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
        CartItem cartItem = cartItemRepository.selectByCartIdAndProductId(request.getCartId(), request.getProductId());
        if(request.getQuanlity() < 1) {
            request.setQuanlity(1);
        }
        if (cartItem == null) {
            cartItemRepository.insertCartItem(request);
        } else {
            if (cartItem.getQuanlity() < 5) {
                request.setQuanlity(cartItem.getQuanlity() + 1);
            } else {
                request.setQuanlity(cartItem.getQuanlity());
            }
            if (!request.getChecked()) {
                request.setChecked(cartItem.getChecked());
            }
            cartItemRepository.updateCartItem(request);
        }
    }

    @Override
    @Transactional
    public void editCartItem(CartItemRequest request) {
        if(request.getQuanlity() < 1) {
            request.setQuanlity(1);
        }
        cartItemRepository.updateCartItem(request);
    }

    @Override
    @Transactional
    public void deleteCartItem(CartItemRequest request) {
        cartItemRepository.deleteCartItem(request);
    }

}
