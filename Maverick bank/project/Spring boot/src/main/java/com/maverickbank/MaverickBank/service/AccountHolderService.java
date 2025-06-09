package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountHolderRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.AccountHolderValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class AccountHolderService {
	
	private AccountHolderRepository accountHolderRepository;
	private UserRepository userRepository;
	public AccountHolderService(AccountHolderRepository accountHolderRepository, UserRepository userRepository) {
		this.accountHolderRepository = accountHolderRepository;
		this.userRepository=userRepository;
	}
	
	
//------------------------------------------------- POST ----------------------------------------------------------------
	/**Validates and Stores Account holder in database
	 * @param accountHolder
	 * @return
	 * @throws InvalidInputException
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws Exception
	 */
	public AccountHolder addAccountHolder(AccountHolder accountHolder, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Checking active Status
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				
			
		AccountHolderValidation.validateAccountHolder(accountHolder);
		
		
		return accountHolderRepository.save(accountHolder);
	}
//---------------------------------------------- GET --------------------------------------------------------------------
	/**
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 */
	public List<AccountHolder> getAllAccountHolder(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		return accountHolderRepository.findAll();
	}


	/**
	 * @param id
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 */
	public AccountHolder getAccountHolderById(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		return accountHolderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Account holder record with goven id...!!!"));
	}


	/**
	 * @param email
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 */
	public List<AccountHolder> getAccountHolderByEmail(String email, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Checking active Status
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				AccountHolderValidation.validateEmail(email);
				List<AccountHolder> accountHolderList= accountHolderRepository.getByEmail(email);
				if(accountHolderList==null) {
					throw new ResourceNotFoundException("No Account holder record with goven email...!!!");
				}
		return accountHolderList;
	}


	/**
	 * @param contactNumber
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 */
	public List<AccountHolder> getAccountHolderByContactNumber(String contactNumber, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		AccountHolderValidation.validateContactNumber(contactNumber);
		List<AccountHolder> accountHolderList= accountHolderRepository.getByContactNumber(contactNumber);
		if(accountHolderList==null) {
			throw new ResourceNotFoundException("No Account holder record with goven contact number...!!!");
		}
		return accountHolderList;
	}


	/**
	 * @param aadharNumber
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 */
	public List<AccountHolder> getAccountHolderByAadharNumber(String aadharNumber, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Checking active Status
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				
				AccountHolderValidation.validateAadharNumber(aadharNumber);
				List<AccountHolder> accountHolderList= accountHolderRepository.getByAadharNumber(aadharNumber);
				if(accountHolderList==null) {
					throw new ResourceNotFoundException("No Account holder record with goven aadhar number...!!!");
				}
		return accountHolderList;
	}


	/**
	 * @param panCardNumber
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 */
	public List<AccountHolder> getAccountHolderByPanCardNumber(String panCardNumber, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Checking active Status
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				
				AccountHolderValidation.validatePanCardNumber(panCardNumber);
				List<AccountHolder> accountHolderList= accountHolderRepository.getByPancardNumber(panCardNumber);
				
				if(accountHolderList==null) {
					throw new ResourceNotFoundException("No Account holder record with goven pan card number...!!!");
				}
		return accountHolderList;
	}



	
	
	
//---------------------------------------- PUT ---------------------------------------------------------------------------
	/**
	 * @param accountHolder
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 */
	public AccountHolder updateAccountHolder(AccountHolder accountHolder, Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		AccountHolder currentAccountHolder=accountHolderRepository.findById(accountHolder.getId()).orElseThrow(()-> new ResourceNotFoundException("No account holder record with given id...!!!"));
		
		//Validate and update
		if(accountHolder.getName()!=null) {
			AccountHolderValidation.validateName(accountHolder.getName());
			currentAccountHolder.setName(accountHolder.getName());
		}
		if(accountHolder.getDob()!=null) {
			AccountHolderValidation.validateDob(accountHolder.getDob());
			currentAccountHolder.setDob(accountHolder.getDob());
		}
		if(accountHolder.getGender()!=null) {
			AccountHolderValidation.validateGender(accountHolder.getGender());
			currentAccountHolder.setGender(accountHolder.getGender());
		}
		if(accountHolder.getEmail()!=null) {
			AccountHolderValidation.validateEmail(accountHolder.getEmail());
			currentAccountHolder.setEmail(accountHolder.getEmail());
		}
		if(accountHolder.getContactNumber()!=null) {
			AccountHolderValidation.validateContactNumber(accountHolder.getContactNumber());
			currentAccountHolder.setContactNumber(accountHolder.getContactNumber());
		}
		if(accountHolder.getAddress()!=null) {
			AccountHolderValidation.validateAddress(accountHolder.getAddress());
			currentAccountHolder.setAddress(accountHolder.getAddress());
		}
		
		
		
		
		//Validate and set aadhar number
		if(accountHolder.getAadharNumber()!=null) {
			AccountHolderValidation.validateAadharNumber(accountHolder.getAadharNumber());
			currentAccountHolder.setAadharNumber(accountHolder.getAadharNumber());
		}
				
		//Validate and set pan card number
		if(accountHolder.getPanCardNumber()!=null) {
			AccountHolderValidation.validatePanCardNumber(accountHolder.getPanCardNumber());
			currentAccountHolder.setPanCardNumber(accountHolder.getPanCardNumber());
		}		
		
		return accountHolderRepository.save(currentAccountHolder);
	}


	

}
