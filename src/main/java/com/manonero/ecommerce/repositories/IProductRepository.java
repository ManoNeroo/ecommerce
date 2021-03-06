package com.manonero.ecommerce.repositories;

import java.util.List;

import com.manonero.ecommerce.entities.Product;

public interface IProductRepository {
    List<Product> selectFilterProduct(int[] categoryIds, int[] brandIds,
    int[] priceRangeIxs, String name, Boolean status, Boolean isSort, Boolean onlyEnable);
    Product save(Product product);
    Product selectProductById(String id);
    void updateProductStatus(Boolean status, String productId);
    void updateProductAvatar(String avatar, String productId);
    List<Product> selectTopProduct(int top, int categoryId);
    List<Product> selectTopSale(int top);
    List<Product> selectTopByName(int top, String name, Boolean status);
}
