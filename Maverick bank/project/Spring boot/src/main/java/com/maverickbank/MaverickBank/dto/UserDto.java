package com.maverickbank.MaverickBank.dto;


import org.springframework.stereotype.Component;

import com.maverickbank.MaverickBank.model.User;


@Component
public class UserDto {
	
	
	private String username;
	private String password;
	
	
	
	public UserDto() {
		super();
	}


	public UserDto(String username,String password) {
		super();
		this.username = username;
		this.password=password;
	}
	
	
	public UserDto(User user) {
		this.username = user.getUsername();
	}
	
	
	
	
	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	
	
	
	public String getUsername() {
		return username;
	}



	



	public void setUsername(String username) {
		this.username = username;
	}



	



	
	
	
	
	
	
	
	
	

}
