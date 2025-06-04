package com.springboot.lms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Learner {
	
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String contact;
	@ManyToOne
	@JoinColumn(unique =true)
	private UserInfo user;
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getContact() {
		return contact;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	
	

}
