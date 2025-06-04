package com.maverickbank.MaverickBank.service;

import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.repository.AccountHolderRepository;
import com.maverickbank.MaverickBank.validation.AccountHolderValidation;

@Service
public class AccountHolderService {
	
	AccountHolderRepository ahr;

	public AccountHolderService(AccountHolderRepository ahr) {
		this.ahr = ahr;
	}
	
	
	
	/**Validates and Stores Account holder in database
	 * @param accountHolder
	 * @return
	 * @throws InvalidInputException
	 * @throws Exception
	 */
	public AccountHolder addAccountHolder(AccountHolder accountHolder) throws InvalidInputException , Exception{
		AccountHolderValidation.validateAccountHolder(accountHolder);
		return ahr.save(accountHolder);
	}


}
