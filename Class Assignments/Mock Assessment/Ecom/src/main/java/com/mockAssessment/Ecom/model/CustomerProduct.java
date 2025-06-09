package com.mockAssessment.Ecom.model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="customer_product")
public class CustomerProduct {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Customer customer;

	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Product product;
	
	@ManyToOne
	private Coupon coupon;

	@Column(name="date_time_of_purchase",nullable=false)
	private LocalDateTime dateTimeOfPurchase;
	
	//------------------------ Constructors --------------------------------------------------------
	public CustomerProduct() {}

	public CustomerProduct(int id, Customer customer, Product product, Coupon coupon,LocalDateTime dateTimeOfPurchase) {
		super();
		this.id = id;
		this.customer = customer;
		this.product = product;
		this.coupon = coupon;
		this.dateTimeOfPurchase=dateTimeOfPurchase;
	}
	
	//-------------------------------------- Getters & Setters -------------------------------------

	public LocalDateTime getDateTimeOfPurchase() {
		return dateTimeOfPurchase;
	}

	

	public int getId() {
		return id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Product getProduct() {
		return product;
	}

	public Coupon getCoupon() {
		return coupon;
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

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	public void setDateTimeOfPurchase(LocalDateTime dateTimeOfPurchase) {
		this.dateTimeOfPurchase = dateTimeOfPurchase;
	}

}
