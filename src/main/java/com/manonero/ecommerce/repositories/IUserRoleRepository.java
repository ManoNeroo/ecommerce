package com.manonero.ecommerce.repositories;

import com.manonero.ecommerce.entities.UserRole;

public interface IUserRoleRepository {
	UserRole selectRoleByName(String roleName);
}
