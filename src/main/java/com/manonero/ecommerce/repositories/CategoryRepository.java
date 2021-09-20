package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.manonero.ecommerce.entities.Category;

@Repository
public class CategoryRepository implements ICategoryRepository {
	private EntityManager entityManager;

	@Autowired
	public CategoryRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> selectCategoryByOffsetLimit(int offset, int limit, String name, Boolean status) {
		StoredProcedureQuery query = entityManager
				.createNamedStoredProcedureQuery("Category.selectCategoryByOffsetLimit");
		query.setParameter("offset", offset);
		query.setParameter("limit", limit);
		query.setParameter("name", name);
		query.setParameter("status", status);
		return query.getResultList();
	}

	@Override
	public int selectCategoryCount(String name, Boolean status) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Category.selectCountCategory");
		query.setParameter("name", name);
		query.setParameter("status", status);
		query.execute();
		int count = (Integer) query.getOutputParameterValue("count");
		return count;
	}

	@Override
	public boolean insertCategory(Category category) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Category.insertCategory");
		query.setParameter("name", category.getName());
		query.setParameter("status", category.isStatus());
		query.setParameter("logo", category.getLogo());
		int rs = query.executeUpdate();
		if (rs > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateCategory(Category category) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Category.updateCategory");
		query.setParameter("id", category.getId());
		query.setParameter("status", category.isStatus());
		query.setParameter("logo", category.getLogo());
		query.setParameter("name", category.getName());
		query.setParameter("status", category.isStatus());
		query.setParameter("logo", category.getLogo());
		int rs = query.executeUpdate();
		if (rs > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Category selectCategoryById(int id) {
		Category category = entityManager.find(Category.class, id);
		return category;
	}

	@Override
	public boolean updateCategoryStatus(int id, boolean status) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Category.toggleCategoryStatus");
		query.setParameter("id", id);
		query.setParameter("status", status);
		int rs = query.executeUpdate();
		if (rs > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Category> selectAllCategory(Boolean isEnable, Boolean isSortByName) {
		String queryStr = "SELECT * FROM category";
		if(isEnable != null) {
			if(isEnable) {
				queryStr += " WHERE category_status=1";
			} else {
				queryStr += " WHERE category_status=0";
			}
		}
		if(isSortByName != null) {
			if(isSortByName) {
				queryStr += " ORDER BY category_name ASC";
			} else {
				queryStr += " ORDER BY category_name DESC";
			}
		}
		Query query = entityManager.createNativeQuery(queryStr, Category.class);

		@SuppressWarnings("unchecked")
		List<Category> categories = query.getResultList();
		return categories;
	}

}
