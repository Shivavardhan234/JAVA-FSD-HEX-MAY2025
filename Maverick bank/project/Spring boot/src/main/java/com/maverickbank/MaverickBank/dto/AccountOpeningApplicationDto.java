package com.maverickbank.MaverickBank.dto;

import java.util.List;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.validation.AccountHolderValidation;



public class AccountOpeningApplicationDto {

	private AccountOpeningApplication application;
	private List<AccountHolder> accountHolderList;
	
	
//-------------------------------------------- Constructors --------------------------------------------------------	
	public AccountOpeningApplicationDto() {}



	public AccountOpeningApplicationDto(AccountOpeningApplication application,List<AccountHolder> accountHolderList) throws InvalidInputException {
		
	}


// ------------------------------------Getters & Setters -----------------------------------------------------------


	public AccountOpeningApplication getApplication() {return application;}
	public List<AccountHolder> getAccountHolderList(){return accountHolderList;}


	public void setApplication(AccountOpeningApplication application) throws InvalidInputException{
		if(application==null) {
			throw new InvalidInputException("given account account opening application is invalid...!!!");
		}
		this.application=application;
	}
	
	
	public void setAccountHolderList(List<AccountHolder> accountHolderList) throws InvalidInputException {
		if(accountHolderList==null || accountHolderList.size()<=0 ||accountHolderList.size()>4 ) {
			throw new InvalidInputException("given account holder list is invalid...!!!");
		}
		for(AccountHolder a: accountHolderList) {
			AccountHolderValidation.validateAccountHolder(a);
		}
		
		this.accountHolderList=accountHolderList;
	}
	
	
	

}
