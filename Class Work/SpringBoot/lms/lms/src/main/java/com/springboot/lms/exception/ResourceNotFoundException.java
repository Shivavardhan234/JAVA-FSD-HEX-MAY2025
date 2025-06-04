package com.springboot.lms.exception;

public class ResourceNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8183706157009762410L;
	
	
	private String message;
	
	
	public ResourceNotFoundException(String message) {
		this.message=message;
	}
	
	
	public String getMessage() {
		return message;
	}

}
