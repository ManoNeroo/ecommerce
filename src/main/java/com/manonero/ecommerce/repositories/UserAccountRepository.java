package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
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
		TypedQuery<UserAccount> query = entityManager.createQuery("from UserAccount where userName=:uName",
				UserAccount.class);
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

	@Override
	public UserAccount selectById(int id) {
		UserAccount account = entityManager.find(UserAccount.class, id);
		return account;
	}

	@Override
	public List<UserAccount> selectFilter(String userName, String phoneNumber, int[] roleIds, Boolean status) {
		String mainQueryStr = "SELECT DISTINCT ua.* FROM user_acc ua";
		String whereQueryString = "";
		if (userName != null) {
			whereQueryString += " WHERE (ua.user_name LIKE '%" + userName + "%'";
			if (phoneNumber != null) {
				whereQueryString += " OR ua.user_phone_number LIKE '%" + phoneNumber + "%'";
			}
			whereQueryString += ")";
		}

		if (roleIds != null) {
			if (roleIds.length > 0) {
				mainQueryStr += ", user_role_detail urd";
				if (whereQueryString.equals("")) {
					whereQueryString += " WHERE ua.user_id=urd.user_id";
				} else {
					whereQueryString += " AND ua.user_id=urd.user_id";
				}
				for (int i = 0; i < roleIds.length; i++) {
					if (i == 0) {
						whereQueryString += " AND (urd.role_id=" + roleIds[i];
					} else {
						whereQueryString += " OR urd.role_id=" + roleIds[i];
					}
				}
				whereQueryString += ")";
			}
		}

		if (status != null) {
			if (whereQueryString.equals("")) {
				whereQueryString += " WHERE ua.user_status=" + (status ? 1 : 0);
			} else {
				whereQueryString += " AND ua.user_status=" + (status ? 1 : 0);
			}
		}

		mainQueryStr += whereQueryString;
		Query query = entityManager.createNativeQuery(mainQueryStr, UserAccount.class);
		@SuppressWarnings("unchecked")
		List<UserAccount> accounts = query.getResultList();
		return accounts;
	}

	@Override
	public void updateStatus(int id, boolean status) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("UserAccount.updateStatus");
		query.setParameter("id", id);
		query.setParameter("status", status);
		query.executeUpdate();
	}

	@Override
	public void updatePassword(int id, String password) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("UserAccount.updatePassword");
		query.setParameter("id", id);
		query.setParameter("password", password);
		query.executeUpdate();
	}

	@Override
	public void updateAvatar(int id, String avatar) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("UserAccount.updateAvatar");
		query.setParameter("id", id);
		query.setParameter("avatar", avatar);
		query.executeUpdate();
	}

	@Override
	public void updateBasicInfo(int id, String firstName, String lastName, String phoneNumber, boolean gender) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("UserAccount.updateBasicInfo");
		query.setParameter("id", id);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		query.setParameter("phoneNumber", phoneNumber);
		query.setParameter("gender", gender);
		query.executeUpdate();
	}
}
