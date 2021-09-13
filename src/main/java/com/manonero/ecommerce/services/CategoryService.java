package com.manonero.ecommerce.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manonero.ecommerce.entities.Category;
import com.manonero.ecommerce.repositories.ICategoryRepository;

@Service
public class CategoryService implements ICategoryService {
	@Autowired
	private ICategoryRepository categoryRepository;

	@Override
	@Transactional
	public List<Category> getCategoryByOffsetLimit(int offset, int limit, String name, Boolean status) {
		// TODO Auto-generated method stub
		List<Category> categories = categoryRepository.selectCategoryByOffsetLimit(offset, limit, name, status);
		return categories;
	}

	@Override
	@Transactional
	public int getCategoryCount(String name, Boolean status) {
		// TODO Auto-generated method stub
		return categoryRepository.selectCategoryCount(name, status);
	}

	@Override
	@Transactional
	public boolean addCategory(Category category) {
		// TODO Auto-generated method stub
		return categoryRepository.insertCategory(category);
	}

	@Override
	@Transactional
	public boolean editCategory(Category category) {
		// TODO Auto-generated method stub
		return categoryRepository.updateCategory(category);
	}

	@Override
	@Transactional
	public Category getCategoryById(int id) {
		return categoryRepository.selectCategoryById(id);
	}

	@Override
	@Transactional
	public boolean toggleCategoryStatus(int id, boolean status) {
		// TODO Auto-generated method stub
		return categoryRepository.updateCategoryStatus(id, status);
	}

	@Override
	public List<Category> getAllCategory(Boolean isEnable) {
		// TODO Auto-generated method stub
		return categoryRepository.selectAllCategory(isEnable);
	}

}
