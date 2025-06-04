package com.maverickbank.MaverickBank.validation;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.users.CIO;

public class CIOValidation {
	public static void fullCioValidation(CIO cio) throws InvalidInputException {
		ActorValidation.validateName(cio.getName());
		ActorValidation.validateAddress(cio.getAddress());
		ActorValidation.validateContactNumber(cio.getContactNumber());
		ActorValidation.validateDob(cio.getDob());
		ActorValidation.validateEmail(cio.getEmail());
		ActorValidation.validateGender(cio.getGender());
		ActorValidation.validateUser(cio.getUser());
		
	}

}
