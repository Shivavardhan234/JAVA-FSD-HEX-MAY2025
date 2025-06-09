package com.maverickbank.MaverickBank.validation;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.CustomerAccountOpeningApplication;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.dto.CustomerAccountOpeningInputDto;

public class CustomerAccountOpeningApplicationValidation {
	
	public static  void validateId(int id)  throws InvalidInputException{
		return;
	}
	
	
	public static  void validateCustomer(Customer customer) throws InvalidInputException {
		if(customer==null ) {
			throw new InvalidInputException("Invalid customer object provided. Please provide appropriate customer object...!!!");
		}
		return;
	}
	
	
	
	public  static void validateAccountHolder(AccountHolder accountHolder) throws InvalidInputException {
		if(accountHolder==null ) {
			throw new InvalidInputException("Invalid account holder object provided. Please provide appropriate accountHolder object...!!!");
		}
		return;
	}
	
	
	
	
	public  static void validateAccountOpeningApplication(AccountOpeningApplication accountOpeningApplication) throws InvalidInputException {
		if(accountOpeningApplication==null ) {
			throw new InvalidInputException("Invalid account opening application object provided. Please provide appropriate object...!!!");
		}
		return;
	}
	
	
	public static void validateCustomerApproval(ApplicationStatus customerApproval) throws InvalidInputException {
		if(customerApproval==null ) {
			throw new InvalidInputException("Invalid customer approval provided. Please provide appropriate customer approval...!!!");
		}
		return;
	}
	
	
	public static void validateCustomerAccountOpeningApplicationObject(CustomerAccountOpeningApplication customerAccountOpeningApplication)throws InvalidInputException {
		if(customerAccountOpeningApplication==null ) {
			throw new InvalidInputException("Invalid null customer account opening application provided...!!!");
		}
		return;
	}
	
	public static void validateCustomerAccountOpeningApplication(CustomerAccountOpeningApplication customerAccountOpeningApplication)throws InvalidInputException {
		validateCustomerAccountOpeningApplicationObject(customerAccountOpeningApplication);
		
		validateAccountHolder(customerAccountOpeningApplication.getAccountHolder());
		validateAccountOpeningApplication(customerAccountOpeningApplication.getAccountOpeningApplication());
		return;
	}
	
	public static void validateCustomerAccountOpeningApplicationInputDtoObject(CustomerAccountOpeningInputDto customerAccountOpeningApplicationInputDto)throws InvalidInputException {
		if(customerAccountOpeningApplicationInputDto==null ) {
			throw new InvalidInputException("Invalid null customer account opening application dto provided...!!!");
		}
		return;
	}
	
	public static void validateCustomerAccountOpeningApplicationInputDto(CustomerAccountOpeningInputDto customerAccountOpeningApplicationInputDto)throws InvalidInputException {
		validateCustomerAccountOpeningApplicationInputDtoObject(customerAccountOpeningApplicationInputDto);
		for(AccountHolder a: customerAccountOpeningApplicationInputDto.getAccountHolderList()) {
			validateAccountHolder(a);
			AccountHolderValidation.validateAccountHolder(a);
		}
		
		validateAccountOpeningApplication(customerAccountOpeningApplicationInputDto.getAccountOpeningApplication());
		AccountOpeningApplicationValidation.validateAccountOpeningApplication2(customerAccountOpeningApplicationInputDto.getAccountOpeningApplication());
		return;
	}


	
	
	
	
}

