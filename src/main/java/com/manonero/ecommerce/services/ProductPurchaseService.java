package com.manonero.ecommerce.services;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.ProductPurchased;
import com.manonero.ecommerce.repositories.IProductPurchasedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPurchaseService implements IProductPurchaseService {

    @Autowired
    private IProductPurchasedRepository purchasedRepository;
    
    @Override
    @Transactional
    public int checkUserEvaluated(String productId, int userId) {
        ProductPurchased productPurchased = purchasedRepository.selectByPK(productId, userId);
        if(productPurchased != null) {
            if(productPurchased.isHasEvaluated()) {
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }

    @Override
    @Transactional
    public ProductPurchased save(ProductPurchased productPurchased) {
        return purchasedRepository.save(productPurchased);
    }
    
}
