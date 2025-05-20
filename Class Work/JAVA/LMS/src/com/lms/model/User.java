package com.lms.model;

import com.lms.enums.Role;

public class User {
	private int userId;
	private String username;
	private String password;
	private Role role;
	
	
	
	public User() {}

	
	
	
	/**
	 * @param userId
	 * @param username
	 * @param password
	 * @param role
	 */
	public User(int userId, String username, String password, Role role) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	
	
	
	




	/**
	 * @param username
	 * @param password
	 * @param role
	 */
	public User(String username, String password, Role role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	
	
	public String getUsername() {return username;}
	public String getPassword() {return password;}
	public Role getRole() {return role;}
	public int getUserId() {return userId;}



	
	
	public void setUsername(String username) {this.username = username;}
	public void setPassword(String password) {this.password = password;}
	public void setRole(Role role) {this.role = role;}
	public void setUserId(int userId) {this.userId = userId;}
}
