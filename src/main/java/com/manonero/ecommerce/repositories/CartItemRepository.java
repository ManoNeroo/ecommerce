package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import com.manonero.ecommerce.entities.CartItem;
import com.manonero.ecommerce.models.CartItemRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository implements ICartItemRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void updateCartItem(CartItemRequest request) {
        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("CartItem.updateCartItem");
        query.setParameter("cartId", request.getCartId());
        query.setParameter("productId", request.getProductId());
        query.setParameter("quanlity", request.getQuanlity());
        boolean checked = request.getChecked() != null ? request.getChecked() : false;
        query.setParameter("checked", checked);
        query.executeUpdate();
    }

    @Override
    public void insertCartItem(CartItemRequest request) {
        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("CartItem.insertCartItem");
        query.setParameter("cartId", request.getCartId());
        query.setParameter("productId", request.getProductId());
        query.setParameter("quanlity", request.getQuanlity());
        boolean checked = request.getChecked() != null ? request.getChecked() : false;
        query.setParameter("checked", checked);
        query.executeUpdate();
    }

    @Override
    public void deleteCartItem(CartItemRequest request) {
        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("CartItem.deleteCartItem");
        query.setParameter("cartId", request.getCartId());
        query.setParameter("productId", request.getProductId());
        query.executeUpdate();
    }

    @Override
    public CartItem selectByCartIdAndProductId(String cartId, String productId) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("CartItem.selectByCartIdAndProductId");
        query.setParameter("cartId", cartId);
        query.setParameter("productId", productId);
        @SuppressWarnings("unchecked")
        List<CartItem> cartItems = query.getResultList();
        if (cartItems.size() > 0) {
            return cartItems.get(0);
        }
        return null;
    }

}
