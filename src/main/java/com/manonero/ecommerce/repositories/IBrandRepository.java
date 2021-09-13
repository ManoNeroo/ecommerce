package com.manonero.ecommerce.repositories;

import java.util.List;

import com.manonero.ecommerce.entities.Brand;

public interface IBrandRepository {
	List<Brand> selectBrandByOffsetLimit(int offset, int limit, String name, Boolean status);
	int selectBrandCount(String name, Boolean status);
	Brand save(Brand brand);
	boolean updateBrandStatus(int id, boolean status);
	Brand selectBrandById(int id);
	List<Brand> selectAllBrand(Boolean isEnable);
}
