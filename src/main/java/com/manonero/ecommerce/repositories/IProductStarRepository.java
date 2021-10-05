package com.manonero.ecommerce.repositories;

import com.manonero.ecommerce.entities.ProductStarView;

public interface IProductStarRepository {
    ProductStarView selectById(String id);
}
