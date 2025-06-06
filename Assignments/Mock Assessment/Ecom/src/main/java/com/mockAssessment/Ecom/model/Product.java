package com.mockAssessment.Ecom.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false)
	private String title;
	@Column(nullable=false)
	private BigDecimal price;
	
	//---------------------------------- Constructor -----------------------------------------------
	public Product() {}
	public Product(int id, String title, BigDecimal price) {
		super();
		this.id = id;
		this.title = title;
		this.price = price;
	}
	
	
	//----------------------------- Getters & setters-----------------------------------------------
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
	
	
	

}
