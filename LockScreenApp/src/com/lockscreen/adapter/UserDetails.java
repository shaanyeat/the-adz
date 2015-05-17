package com.lockscreen.adapter;

public class UserDetails {
	
	public Integer UserId;
	public String firstName;
	public String lastName;
	public String email;
	public Integer gender;
	public String dateofBirth;
	public String password;
	public String notification;
	public String imgUrl;
	public String ApiKey;
	public String Token = null;
	
	public UserDetails(Integer UserId, String firstName, String lastName, String email, Integer gender,
			String dateofBirth, String password, String imgUrl, String ApiKey){
		this.UserId = UserId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.dateofBirth = dateofBirth;
		this.password = password;
//		this.notification = notification;
		this.imgUrl = imgUrl;
		this.ApiKey = ApiKey;
	}

	public UserDetails() {
		// TODO Auto-generated constructor stub
	}
	
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}

	public Integer getUserId() {
		return UserId;
	}
	public void setUserId(Integer UserId) {
		this.UserId = UserId;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getdateofBirth() {
		return dateofBirth;
	}
	public void setdateofBirth(String dateofBirth) {
		this.dateofBirth = dateofBirth;
	}
	public String getpassword() {
		return password;
	}
	public void setpassword(String password) {
		this.password = password;
	}
	public String getnotification() {
		return notification;
	}
	public void setnotification(String notification) {
		this.notification = notification;
	}
	public String getimgUrl() {
		return imgUrl;
	}
	public void setimgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getApiKey() {
		return ApiKey;
	}
	public void setApiKey(String ApiKey) {
		this.ApiKey = ApiKey;
	}
	
}
