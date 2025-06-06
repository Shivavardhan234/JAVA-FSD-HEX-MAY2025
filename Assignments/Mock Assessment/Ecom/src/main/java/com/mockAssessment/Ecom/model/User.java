package com.mockAssessment.Ecom.model;

import com.mockAssessment.Ecom.enums.Role;

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
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false,unique=true)
	private String username;
	@Column(nullable=false)
	private String password;
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private Role role;
	
	
	
	//---------------------------Constructors--------------------------------------------------------

	
	
	public User(int id, String username, String password, Role role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	
	public User() {}


	
	
	
	//------------------------------------- Getters & Setters ---------------------------------------
	public int getId() {
		return id;
	}


	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}


	public Role getRole() {
		return role;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setRole(Role role) {
		this.role = role;
	}
	
	
}
