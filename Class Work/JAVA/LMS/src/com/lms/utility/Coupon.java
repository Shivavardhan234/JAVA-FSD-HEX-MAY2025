package com.lms.utility;

public enum Coupon {
	
	WELCOME_20(20), WELCOME_15(15), DIWALI_DHAMAKA(25), NEWYEAR_NEWSTART(10), SUMMER_START(15);
	private double discount;
	
	Coupon(double discount) {
		this.discount=discount;
	}
	
	public double getDiscount() {
		return discount;
	}
	
}
