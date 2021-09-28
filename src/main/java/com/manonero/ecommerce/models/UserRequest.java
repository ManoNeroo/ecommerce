package com.manonero.ecommerce.models;

public class UserRequest {

	private Integer userId;

	private String userName;

	private String password;

	private String newPassword;

	private String matchingPassword;

	private String firstName;

	private String lastName;

	private String phoneNumber;

	private Boolean gender;

	private Boolean status;

	private String avatar;

	private int[] roleIxs;

	public UserRequest() {
	}

	public UserRequest(Integer userId, String userName, String password, String newPassword, String matchingPassword,
			String firstName, String lastName, String phoneNumber, Boolean gender, Boolean status, String avatar,
			int[] roleIxs) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.newPassword = newPassword;
		this.matchingPassword = matchingPassword;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.roleIxs = roleIxs;
		this.status = status;
		this.avatar = avatar;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public int[] getRoleIxs() {
		return roleIxs;
	}

	public void setRoleIxs(int[] roleIxs) {
		this.roleIxs = roleIxs;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getFirstName() {
		return firstName;
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

}
