package com.maverickbank.MaverickBank.exception;

public class InvalidActionException  extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	public InvalidActionException(String message) {
		this.message = message;
	}

	
	
	
	public String getMessage() {
		return message;
	}
	
	

}
