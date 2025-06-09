package com.mockAssessment.Ecom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Customer customer;

	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Product product;
	
	private String comment;
	
	@Column(nullable = false)
	private float rating;

	
	
	//-------------------------------- Constructor -------------------------------------------------
	public Review() {}

	public Review(int id, Customer customer, Product product, String comment, float rating) {
		super();
		this.id = id;
		this.customer = customer;
		this.product = product;
		this.comment = comment;
		this.rating = rating;
	}
	
	//---------------------------- Getters & Setters -----------------------------------------------

	public int getId() {
		return id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Product getProduct() {
		return product;
	}

	public String getComment() {
		return comment;
	}

	public float getRating() {
		return rating;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
	
	
	
	
	
	

}
