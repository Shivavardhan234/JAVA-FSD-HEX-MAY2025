package com.maverickbank.MaverickBank.validation;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.users.CIO;

public class CIOValidation {
	
	public static void validateCioObject(CIO cio)throws InvalidInputException {
		if(cio==null) {
			throw new InvalidInputException("Invalid CIO. CIO is null...!!!");
		}
		return;
	}
	
	public static void validateCIO(CIO cio) throws InvalidInputException {
		validateCioObject(cio);
		ActorValidation.validateName(cio.getName());
		ActorValidation.validateAddress(cio.getAddress());
		ActorValidation.validateContactNumber(cio.getContactNumber());
		ActorValidation.validateDob(cio.getDob());
		ActorValidation.validateEmail(cio.getEmail());
		ActorValidation.validateGender(cio.getGender());
		ActorValidation.validateUserObject(cio.getUser());
		
	}

}
