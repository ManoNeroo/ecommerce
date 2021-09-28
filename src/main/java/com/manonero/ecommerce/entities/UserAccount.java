package com.manonero.ecommerce.entities;

import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "user_acc")
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "UserAccount.updateBasicInfo", procedureName = "usp_updateUserBasicInfo", parameters = {
				@StoredProcedureParameter(name = "id", type = Integer.class),
				@StoredProcedureParameter(name = "firstName", type = String.class),
				@StoredProcedureParameter(name = "lastName", type = String.class),
				@StoredProcedureParameter(name = "phoneNumber", type = String.class),
				@StoredProcedureParameter(name = "gender", type = Boolean.class) }),
		@NamedStoredProcedureQuery(name = "UserAccount.updatePassword", procedureName = "usp_updateUserPassword", parameters = {
				@StoredProcedureParameter(name = "id", type = Integer.class),
				@StoredProcedureParameter(name = "password", type = String.class) }),
		@NamedStoredProcedureQuery(name = "UserAccount.updateAvatar", procedureName = "usp_updateUserAvatar", parameters = {
				@StoredProcedureParameter(name = "id", type = Integer.class),
				@StoredProcedureParameter(name = "avatar", type = String.class) }),
		@NamedStoredProcedureQuery(name = "UserAccount.updateStatus", procedureName = "usp_updateUserStatus", parameters = {
				@StoredProcedureParameter(name = "id", type = Integer.class),
				@StoredProcedureParameter(name = "status", type = Boolean.class) }) })
public class UserAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "user_password")
	private String password;

	@Column(name = "user_first_name")
	private String firstName;

	@Column(name = "user_last_name")
	private String lastName;

	@Column(name = "user_phone_number")
	private String phoneNumber;

	@Column(name = "user_gender")
	private boolean gender;

	@Column(name = "user_avatar")
	private String avatar;

	@Column(name = "user_status")
	private Boolean status;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role_detail", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<UserRole> roles;

	public UserAccount() {

	}

	public UserAccount(int id, String userName, String password, String firstName, String lastName, String phoneNumber,
			boolean gender, String avatar, Boolean status, Collection<UserRole> roles) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.avatar = avatar;
		this.status = status;
		this.roles = roles;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Collection<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Collection<UserRole> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", userName='" + userName + '\'' + ", password='" + "*********" + '\''
				+ ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", phoneNumber='"
				+ phoneNumber + '\'' + ", roles=" + roles + '}';
	}
}
