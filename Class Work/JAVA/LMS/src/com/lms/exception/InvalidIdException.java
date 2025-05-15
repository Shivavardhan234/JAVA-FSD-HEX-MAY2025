package com.lms.exception;

public class InvalidIdException extends Exception {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8587652395362251111L;
	private String message;
	
	
	public InvalidIdException(String message) { this.message= message; }
	
	public String getMessage()
	{
		return this.message;
	}
}
