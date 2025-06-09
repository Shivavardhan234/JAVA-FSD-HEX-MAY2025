package com.maverickbank.MaverickBank.exception;

public class NotEnoughFundsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	public NotEnoughFundsException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	

}
