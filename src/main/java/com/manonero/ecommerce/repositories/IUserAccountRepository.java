package com.manonero.ecommerce.repositories;

import com.manonero.ecommerce.entities.UserAccount;

public interface IUserAccountRepository {
	UserAccount selectByUserName(String userName);

	UserAccount save(UserAccount user);

	UserAccount selectById(int id);
}
