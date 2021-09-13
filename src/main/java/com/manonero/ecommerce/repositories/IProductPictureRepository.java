package com.manonero.ecommerce.repositories;

import java.util.List;

import com.manonero.ecommerce.entities.ProductPicture;

public interface IProductPictureRepository {
    List<ProductPicture> selectByProductId(String productId);
    ProductPicture save(ProductPicture productPicture);
    ProductPicture delete(int pictureId);
}
