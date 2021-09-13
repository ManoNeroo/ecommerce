package com.manonero.ecommerce.services;

import java.util.List;

import com.manonero.ecommerce.entities.Brand;
import com.manonero.ecommerce.models.BrandRequest;

public interface IBrandService {
    List<Brand> getBrandByOffsetLimit(int offset, int limit, String name, Boolean status);
	int getBrandCount(String name, Boolean status);
	Brand save(BrandRequest request);
	boolean toggleBrandStatus(int id, boolean status);
	Brand getBrandById(int id);
	List<Brand> getAllBrand(Boolean isEnable);
}
