package com.manonero.ecommerce.services;

import java.util.List;

import com.manonero.ecommerce.entities.Category;

public interface ICategoryService {
	List<Category> getAllCategory(Boolean isEnable);
	List<Category> getCategoryByOffsetLimit(int offset, int limit, String name, Boolean status);
	int getCategoryCount(String name, Boolean status);
	boolean addCategory(Category category);
	boolean editCategory(Category category);
	boolean toggleCategoryStatus(int id, boolean status);
	Category getCategoryById(int id);
}
