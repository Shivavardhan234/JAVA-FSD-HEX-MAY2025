package com.maverickbank.MaverickBank.validation;

import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.users.Employee;

public class EmployeeValidation {
	
	public static void validateBranch(Branch branch) throws InvalidInputException {
		if (branch==null) {
			throw new InvalidInputException("Provided branch object is null. Please provide appropriate branch object...!!!");
		}
		return;
	}
	public static void validateDesignation(String designation) throws InvalidInputException {
		
		if ((designation==null ) || (designation.equalsIgnoreCase(String.valueOf(Role.ACCOUNT_MANAGER))
								&& (designation.equalsIgnoreCase(String.valueOf(Role.LOAN_OFFICER)))
								&& (designation.equalsIgnoreCase(String.valueOf(Role.TRANSACTION_ANALYST)))
								&& (designation.equalsIgnoreCase(String.valueOf(Role.JUNIOR_OPERATIONS_MANAGER)))
								&& (designation.equalsIgnoreCase(String.valueOf(Role.SENIOR_OPERATIONS_MANAGER))))){
			throw new InvalidInputException("Provided designation is invalid. Please provide appropriate designation...!!!");
		}
		return;
	}

	
	public static void fullEmployeeValidation(Employee employee) throws InvalidInputException {
		ActorValidation.validateName(employee.getName());
		ActorValidation.validateDob(employee.getDob());
		ActorValidation.validateEmail(employee.getEmail());
		ActorValidation.validateContactNumber(employee.getContactNumber());
		ActorValidation.validateGender(employee.getGender());
		ActorValidation.validateAddress(employee.getAddress());
		//this only checks weather user is null or not
		ActorValidation.validateUser(employee.getUser());
		validateDesignation(employee.getDesignation());
	}
}
