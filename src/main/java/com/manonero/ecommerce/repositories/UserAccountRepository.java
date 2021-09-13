package com.manonero.ecommerce.repositories;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.manonero.ecommerce.entities.UserAccount;

@Repository
public class UserAccountRepository implements IUserAccountRepository {
	private EntityManager entityManager;
	
	@Autowired
	public UserAccountRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public UserAccount selectByUserName(String userName) {
		TypedQuery<UserAccount> query = entityManager
				.createQuery("from UserAccount where userName=:uName", UserAccount.class);
		query.setParameter("uName", userName);
		UserAccount userAccount = null;
		try {
			userAccount = query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return userAccount;
	}

	@Override
	public UserAccount save(UserAccount user) {
		UserAccount userAccount = entityManager.merge(user);
		return userAccount;	
	}
}
