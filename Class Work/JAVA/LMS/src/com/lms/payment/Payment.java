package com.lms.payment;

public enum Payment {
	UPI(1), NEFT(1), IMPS(0);
	
	Payment(int status){
		this.status=status;
	}
	private int status;
	
	public int getStatus() {
		return status;
	}
}
