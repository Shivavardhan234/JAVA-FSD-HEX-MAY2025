package com.maverickbank.MaverickBank.exception;

public class DeletedUserException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	
	
	public DeletedUserException(String message) {
		this.message = message;
	}

	
	
	public String getMessage() {
		return message;
	}
	
	
	
	

}
