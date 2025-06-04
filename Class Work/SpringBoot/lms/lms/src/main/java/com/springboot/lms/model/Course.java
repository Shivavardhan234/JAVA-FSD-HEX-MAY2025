package com.springboot.lms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	@Column(nullable =false)
	private String title;
	
	private int credits;

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getCredits() {
		return credits;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}
	
	
	

}
