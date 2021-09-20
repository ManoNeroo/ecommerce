package com.manonero.ecommerce.repositories;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import com.manonero.ecommerce.entities.Cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository implements ICartRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public int selectNumberItem(String cartId) {
        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Cart.selectNumberCartItem");
        query.setParameter("cartId", cartId);
        query.execute();
        Integer numberItem = (Integer) query.getOutputParameterValue("numberItem");
        if (numberItem == null) {
            return 0;
        }
        return numberItem;
    }

    @Override
    public Cart selectById(String cartId) {
        Cart cart = entityManager.find(Cart.class, cartId);
        return cart;
    }

}
