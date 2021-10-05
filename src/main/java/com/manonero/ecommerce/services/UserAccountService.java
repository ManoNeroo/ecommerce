package com.manonero.ecommerce.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.manonero.ecommerce.entities.NotificationTopic;
import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.entities.UserRole;
import com.manonero.ecommerce.models.UserRequest;
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

	private int numberAccount;

	@Override
	@Transactional
	public UserAccount findByUserName(String userName) {
		return userRepository.selectByUserName(userName);
	}

	@Override
	@Transactional
	public UserAccount add(UserRequest request) {
		try {
			if (request.getPassword().equals(request.getMatchingPassword())) {
				UserAccount user = new UserAccount();
				user.setUserName(request.getUserName());
				user.setPassword(passwordEncoder.encode(request.getPassword()));
				user.setFirstName(request.getFirstName());
				user.setLastName(request.getLastName());
				user.setPhoneNumber(request.getPhoneNumber());
				boolean gender = request.getGender() != null ? request.getGender() : false;
				user.setGender(gender);
				user.setAvatar(gender ? "/admin/img/girl.png" : "/admin/img/boy.png");
				user.setStatus(true);
				Set<UserRole> roles = generateRoles(request.getRoleIxs());
				Set<NotificationTopic> topics = generateNotificationTopic(roles, request.getUserName());
				if (roles.size() > 0) {
					user.setNotificationTopics(topics);
					user.setRoles(roles);
					return userRepository.save(user);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	private Set<UserRole> generateRoles(int[] roleIxs) {
		Set<UserRole> roles = new HashSet<>();
		if (roleIxs != null) {
			for (int i = 0; i < roleIxs.length; i++) {
				int ix = roleIxs[i] - 1;
				if (ix >= 0 && ix <= 3) {
					String roleName = UserAccountRoleEnum.values()[ix].name();
					roles.add(roleRepository.selectRoleByName(roleName));
				}
			}
		}
		return roles;
	}

	private Set<NotificationTopic> generateNotificationTopic(Set<UserRole> roles, String userName) {
		Set<NotificationTopic> topics = new HashSet<>();
		topics.add(new NotificationTopic("TOPIC_USER_" + userName.toUpperCase()));
		boolean isTopicAdmin = roles.stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN")
				|| r.getName().equals("ROLE_MANAGER") || r.getName().equals("ROLE_EMPLOYEE"));
		boolean isTopicCustomer = roles.stream().anyMatch(r -> r.getName().equals("ROLE_CUSTOMER"));
		if (isTopicAdmin) {
			topics.add(new NotificationTopic("TOPIC_ADMIN"));
		}
		if (isTopicCustomer) {
			topics.add(new NotificationTopic("TOPIC_CUSTOMER"));
		}
		return topics;
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

	@Override
	@Transactional
	public UserAccount getById(int id) {
		return userRepository.selectById(id);
	}

	@Override
	@Transactional
	public List<UserAccount> filter(Integer offset, Integer limit, String userName, String phoneNumber, int[] roleIds,
			Boolean status) {
		List<UserAccount> accounts = userRepository.selectFilter(userName, phoneNumber, roleIds, status);
		this.numberAccount = accounts.size();
		if (offset != null && limit != null) {
			int ix1 = offset - 1;
			int ix2 = offset + limit - 1;
			ix1 = ix1 >= 0 ? ix1 : 0;
			ix2 = ix2 >= 1 ? ix2 : 1;
			ix1 = this.numberAccount > ix1 ? ix1 : 0;
			ix2 = this.numberAccount > ix2 ? ix2 : this.numberAccount;
			return accounts.subList(ix1, ix2);
		}
		return accounts;
	}

	@Override
	public int getNumberAccount() {
		return this.numberAccount;
	}

	@Override
	@Transactional
	public void updateBasicInfo(UserRequest request) {
		userRepository.updateBasicInfo(request.getUserId(), request.getFirstName(), request.getLastName(),
				request.getPhoneNumber(), request.getGender());
	}

	@Override
	@Transactional
	public boolean updatePassword(UserRequest request) {
		if (request.getNewPassword().equals(request.getMatchingPassword())) {
			UserAccount dbAccount = getById(request.getUserId());
			String encodeNewPassword = passwordEncoder.encode(request.getNewPassword());
			if (passwordEncoder.matches(request.getPassword(), dbAccount.getPassword())) {
				userRepository.updatePassword(request.getUserId(), encodeNewPassword);
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public int[] updateRoles(UserRequest request) {
		if (request.getRoleIxs() != null) {
			if (request.getRoleIxs().length > 0) {
				UserAccount account = getById(request.getUserId());
				if (account != null) {
					Set<UserRole> roles = generateRoles(request.getRoleIxs());
					Set<NotificationTopic> topics = generateNotificationTopic(roles, account.getUserName());
					for (NotificationTopic topic : account.getNotificationTopics()) {
						boolean isContain = false;
						for(NotificationTopic topic2 : topics) {
							if(topic2.equals(topic)) {
								isContain = true;
								break;
							}
						}
						if(!isContain && !topic.getId().equals("TOPIC_CUSTOMER") && !topic.getId().equals("TOPIC_ADMIN")) {
							topics.add(topic);
						}
					}
					if (roles.size() > 0) {
						account.setNotificationTopics(topics);
						account.setRoles(roles);
						userRepository.save(account);
						return request.getRoleIxs();
					}
				}
			}
		}
		return null;
	}

	@Override
	@Transactional
	public boolean updateStatus(UserRequest request) {
		userRepository.updateStatus(request.getUserId(), request.getStatus());
		return request.getStatus();
	}

	@Override
	@Transactional
	public void updateAvatar(UserRequest request) {
		userRepository.updateAvatar(request.getUserId(), request.getAvatar());
	}

}
