package com.maverickbank.MaverickBank.validation;

import java.util.List;

import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;

public class AccountOpeningApplicationDtoValidation {
	
	
	
	public static void validateAccountOpeningApplication(AccountOpeningApplication application) throws InvalidInputException{
		if(application==null) {
			throw new InvalidInputException("given account account opening application is invalid...!!!");
		}
		return;
	}
	
	
	
	
	public static void validateCustomerList(List<AccountHolder> accountHolderList) throws InvalidInputException {
		if(accountHolderList==null || accountHolderList.size()<=0 ||accountHolderList.size()>4 ) {
			throw new InvalidInputException("given account holder list is invalid...!!!");
		}
		for(AccountHolder a: accountHolderList) {
			AccountHolderValidation.validateAccountHolder(a);
		}
		
		return;
	}
	
	public static void validateListSize(BankAccountType accountType, List<AccountHolder> accountHolderList ) throws InvalidInputException {
		if(((accountType==BankAccountType.JOINT_BUSINESS||
		   accountType==BankAccountType.JOINT_CURRENT||
		   accountType==BankAccountType.JOINT_FIXED||
		   accountType==BankAccountType.JOINT_SAVINGS)&& accountHolderList.size()<2)||
		(((accountType==BankAccountType.BUSINESS||
		   accountType==BankAccountType.CURRENT||
		   accountType==BankAccountType.FIXED||
		   accountType==BankAccountType.SAVINGS)&& accountHolderList.size()>1))) {
			throw new InvalidInputException("Number of account holders are invalid...!!!");
			
		}
	}
	
	
	
	
}
