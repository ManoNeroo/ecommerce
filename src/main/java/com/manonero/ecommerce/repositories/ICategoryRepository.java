package com.manonero.ecommerce.repositories;

import java.util.List;

import com.manonero.ecommerce.entities.Category;

public interface ICategoryRepository {
	List<Category> selectAllCategory(Boolean isEnable, Boolean isSortByName);
	List<Category> selectCategoryByOffsetLimit(int offset, int limit, String name, Boolean status);
	int selectCategoryCount(String name, Boolean status);
	boolean insertCategory(Category category);
	boolean updateCategory(Category category);
	boolean updateCategoryStatus(int id, boolean status);
	Category selectCategoryById(int id);
}
