package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.CustomerAccount;
import com.maverickbank.MaverickBank.model.CustomerAccountOpeningApplication;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.CustomerAccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.CustomerAccountRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.AccountOpeningApplicationValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class CustomerAccountService {
	private CustomerAccountRepository customerAccountRepository;
	private UserRepository userRepository;
	private CustomerAccountOpeningApplicationRepository customerAccountOpeningApplicationRepository;
	private PasswordEncoder passwordEncoder;
	private AccountHolderService accountHolderService;
	






public CustomerAccountService(CustomerAccountRepository customerAccountRepository, UserRepository userRepository,
			CustomerAccountOpeningApplicationRepository customerAccountOpeningApplicationRepository,
			 PasswordEncoder passwordEncoder, AccountHolderService accountHolderService) {
		super();
		this.customerAccountRepository = customerAccountRepository;
		this.userRepository = userRepository;
		this.customerAccountOpeningApplicationRepository = customerAccountOpeningApplicationRepository;
		this.passwordEncoder = passwordEncoder;
		this.accountHolderService = accountHolderService;
	}



//-------------------------------------------- POST ----------------------------------------------------------------------


	/**
	 * @param id
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws InvalidInputException
	 * @throws DeletedUserException
	 * @throws InvalidActionException 
	 */
	public CustomerAccount createCustomerAccount(int id,Account account, Principal principal)throws ResourceNotFoundException, InvalidInputException, DeletedUserException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    // get the CustomerAccountOpeningApplication from db using the id provided
	    CustomerAccountOpeningApplication customerAccountOpeningApplication = customerAccountOpeningApplicationRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No application record found with the given id...!!!"));
	    
	    
	    
	    AccountOpeningApplication accountOpeningApplication = customerAccountOpeningApplication.getAccountOpeningApplication();
	    //Validate so that it will not be null
	    AccountOpeningApplicationValidation.validateAccountOpeningApplicationObject(accountOpeningApplication);
	    
	    // Check whether the approval customer and employee is accepted or not
	    if (accountOpeningApplication.getCustomerApprovalStatus() != ApplicationStatus.ACCEPTED
	            || accountOpeningApplication.getEmployeeApprovalStatus() != ApplicationStatus.ACCEPTED) {
	        throw new InvalidInputException("Acceptance not proper for the provided customer account opening application...!!!");
	    }
	    
	    // Get customer phone number and extract first 6 digits as PIN
	    String contactNumber = customerAccountOpeningApplication.getCustomer().getContactNumber();
	    
	    String pin = contactNumber.substring(0, 6);
	    
	    // Create new CustomerAccount entity
	    CustomerAccount customerAccount = new CustomerAccount();
	    customerAccount.setCustomer(customerAccountOpeningApplication.getCustomer());
	    customerAccount.setAccountHolder(customerAccountOpeningApplication.getAccountHolder());
	    customerAccount.setAccount(account);
	    pin=passwordEncoder.encode(pin);
	    customerAccount.setPin(pin);
	    
	    // Save the CustomerAccount in the database and return updated customer account
	    return customerAccountRepository.save(customerAccount);
	}
	
	
	
//----------------------------------------- GET -------------------------------------------------------------------------
	 /**
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public List<CustomerAccount> getAllCustomerAccounts(Principal principal) throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        
	        return customerAccountRepository.findAll();
	    }
	 
	 
	 /**
	 * @param id
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public CustomerAccount getById(int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        return customerAccountRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No customer account recotd found with the given id...!!! "));
	    }

	    
	    public List<CustomerAccount> getByCustomerId(int customerId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        List<CustomerAccount> list = customerAccountRepository.getByCustomerId(customerId);
	        if (list.isEmpty()) {
	            throw new ResourceNotFoundException("No customer accounts found with customer id...!!!");
	        }
	        return list;
	    }

	   
	    public List<CustomerAccount> getByAccountHolderId(int accountHolderId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        List<CustomerAccount> list = customerAccountRepository.getByAccountHolderId(accountHolderId);
	        if (list.isEmpty()) {
	            throw new ResourceNotFoundException("No customer accounts found with account holder id...!!!");
	        }
	        return list;
	    }

	    
	    public List<CustomerAccount> getByAccountId(int accountId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        List<CustomerAccount> list = customerAccountRepository.getByAccountId(accountId);
	        if (list.isEmpty()) {
	            throw new ResourceNotFoundException("No customer accounts found with account id...!!!");
	        }
	        return list;
	    }
	    
	    
	    public CustomerAccount getByCustomerIdAndAccountId(int customerId, int accountId, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        CustomerAccount customerAccount= customerAccountRepository.getByCustomerIdAndAccountId(customerId,accountId);
	        if (customerAccount==null) {
	            throw new ResourceNotFoundException("No customer accounts found with this customerId and account id...!!!");
	        }
	        return customerAccount;
		}





//-------------------------------------------- PUT ----------------------------------------------------------------------
	    


		public CustomerAccount updateAccountHolder(int customerAccountId,AccountHolder accountHolder, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
			User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        
	        
	        CustomerAccount customerAccount=customerAccountRepository.findById(customerAccountId).orElseThrow(() -> new ResourceNotFoundException("No customer account recotd found with the given id...!!! "));
	        
	        
	        accountHolder=accountHolderService.addAccountHolder(accountHolder, principal);
	        customerAccount.setAccountHolder(accountHolder);
	        
	        
			return customerAccountRepository.save(customerAccount);
		}



		


}
