package com.manonero.ecommerce.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.models.RegisterUser;

public interface IUserAccountService extends UserDetailsService {
    UserAccount findByUserName(String userName);

    UserAccount save(RegisterUser registerUser, int roleIndex);

    UserAccount getById(int id);
}
