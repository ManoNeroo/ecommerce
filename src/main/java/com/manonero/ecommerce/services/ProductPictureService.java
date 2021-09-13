package com.manonero.ecommerce.services;

import java.util.List;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.Product;
import com.manonero.ecommerce.entities.ProductPicture;
import com.manonero.ecommerce.models.ProductPictureRequest;
import com.manonero.ecommerce.repositories.IProductPictureRepository;
import com.manonero.ecommerce.repositories.IProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPictureService implements IProductPictureService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IProductPictureRepository pictureRepository;

    @Override
    @Transactional
    public List<ProductPicture> getByProductId(String productId) {
         return pictureRepository.selectByProductId(productId);
    }

    @Override
    @Transactional
    public ProductPicture save(ProductPictureRequest request) {
        ProductPicture picture = new ProductPicture();
        picture.setId(request.getId());
        picture.setName(request.getName());
        Product product = productRepository.selectProductById(request.getProductId());
        picture.setProduct(product);
        return pictureRepository.save(picture);
    }

    @Override
    @Transactional
    public ProductPicture delete(int pictureId) {
        return pictureRepository.delete(pictureId);
    }
    
}
