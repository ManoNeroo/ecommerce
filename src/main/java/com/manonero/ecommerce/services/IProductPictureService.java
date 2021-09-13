package com.manonero.ecommerce.services;

import java.util.List;

import com.manonero.ecommerce.entities.ProductPicture;
import com.manonero.ecommerce.models.ProductPictureRequest;

public interface IProductPictureService {
    List<ProductPicture> getByProductId(String productId);
    ProductPicture save(ProductPictureRequest request);
    ProductPicture delete(int pictureId);
}
