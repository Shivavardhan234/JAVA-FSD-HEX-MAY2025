package com.maverickbank.MaverickBank.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;

@Component
public class CustomerAccountOpeningInputDto {
	
	private List<AccountHolder> accountHolderList;
	private AccountOpeningApplication accountOpeningApplication;
	
	
	
	public CustomerAccountOpeningInputDto() {
		super();
	}





	public CustomerAccountOpeningInputDto(List<AccountHolder> accountHolderList,
			AccountOpeningApplication accountOpeningApplication) {
		super();
		this.accountHolderList = accountHolderList;
		this.accountOpeningApplication = accountOpeningApplication;
	}
	
	
	
	
	
	public List<AccountHolder> getAccountHolderList() {
		return accountHolderList;
	}
	public AccountOpeningApplication getAccountOpeningApplication() {
		return accountOpeningApplication;
	}
	public void setAccountHolderList(List<AccountHolder> accountHolderList) {
		this.accountHolderList = accountHolderList;
	}
	public void setAccountOpeningApplication(AccountOpeningApplication accountOpeningApplication) {
		this.accountOpeningApplication = accountOpeningApplication;
	}
	
	
	

}
