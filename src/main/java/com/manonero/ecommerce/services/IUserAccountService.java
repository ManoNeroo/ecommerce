package com.manonero.ecommerce.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.models.UserRequest;

public interface IUserAccountService extends UserDetailsService {

    int getNumberAccount();

    UserAccount findByUserName(String userName);

    UserAccount add(UserRequest request);

    void updateBasicInfo(UserRequest request);

    boolean updatePassword(UserRequest request);

    int[] updateRoles(UserRequest request);

    boolean updateStatus(UserRequest request);

    void updateAvatar(UserRequest request);

    UserAccount getById(int id);

    List<UserAccount> filter(Integer offset, Integer limit, String userName, String phoneNumber, int[] roleIds, Boolean status);
}
