package com.hospitalManagementSystem.HospitalManagementSystem.exception;

public class ResourceExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public ResourceExistsException(String message) {
		this.message=message;
	}
	
	
	
	public String getMessage() {
		return message;
	}

}
