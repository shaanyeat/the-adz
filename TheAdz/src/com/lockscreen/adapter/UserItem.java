package com.lockscreen.adapter;

/*Developer: TAI ZHEN KAI
Project 2015*/

public class UserItem {
	
	public Integer UserId;
	public String firstName;
	public String lastName;
	public String email;
	public Integer gender;
	public String dateofBirth;
	public String password;
	public String status;
	public String imgUrl;
	public int point;
	public String ApiKey;
	public String Token = null;
	
	public UserItem(Integer UserId, String firstName, String lastName, String email, Integer gender,
			String dateofBirth, String Status, String imgUrl, Integer point){
		this.UserId = UserId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.dateofBirth = dateofBirth;
		this.status = Status;
		this.imgUrl = imgUrl;
		this.point = point;
	}

	public UserItem() {
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	
	public Integer getPoints(){
		return point;
	}
	
	public void setPoints(Integer points){
		this.point = points;
	}
	
}
