package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	Logger logger = LoggerFactory.getLogger(CustomerAccountService.class);






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
		logger.info("In Create customer account");
		
		User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    logger.info("User active status validated");
	    
	    // get the CustomerAccountOpeningApplication from db using the id provided
	    CustomerAccountOpeningApplication customerAccountOpeningApplication = customerAccountOpeningApplicationRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No application record found with the given id...!!!"));
	    logger.info("Fetched customer account opening application");
	    
	    
	    AccountOpeningApplication accountOpeningApplication = customerAccountOpeningApplication.getAccountOpeningApplication();
	    logger.info("account opening applictaion fetched");
	    
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
	    logger.info("Pin for account is created");
	    
	    // Create new CustomerAccount entity
	    CustomerAccount customerAccount = new CustomerAccount();
	    customerAccount.setCustomer(customerAccountOpeningApplication.getCustomer());
	    customerAccount.setAccountHolder(customerAccountOpeningApplication.getAccountHolder());
	    customerAccount.setAccount(account);
	    pin=passwordEncoder.encode(pin);
	    customerAccount.setPin(pin);
	    logger.info("saving customer account");
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
		logger.info("In get all customer accounts");    
		User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status verified");
	        logger.info("Returning customer account list");
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
		logger.info("In get customer account by id");    
		User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status verified");
	        logger.info("Fetching customer account by id");
	        return customerAccountRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No customer account recotd found with the given id...!!! "));
	    }

	    
	    /**
	     * @param customerId
	     * @param principal
	     * @return
	     * @throws ResourceNotFoundException
	     * @throws DeletedUserException
	     * @throws InvalidInputException
	     * @throws InvalidActionException
	     */
	    public List<CustomerAccount> getByCustomerId(int customerId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    	logger.info("In get customer account by customer id");
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status validated");
	        List<CustomerAccount> list = customerAccountRepository.getByCustomerId(customerId);
	        if (list.isEmpty()) {
	            throw new ResourceNotFoundException("No customer accounts found with customer id...!!!");
	        }
	        logger.info("Customer account list for customer id fetched");
	        return list;
	    }

	   
	    /**
	     * @param accountHolderId
	     * @param principal
	     * @return
	     * @throws ResourceNotFoundException
	     * @throws DeletedUserException
	     * @throws InvalidInputException
	     * @throws InvalidActionException
	     */
	    public List<CustomerAccount> getByAccountHolderId(int accountHolderId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    	logger.info("In get customer account by account holder id");
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status validated");
	        List<CustomerAccount> list = customerAccountRepository.getByAccountHolderId(accountHolderId);
	        if (list.isEmpty()) {
	            throw new ResourceNotFoundException("No customer accounts found with account holder id...!!!");
	        }
	        logger.info("Customer account list for account holder id is fetched");
	        return list;
	    }

	    
	    /**
	     * @param accountId
	     * @param principal
	     * @return
	     * @throws ResourceNotFoundException
	     * @throws DeletedUserException
	     * @throws InvalidInputException
	     * @throws InvalidActionException
	     */
	    public List<CustomerAccount> getByAccountId(int accountId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    	logger.info("In get customer account by account id");
	    	
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status validated ");
	        
	        List<CustomerAccount> list = customerAccountRepository.getByAccountId(accountId);
	        if (list.isEmpty()) {
	            throw new ResourceNotFoundException("No customer accounts found with account id...!!!");
	        }
	        logger.info("Customer account list for account id is fetched");
	        return list;
	    }
	    
	    
	    /**
	     * @param customerId
	     * @param accountId
	     * @param principal
	     * @return
	     * @throws InvalidInputException
	     * @throws InvalidActionException
	     * @throws DeletedUserException
	     * @throws ResourceNotFoundException
	     */
	    public CustomerAccount getByCustomerIdAndAccountId(int customerId, int accountId, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
	    	logger.info("In get customer account by customer id and account id");
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status is validated ");
	        CustomerAccount customerAccount= customerAccountRepository.getByCustomerIdAndAccountId(customerId,accountId);
	        if (customerAccount==null) {
	            throw new ResourceNotFoundException("No customer accounts found with this customerId and account id...!!!");
	        }
	        logger.info("Customer account list for customer id and account id is fetched");
	        return customerAccount;
		}





//-------------------------------------------- PUT ----------------------------------------------------------------------
	    


		/**
		 * @param customerAccountId
		 * @param accountHolder
		 * @param principal
		 * @return
		 * @throws InvalidInputException
		 * @throws InvalidActionException
		 * @throws DeletedUserException
		 * @throws ResourceNotFoundException
		 */
		public CustomerAccount updateAccountHolder(int customerAccountId,AccountHolder accountHolder, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
			logger.info("In update account holder of customer account ");
			User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status validated");
	        
	        CustomerAccount customerAccount=customerAccountRepository.findById(customerAccountId).orElseThrow(() -> new ResourceNotFoundException("No customer account recotd found with the given id...!!! "));
	        logger.info("Customer account fetched");
	        
	        accountHolder=accountHolderService.addAccountHolder(accountHolder, principal);
	        logger.info("get new account holder");
	        customerAccount.setAccountHolder(accountHolder);
	        logger.info("new account holder us updated");
	        
	        logger.info("save customer account");
			return customerAccountRepository.save(customerAccount);
		}



		


}
