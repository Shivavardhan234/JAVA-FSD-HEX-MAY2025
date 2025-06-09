package com.mockAssessment.Ecom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false,unique=true)
	private String contact;
	@ManyToOne
	@JoinColumn(nullable=false,unique=true)
	private User user;
	
	
	//------------------------------------- Constructors ------------------------------------------
	public Customer() {}
	
	
	public Customer(int id, String name, String contact, User user) {
		super();
		this.id = id;
		this.name = name;
		this.contact = contact;
		this.user = user;
	}
	
	
	
	//-------------------------------- Getters & Setters --------------------------------------------
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getContact() {
		return contact;
	}
	public User getUser() {
		return user;
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
	public void setUser(User user) {
		this.user = user;
	}
	
	
	

}
