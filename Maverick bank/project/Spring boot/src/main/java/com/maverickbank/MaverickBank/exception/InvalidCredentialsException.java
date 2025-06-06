package com.maverickbank.MaverickBank.exception;

public class InvalidCredentialsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String message;


	public InvalidCredentialsException(String message) {
		this.message = message;
	}


	
	
	public String getMessage() {
		return message;
	}
	
	
	
	
}
