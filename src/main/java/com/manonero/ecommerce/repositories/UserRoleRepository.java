package com.manonero.ecommerce.repositories;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.manonero.ecommerce.entities.UserRole;

@Repository
public class UserRoleRepository implements IUserRoleRepository {
	private EntityManager entityManager;

	@Autowired
	public UserRoleRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public UserRole selectRoleByName(String roleName) {
		TypedQuery<UserRole> query = entityManager.createQuery("from UserRole where name=:rName", UserRole.class);
		query.setParameter("rName", roleName);
		UserRole userRole = null;
		try {
			userRole = query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return userRole;
	}

}
