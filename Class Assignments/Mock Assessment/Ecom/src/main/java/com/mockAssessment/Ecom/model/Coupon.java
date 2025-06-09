package com.mockAssessment.Ecom.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="coupon_code",nullable=false)
	private String couponCode;
	@Column(nullable=false,unique=true)
	private BigDecimal discount;
	
	//--------------------------------- Constructors -------------------------------------------
	
	public Coupon(int id, String couponCode, BigDecimal discount) {
		super();
		this.id = id;
		this.couponCode = couponCode;
		this.discount = discount;
	}
	
	public Coupon() {}
	
	
	
	//---------------------------- getters & Setters------------------------------------------
	
	public int getId() {
		return id;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	
	


}
