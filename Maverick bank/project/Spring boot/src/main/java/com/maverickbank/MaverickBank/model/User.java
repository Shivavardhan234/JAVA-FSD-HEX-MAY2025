package com.maverickbank.MaverickBank.model;


import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	
	@Column(unique = true, nullable = false)
	private String username;
	
	
	@Column(nullable = false)
	private String password;
	
	
	@Column(name="role",nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	
	//---------------------------------- Constructors ---------------------------------------------
	public User() {}
	
	
	
	public User(int id, String username, String password, Role userType) throws InvalidInputException {
		this.setId( id);
		this.setUsername( username);
		this.setPassword( password);
		this.setRole(userType);
	}
	
	
	public User(int userId, String username, String password) throws InvalidInputException {
		this.setId( id);
		this.setUsername( username);
		this.setPassword( password);
	}
	
	
	public User(String username, String password) throws InvalidInputException {
		this.setUsername( username);
		this.setPassword( password);
	}
	public User(Role userType) throws InvalidInputException {
		this.setRole(userType);
	}
	
	
	public User(String username, String password, Role userType) throws InvalidInputException {
		this.setUsername(username);
		this.setPassword(password);
		this.setRole(userType);
	}



	//------------------------------------ Getters & Setters-----------------------------------------
	public int getId() {return id;}
	public String getUsername() {return username;}
	public String getPassword() {return password;}
	public Role getRole() {return role;}
	
	
	
	
	
	
	/**Here we check whether id is proper or not .
	 * Checks weather the id is less than 0, if yes throws a InvalidInputException.
	 * Else sets object's id to given id.
	 * @param userId
	 * @throws InvalidInputException
	 */
	public void setId(int id) throws InvalidInputException  {
		if( id<=0) {
			throw new InvalidInputException("User ID is Invalid. Please enter appropriate User ID...!!!");
		}
		this.id = id;
	}
	
	
	
	
	
	
	
	/**Here we check whether username is proper or not .
	 * if username is null or length less than 5 characters or doesnot follow the given pattern  then throws a InvalidInputException.
	 * Else sets object's address to the given address.
	 * @param username
	 * @throws InvalidInputException
	 */
	public void setUsername(String username)throws InvalidInputException {
		if(username==null || (username.trim().length()<=4)||!username.matches("^[A-Za-z0-9_.-]+$")) {
			throw new InvalidInputException("Username is Invalid. Please enter appropriate Username...!!!");
		}
		this.username = username;
	}
	
	
	
	
	/**Here we check whether password is proper or not .
	 * if password is null or length less than 8 characters or doesnot follow the given pattern  then throws a InvalidInputException.
	 * Else sets object's password to the given password.
	 * @param password
	 * @throws InvalidInputException
	 */
	public void setPassword(String password)throws InvalidInputException {
		if(password==null || (password.trim().length()<8)||!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[`~!@#$%^&*()_+=\\\\/?>\\[\\]\\-{}.,<';:]).+$")) {
			throw new InvalidInputException("Password is Invalid. Please enter appropriate Password...!!!");
		}
		this.password = password;
	}
	
	
	
	
	/**Here we check whether userType is proper or not .
	 * if userType(ENUM) is null then throws a InvalidInputException.
	 * Else sets object's userType to the given userType. 
	 * @param userType
	 * @throws InvalidInputException
	 */
	public void setRole(Role userType)throws InvalidInputException {
		if(userType==null) {
			throw new InvalidInputException("UserType is Invalid. Please check the logic...!!!");
		}//if statement
		this.role = userType;
	}

}
