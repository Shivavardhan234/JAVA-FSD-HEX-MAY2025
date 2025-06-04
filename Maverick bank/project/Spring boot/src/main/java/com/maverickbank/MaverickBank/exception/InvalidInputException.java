package com.maverickbank.MaverickBank.exception;

public class InvalidInputException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -298344229301380597L;
	private String message;
	
	public InvalidInputException(String message) {
		this.message=message;
	}
	
	
	
	public String getMessage() {
		return message;
	}
}
