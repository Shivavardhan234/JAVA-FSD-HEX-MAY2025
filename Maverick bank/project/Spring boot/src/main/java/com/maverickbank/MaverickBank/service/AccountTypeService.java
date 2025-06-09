package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountTypeRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.AccountTypeValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class AccountTypeService {
	private AccountTypeRepository accountTypeRepository;
	private UserRepository userRepository;
	
	public AccountTypeService(AccountTypeRepository accountTypeRepository,UserRepository userRepository) {
		this.accountTypeRepository=accountTypeRepository;
		this.userRepository=userRepository;
	}
	
	
//------------------------------------------ POST ------------------------------------------------------------------------
	public AccountType addAccountType(AccountType accountType, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceExistsException {
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				
				AccountTypeValidation.validateAccountType(accountType);
				
				if(accountTypeRepository.getAccountTypeByName(accountType.getAccountType())!=null) {
					throw new ResourceExistsException("Bank Account type already exists...!!! ");
				}
				
		return accountTypeRepository.save(accountType);
	}
	
	
	
//---------------------------------------- GET ---------------------------------------------------------------------------	
	
	
	public List<AccountType> getAllAccountType(Integer page, Integer size, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException{
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		Pageable pageable= PageRequest.of(page, size);
		return accountTypeRepository.findAll(pageable).getContent();
	}
	
	
	
	public AccountType getAccountTypeByName(BankAccountType type, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		return accountTypeRepository.getAccountTypeByName(type);
	}


	public AccountType getAccountTypeById(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		return accountTypeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Account type record with given Id...!!!"));
	}


	

//------------------------------------------------ PUT -------------------------------------------------------------------
	public AccountType updateAccountType(AccountType accountType, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
		AccountTypeValidation.validateAccountTypeObject(accountType);
		AccountType previousAccountType=accountTypeRepository.findById(accountType.getAccountTypeId()).orElseThrow(()-> new ResourceNotFoundException("No Account type record with given Id...!!!"));
				
		if(accountType.getInterestRate()!=null) {
			AccountTypeValidation.validateInterestRate(accountType.getInterestRate());
			previousAccountType.setInterestRate(accountType.getInterestRate());
		}
		if(accountType.getMinimumBalance()!=null) {
			AccountTypeValidation.validateMinimumBalance(accountType.getMinimumBalance());
			previousAccountType.setMinimumBalance(accountType.getMinimumBalance());
		}
		if(accountType.getTransactionLimit()!= previousAccountType.getTransactionLimit()) {
			AccountTypeValidation.validateTransactionLimit(accountType.getTransactionLimit());
			previousAccountType.setTransactionLimit(accountType.getTransactionLimit());
		}
		if(accountType.getTransactionAmountLimit()!=null) {
			AccountTypeValidation.validateTransactionAmountLimit(accountType.getTransactionAmountLimit());
			previousAccountType.setTransactionAmountLimit(accountType.getTransactionAmountLimit());
		}
		if(accountType.getWithdrawLimit()!=previousAccountType.getWithdrawLimit()) {
			AccountTypeValidation.validateWithdrawLimit(accountType.getWithdrawLimit());
			previousAccountType.setWithdrawLimit(accountType.getWithdrawLimit());
		}
		
		
		return accountTypeRepository.save(previousAccountType);
	}


}
