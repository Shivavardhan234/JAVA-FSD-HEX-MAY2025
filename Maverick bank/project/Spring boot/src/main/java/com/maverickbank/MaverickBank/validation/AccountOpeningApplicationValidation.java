package com.maverickbank.MaverickBank.validation;

import java.time.LocalDateTime;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.Branch;

public class AccountOpeningApplicationValidation {
	
	public static void validateId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Account application ID is Invalid. Please enter appropriate ID...!!!");
		}
		return;
	}





	public static void validateBranch(Branch branch) throws InvalidInputException{
		if(branch==null ) {
			throw new InvalidInputException("Invalid branch object provided. Please provide appropriate branch object...!!!");
		}
		return;
	}



	public static void validateAccountType(AccountType accountType) throws InvalidInputException{
		if(accountType==null ) {
			throw new InvalidInputException("Invalid account type object provided. Please provide appropriate account type object...!!!");
		}
		return;
	}



	public static void validatePurpose(String purpose) throws InvalidInputException{
		return;
	}

	
	public static void validateCustomerApprovalStatus(ApplicationStatus customerApprovalStatus) throws InvalidInputException{
		if(customerApprovalStatus==null) {
			throw new InvalidInputException("Null customer approval status provided.should not be null...!!!");
		}
		return;
	}


	public static void validateEmployeeApprovalStatus(ApplicationStatus employeeApprovalStatus) throws InvalidInputException{
		if(employeeApprovalStatus==null) {
			throw new InvalidInputException("Null employee approval status provided.should not be null...!!!");
		}
		return;
	}

	
	public static void validateApplicationDateTime(LocalDateTime applicationDateTime) throws InvalidInputException {
		if(applicationDateTime==null) {
			throw new InvalidInputException("Null application date time provided. Application date time should not be null...!!!");
		}
		return;
	}
	
	public static void validateAccountOpeningApplicationObject(AccountOpeningApplication accountOpeningApplication)throws InvalidInputException{
		if(accountOpeningApplication==null) {
			throw new InvalidInputException("Null account opening application provided...!!!");
		}
		return;
	}
	
	public static void validateAccountOpeningApplication(AccountOpeningApplication accountOpeningApplication)throws InvalidInputException{
		validateAccountOpeningApplicationObject(accountOpeningApplication);
		validateBranch(accountOpeningApplication.getBranch());
		BranchValidation.validateBranchName(accountOpeningApplication.getBranch().getBranchName());
		validateAccountType(accountOpeningApplication.getAccountType());
		AccountTypeValidation.validateBankAccountType(accountOpeningApplication.getAccountType().getAccountType());
		
		
		return;
	}
	
	public static void validateAccountOpeningApplication2(AccountOpeningApplication accountOpeningApplication)throws InvalidInputException{
		validateAccountOpeningApplicationObject(accountOpeningApplication);
		validateBranch(accountOpeningApplication.getBranch());
		BranchValidation.validateBranchName(accountOpeningApplication.getBranch().getBranchName());
		validateAccountType(accountOpeningApplication.getAccountType());
		AccountTypeValidation.validateBankAccountType(accountOpeningApplication.getAccountType().getAccountType());
		
		return;
	}
	
	
	
	
}
