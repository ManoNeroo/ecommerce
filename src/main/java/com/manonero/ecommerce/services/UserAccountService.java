package com.manonero.ecommerce.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.manonero.ecommerce.configs.UserAccountRoleEnum;
import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.entities.UserRole;
import com.manonero.ecommerce.models.RegisterUser;
import com.manonero.ecommerce.repositories.IUserAccountRepository;
import com.manonero.ecommerce.repositories.IUserRoleRepository;

@Service
public class UserAccountService implements IUserAccountService {
	@Autowired
	private IUserAccountRepository userRepository;

	@Autowired
	private IUserRoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public UserAccount findByUserName(String userName) {
		return userRepository.selectByUserName(userName);
	}

	@Override
	@Transactional
	public UserAccount save(RegisterUser registerUser, int roleIndex) {
		try {
			String roleName = UserAccountRoleEnum.values()[roleIndex].name();
			UserAccount user = new UserAccount();
			user.setUserName(registerUser.getUserName());
			user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
			user.setFirstName(registerUser.getFirstName());
			user.setLastName(registerUser.getLastName());
			user.setPhoneNumber(registerUser.getPhoneNumber());
			user.setRoles(Arrays.asList(roleRepository.selectRoleByName(roleName)));
			return userRepository.save(user);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserAccount user = userRepository.selectByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<UserRole> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
