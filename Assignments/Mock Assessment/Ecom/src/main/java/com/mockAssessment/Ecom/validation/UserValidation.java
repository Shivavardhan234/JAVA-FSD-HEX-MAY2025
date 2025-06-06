package com.mockAssessment.Ecom.validation;

import com.mockAssessment.Ecom.exception.InvalidInputException;
import com.mockAssessment.Ecom.model.User;

public class UserValidation {
	
	
	public static void validateUsername(String username) throws InvalidInputException {
		if(username==null|| username.trim().isEmpty()) {
			throw new InvalidInputException("Provided username is null or empty..!!");
		}
		
	}
	
	
	public static void validateUser(User user) throws InvalidInputException {
		if(user==null) {
			throw new InvalidInputException("Provided user object is null..!!");
		}
		if(user.getUsername()==null|| user.getUsername().trim().isEmpty()) {
			throw new InvalidInputException("Provided username is null or empty..!!");
		}
		if(user.getPassword()==null|| user.getPassword().trim().isEmpty()) {
			throw new InvalidInputException("Provided password is null or empty..!!");
		}
		if(user.getRole()==null) {
			throw new InvalidInputException("Provided role is null or empty..!!");
		}
		
	}
}
