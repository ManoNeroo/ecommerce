package com.manonero.ecommerce.services;

import java.util.List;

import com.manonero.ecommerce.entities.Product;
import com.manonero.ecommerce.models.ProductRequest;
import com.manonero.ecommerce.models.UpdateProductStatusRequest;

public interface IProductService {
    List<Product> filterProduct(Integer offset, Integer limit, int[] categoryIds, int[] brandIds,
    int[] priceRangeIxs, String name, Boolean status, Boolean isSort, Boolean onlyEnable);
    int getProductCount();
    Product save(ProductRequest request);
    Product getProductById(String id);
    void updateProductStatus(UpdateProductStatusRequest request);
    void updateProductAvatar(ProductRequest request);
    List<Object> getTopProduct(int top, int[] categoryIds);
    List<Product> getTopSale(int top);
    List<Product> getTopByName(int top, String name, Boolean status);
}
