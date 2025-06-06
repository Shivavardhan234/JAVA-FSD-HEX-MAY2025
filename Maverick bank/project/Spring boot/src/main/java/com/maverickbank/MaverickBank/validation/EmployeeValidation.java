package com.maverickbank.MaverickBank.validation;

import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.users.Employee;

public class EmployeeValidation {
	
	
	
	
	
	
	/**
	 * @param branch
	 * @throws InvalidInputException
	 */
	public static void validateBranch(Branch branch) throws InvalidInputException {
		if (branch==null) {
			throw new InvalidInputException("Provided branch object is null. Please provide appropriate branch object...!!!");
		}
		return;
	}
	
	
	public static void validateEmployeeObject(Employee employee) throws InvalidInputException {
		if (employee==null) {
			throw new InvalidInputException("Provided employee object is null. Please provide appropriate employee...!!!");
		}
		return;
	}
	
	
	
	
	
	
	
	
	/**
	 * @param designation
	 * @throws InvalidInputException
	 */
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
	
	
	
	
	
	
	
	
	/**
	 * @param role
	 * @throws InvalidInputException
	 */
	public static void validateEmployeeRole(Role role) throws InvalidInputException {
		if(role!=Role.ACCOUNT_MANAGER && role!=Role.LOAN_OFFICER &&
										 role != Role.TRANSACTION_ANALYST &&
										 role != Role.JUNIOR_OPERATIONS_MANAGER &&
										 role != Role.SENIOR_OPERATIONS_MANAGER) {
			throw new InvalidInputException("Provided Employee role is invalid. Please provide appropriate employee role...!!!");
		}
		return;
	}

	
	
	
	
	
	
	
	public static void validateEmployee(Employee employee) throws InvalidInputException {
		validateEmployeeObject(employee);
		ActorValidation.validateName(employee.getName());
		ActorValidation.validateDob(employee.getDob());
		ActorValidation.validateEmail(employee.getEmail());
		ActorValidation.validateContactNumber(employee.getContactNumber());
		ActorValidation.validateGender(employee.getGender());
		ActorValidation.validateAddress(employee.getAddress());
		//this only checks weather user is null or not
		ActorValidation.validateUserObject(employee.getUser());
		validateBranch(employee.getBranch());
		validateDesignation(employee.getDesignation());
	}
}
