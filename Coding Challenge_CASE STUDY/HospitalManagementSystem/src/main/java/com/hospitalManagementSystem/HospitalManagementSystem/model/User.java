package com.hospitalManagementSystem.HospitalManagementSystem.model;



import com.hospitalManagementSystem.HospitalManagementSystem.enums.Role;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	
	
	public User(int id, String username, String password, Role role) throws InvalidInputException {
		this.setId( id);
		this.setUsername( username);
		this.setPassword( password);
		this.setRole(role);
	}
	
	public User(Role role) throws InvalidInputException {
		this.setRole(role);
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
	 * @param username
	 * @throws InvalidInputException
	 */
	public void setUsername(String username)throws InvalidInputException {
		if(username==null ) {
			throw new InvalidInputException("Username is Invalid. Please enter appropriate Username...!!!");
		}
		this.username = username;
	}
	
	
	
	
	/**Here we check whether password is proper or not .
	 * if password is null or length less than 8 characters  then throws a InvalidInputException.
	 * Else sets object's password to the given password.
	 * @param password
	 * @throws InvalidInputException
	 */
	public void setPassword(String password)throws InvalidInputException {
		if(password==null || (password.trim().length()<8)) {
			throw new InvalidInputException("Password is Invalid. Please enter appropriate Password...!!!");
		}
		this.password = password;
	}
	
	
	
	
	/**Here we check whether userType is proper or not .
	 * if role(ENUM) is null then throws a InvalidInputException.
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
