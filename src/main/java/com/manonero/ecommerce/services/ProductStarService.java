package com.manonero.ecommerce.services;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.ProductStarView;
import com.manonero.ecommerce.repositories.IProductStarRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductStarService implements IProductStarService {

    @Autowired
    private IProductStarRepository starRepository;

    @Override
    @Transactional
    public ProductStarView getById(String id) {
        return starRepository.selectById(id);
    }
    
}
