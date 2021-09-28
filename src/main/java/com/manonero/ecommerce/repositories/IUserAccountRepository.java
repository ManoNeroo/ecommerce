package com.manonero.ecommerce.repositories;

import java.util.List;

import com.manonero.ecommerce.entities.UserAccount;

public interface IUserAccountRepository {

	UserAccount selectByUserName(String userName);

	UserAccount save(UserAccount user);

	void updateStatus(int id, boolean status);

	void updatePassword(int id, String password);

	void updateAvatar(int id, String avatar);

	void updateBasicInfo(int id, String firstName, String lastName, String phoneNumber, boolean gender);

	UserAccount selectById(int id);

	List<UserAccount> selectFilter(String userName, String phoneNumber, int[] roleIds, Boolean status);
}
