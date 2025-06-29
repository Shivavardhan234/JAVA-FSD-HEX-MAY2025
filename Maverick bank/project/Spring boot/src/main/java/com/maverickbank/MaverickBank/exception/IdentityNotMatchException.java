package com.maverickbank.MaverickBank.exception;

public class IdentityNotMatchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String message;
	public IdentityNotMatchException(String message) {
		this.message=message;
	}
	
	public String getMessage() {
		return message;
	}

}
