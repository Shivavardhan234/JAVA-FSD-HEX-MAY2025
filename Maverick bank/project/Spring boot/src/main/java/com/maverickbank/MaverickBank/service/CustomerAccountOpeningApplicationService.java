package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maverickbank.MaverickBank.dto.CustomerAccountOpeningInputDto;
import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.IdentityNotMatchException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.CustomerAccountOpeningApplication;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.repository.AccountHolderRepository;
import com.maverickbank.MaverickBank.repository.CustomerAccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.CustomerRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.AccountOpeningApplicationDtoValidation;
import com.maverickbank.MaverickBank.validation.CustomerAccountOpeningApplicationValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class CustomerAccountOpeningApplicationService {
	
	private CustomerAccountOpeningApplicationRepository customerAccountOpeningApplicationRepository;
	private UserRepository userRepository;
	private AccountHolderRepository accountHolderRepository;
	private AccountOpeningApplicationService accountOpeningApplicationService;
	private CustomerRepository customerRepository;
	Logger logger = LoggerFactory.getLogger(CustomerAccountOpeningApplicationService.class);
	
	public CustomerAccountOpeningApplicationService(
			CustomerAccountOpeningApplicationRepository customerAccountOpeningApplicationRepository,
			UserRepository userRepository, AccountHolderRepository accountHolderRepository,
			AccountOpeningApplicationService accountOpeningApplicationService, CustomerRepository customerRepository) {
		super();
		this.customerAccountOpeningApplicationRepository = customerAccountOpeningApplicationRepository;
		this.userRepository = userRepository;
		this.accountHolderRepository = accountHolderRepository;
		this.accountOpeningApplicationService = accountOpeningApplicationService;
		this.customerRepository = customerRepository;
	}








//---------------------------------------------- POST -------------------------------------------------------------------

	
	


	/**AIM : To add customerAccountOpeningApplication
	 * Step 1: Validate user activity status
	 * Step 2: Validate customerAccountInputdto
	 * Step 3: Extract the account opening application from customer account opening application
	 * Step 4: Validate bank account type and number of account holders
	 * 		=>if account type is joint it should have minimum 2 account holders, else 1
	 * Step 5:
	 * @param customerAccountOpeningApplicationList
	 * @param principal
	 * @return
	 * @throws InvalidInputException 
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws ResourceNotFoundException 
	 * @throws IdentityNotMatchException 
	 */
	@Transactional
	public List<CustomerAccountOpeningApplication> addcustomerAccountOpeningApplication(
			CustomerAccountOpeningInputDto customerAccountOpeningInputDto, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException, IdentityNotMatchException {
		logger.info("In Add customer account opening application.");
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		logger.info("Active status approved.");
		CustomerAccountOpeningApplicationValidation.validateCustomerAccountOpeningApplicationInputDto(customerAccountOpeningInputDto);
		logger.info("Customer account opening application validated.");
		
		//Extract Account opening application
		AccountOpeningApplication accountOpeningApplication= customerAccountOpeningInputDto.getAccountOpeningApplication();
		//Extract account type
		AccountType accountType= accountOpeningApplication.getAccountType();
		/*
		 * Now here check if account is joint type, 
		 * if yes account holders should be greater than or equal to 2
		 * then fetch the customer details of all the account holders using their mail and 
		 * store in list of customers
		 * if the current Actor(customer) is not in the list of customers then account opening application cannot be created due to identity invalid
		 * if all the conditions are met then save account holders in account holder and get the ids
		 * save updated account holders in the list 
		 * add application to database
		 * now update CustomerAccountHolderApplication with account holder id, customer id, application id
		 * */
		AccountOpeningApplicationDtoValidation.validateListSize(accountType.getAccountType(), customerAccountOpeningInputDto.getAccountHolderList());
		logger.info("Account holder size validation done.");
		
		//Check any one of the account holder matches current customer
		List<Customer> customerList=new ArrayList<>();
		
		for(AccountHolder a : customerAccountOpeningInputDto.getAccountHolderList()) {
			Customer customer=customerRepository.getByContactNumber(a.getContactNumber());
			if(customer==null) {
				logger.error("Account holder is not present in the customers.");
				throw new ResourceNotFoundException("Cannot submit application,No customer exist with given phone number"+a.getContactNumber());
			}
			customerList.add(customer);
		}
		logger.info("Customers for account holders are retrieved.");
		
		Customer presentCustomer=customerRepository.getByUsername(currentUser.getUsername());
		Boolean isPresent=false;
		for(Customer c: customerList) {
			if(c.getId()==presentCustomer.getId()) {
				isPresent=true;
			}
		}
		logger.info("Is current customer present in the retrieved customer list = "+isPresent);
		
		if(isPresent==false) {
			logger.error("Customers not present in the extracted customer list.");
			throw new IdentityNotMatchException("Customer's identity is absent in the given account holder list...!!! ");	
		}
		
		
		
		//SAVING step1: add account holders
		customerAccountOpeningInputDto.setAccountHolderList(accountHolderRepository.saveAll(customerAccountOpeningInputDto.getAccountHolderList()));
		logger.info("Account holders added");
		//SAVING step2: add application and get the updated application
		accountOpeningApplication = accountOpeningApplicationService.addAccountOpeningApplication(accountOpeningApplication, principal);
		customerAccountOpeningInputDto.setAccountOpeningApplication(accountOpeningApplication);
		logger.info("Application added.");
		
		//SAVING step3: Create list of CustomerAccountOpeningApplication and save
		List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList=new ArrayList<>();
		
		AccountHolder accountHolder;
		//Create CustomerAccountOpeningApplication object and add it to list
		for(int i=0;i<customerList.size();i++) {
			accountHolder = customerAccountOpeningInputDto.getAccountHolderList().get(i);
			
			CustomerAccountOpeningApplication customerAccountOpeningApplicationTemp=new CustomerAccountOpeningApplication();
			customerAccountOpeningApplicationTemp.setAccountHolder(accountHolder);
			customerAccountOpeningApplicationTemp.setCustomer(customerList.get(i));
			customerAccountOpeningApplicationTemp.setAccountOpeningApplication(accountOpeningApplication);
			if(customerList.get(i).getId()==presentCustomer.getId()) {
				customerAccountOpeningApplicationTemp.setCustomerApproval(ApplicationStatus.ACCEPTED);
			}
			else {
				customerAccountOpeningApplicationTemp.setCustomerApproval(ApplicationStatus.PENDING);
				
			}
			
			customerAccountOpeningApplicationList.add(customerAccountOpeningApplicationTemp);
			
		}//For loop
		logger.info("Customer account opening list created");
		
		customerAccountOpeningApplicationList=customerAccountOpeningApplicationRepository.saveAll(customerAccountOpeningApplicationList);
		logger.info("Customer account opening list saved in Database");
		accountOpeningApplicationService.updateCustomerApprovalStatus(accountOpeningApplication.getId(), principal);
		logger.info("Customer approval status is updated in account opening application.");
		return customerAccountOpeningApplicationList;
	}
	
	
//------------------------------------------ GET ------------------------------------------------------------------------
	 /**
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 */
	public List<CustomerAccountOpeningApplication> getAllApplications(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		logger.info("In get all customer account opening application.");
		User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status verified");

	        return customerAccountOpeningApplicationRepository.findAll();
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
	    public CustomerAccountOpeningApplication getById(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
	    	logger.info("In get customer account opening application by id.");
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status verified");
	        return customerAccountOpeningApplicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No customer account opening Application found with the given id...!!! " ));
	    }

	    
	    
	    
	    /**
	     * @param customerId
	     * @param principal
	     * @return
	     * @throws ResourceNotFoundException 
	     * @throws DeletedUserException 
	     * @throws InvalidActionException 
	     * @throws InvalidInputException 
	     */
	    public List<CustomerAccountOpeningApplication> getByCustomerId(int id, Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
	    	logger.info("In get customer account opening application by csutomer id.");
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status verified");
	        List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList = customerAccountOpeningApplicationRepository.getByCustomerId(id);
	        logger.info("Customer account opening list retrieved");
	        
	        if (customerAccountOpeningApplicationList.isEmpty()) {
	        	logger.error("No application records found with given customer id.");
	            throw new ResourceNotFoundException("No application records found with given customer id...!!!");
	        }
	        return customerAccountOpeningApplicationList;
	    }
	    
	    
	    
	    
	    
	    
	    
	    

	    /**
	     * @param id
	     * @param principal
	     * @return
	     * @throws ResourceNotFoundException 
	     * @throws DeletedUserException 
	     * @throws InvalidActionException 
	     * @throws InvalidInputException 
	     */
	    public List<CustomerAccountOpeningApplication> getByAccountHolderId(int id, Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
	    	logger.info("In get customer account opening application by account holder id.");
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status verified");
	        List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList = customerAccountOpeningApplicationRepository.getByAccountHolderId(id);
	        logger.info("Customer account opening list retrieved");
	        
	        if (customerAccountOpeningApplicationList.isEmpty()) {
	        	logger.error("No application records found with the given account holder id.");
	            throw new ResourceNotFoundException("No application records found with the given account holder id...!!!");
	        }
	        return customerAccountOpeningApplicationList;
	    }
	    
	    
	    
	    
	    

	    /**
	     * @param applicationId
	     * @param principal
	     * @return
	     * @throws DeletedUserException 
	     * @throws InvalidActionException 
	     * @throws InvalidInputException 
	     * @throws ResourceNotFoundException 
	     */
	    public List<CustomerAccountOpeningApplication> getByAccountOpeningApplicationId(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
	    	logger.info("In get customer account opening application by account opening application id.");
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status verified");
	        List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList = customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(id);
	        logger.info("Customer account opening list retrieved.");
	        
	        if (customerAccountOpeningApplicationList.isEmpty()) {
	        	logger.error("No application records found with the given account opening application id.");
	            throw new ResourceNotFoundException("No application records found with the given account opening application id...!!!");
	        }
	        return customerAccountOpeningApplicationList;
	    }
	    
	    
	    
	    /**
	     * @param customerId
	     * @param status
	     * @param principal
	     * @return
	     * @throws ResourceNotFoundException
	     * @throws InvalidInputException
	     * @throws InvalidActionException
	     * @throws DeletedUserException
	     */
	    public List<CustomerAccountOpeningApplication> getByCustomerIdAndStatus(int customerId,ApplicationStatus status, Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
	    	logger.info("In get customer account opening application by customer id and customer approval status."); 
	    	User currentUser = userRepository.getByUsername(principal.getName());
		        UserValidation.checkActiveStatus(currentUser.getStatus());
		        logger.info("User active status verified");
		        List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList = customerAccountOpeningApplicationRepository.getByCustomerIdAndStatus(customerId,status);
		        logger.info("Customer account opening list retrieved");
		        
		        if (customerAccountOpeningApplicationList.isEmpty()) {
		        	logger.error("No application records found with the given customer id and status.");
		            throw new ResourceNotFoundException("No application records found with the given customer id and status...!!!");
		        }
		        return customerAccountOpeningApplicationList;
		}
	    
	    /**
	     * @param principal
	     * @return
	     * @throws InvalidInputException
	     * @throws InvalidActionException
	     * @throws DeletedUserException
	     * @throws ResourceNotFoundException
	     */
	    public List<CustomerAccountOpeningApplication> getCustomerApprovalPending(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
	    	logger.info("In get pending customer account opening application for current customer.");
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status verified");
	        
	        Customer currentCustomer = customerRepository.getByUserId(currentUser.getId());
	        List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList =customerAccountOpeningApplicationRepository.getCustomerApprovalPending(currentCustomer.getId(),ApplicationStatus.PENDING);
	        logger.info("Customer account opening list retrieved");
	        
	        if (customerAccountOpeningApplicationList.isEmpty()) {
	        	logger.error("No pending application records found with the given customer id.");
	            throw new ResourceNotFoundException("No pending application records found with the given customer id...!!!");
	        }
			return customerAccountOpeningApplicationList;
		}
	    
	    
//---------------------------------------------- PUT ---------------------------------------------------------------------
	    /**
	     * @param customerAccountOpeningApplication
	     * @param principal
	     * @return
	     * @throws ResourceNotFoundException
	     * @throws InvalidInputException
	     * @throws InvalidActionException
	     * @throws DeletedUserException
	     */
	    public CustomerAccountOpeningApplication updateCustomerAccountOpeningApplication(CustomerAccountOpeningApplication customerAccountOpeningApplication, Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
	    	logger.info("In update customer account opening application .");
	    	// Check active status
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User status validated");
	        //Get current customer account opening application
	        CustomerAccountOpeningApplication currentApplication = customerAccountOpeningApplicationRepository.findById(customerAccountOpeningApplication.getId()).orElseThrow(() -> new ResourceNotFoundException("No application record found with the provided id...!!!"));
	        logger.info("Customer account opening application retrieved");
	        // Validate and update customer account opening application 
	        if (customerAccountOpeningApplication.getCustomer() != null) {
	        	logger.info("Updating customer in the customer account opening application");
	            CustomerAccountOpeningApplicationValidation.validateCustomer(customerAccountOpeningApplication.getCustomer());
	            currentApplication.setCustomer(customerAccountOpeningApplication.getCustomer());
	        }

	        
	        
	        
	        if (customerAccountOpeningApplication.getAccountHolder() != null) {
	        	logger.info("Updating account holder in the customer account opening application");
	            CustomerAccountOpeningApplicationValidation.validateAccountHolder(customerAccountOpeningApplication.getAccountHolder());
	            currentApplication.setAccountHolder(customerAccountOpeningApplication.getAccountHolder());
	        }

	        if (customerAccountOpeningApplication.getAccountOpeningApplication() != null) {
	        	logger.info("Updating account opening application in the customer account opening application");
	            CustomerAccountOpeningApplicationValidation.validateAccountOpeningApplication(customerAccountOpeningApplication.getAccountOpeningApplication());
	            currentApplication.setAccountOpeningApplication(customerAccountOpeningApplication.getAccountOpeningApplication());
	        }

	        if (customerAccountOpeningApplication.getCustomerApproval() != null) {
	        	logger.info("Updating customer approval in the customer account opening application");
	            CustomerAccountOpeningApplicationValidation.validateCustomerApproval(customerAccountOpeningApplication.getCustomerApproval());
	            currentApplication.setCustomerApproval(customerAccountOpeningApplication.getCustomerApproval());
	        }

	        logger.info("saving customer account opening application");
	        return customerAccountOpeningApplicationRepository.save(currentApplication);
	    }








	    /**
	     * @param id
	     * @param principal
	     * @return
	     * @throws ResourceNotFoundException
	     * @throws InvalidActionException
	     * @throws DeletedUserException
	     * @throws InvalidInputException
	     */
	    public CustomerAccountOpeningApplication updateApproval(int id,ApplicationStatus status, Principal principal) throws ResourceNotFoundException, InvalidActionException, DeletedUserException, InvalidInputException {
	    	logger.info("In update customer approval for customer account opening application .");
	    	// Check user is active or not
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status validated");

	        // get the customer account opening application by id
	        CustomerAccountOpeningApplication customerAccountOpeningApplication = customerAccountOpeningApplicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No customer account opening application was found with the provided id...!!!"));
	        logger.info("customer account opening application extracted");
	        // get the customer using username
	        Customer currentCustomer = customerRepository.getByUsername(currentUser.getUsername());
	        logger.info("Current customer extracted");
	        // Check if current customer is same as the customer in the customer account opening application
	        logger.info("Verifying the current customer is the same customer in the application");
	        if (customerAccountOpeningApplication.getCustomer().getId()!=(currentCustomer.getId())) {
	        	logger.error("Current customer is not the same customer in the customer account opening application");
	            throw new InvalidActionException("Invalid identity.You are not the person in application...!!!");
	        }

	        // Check present customer approval status
	        logger.info("Verifying customer approval status in the customer account opening application");
	        if (customerAccountOpeningApplication.getCustomerApproval() != ApplicationStatus.PENDING) {
	        	logger.error("This customer account opening application is already accepted or rejected");
	            throw new InvalidActionException("Action invalid.This application is already ACCEPTED or REJECTED...!!!");
	        }

	        // set the status 
	        customerAccountOpeningApplication.setCustomerApproval(status);
	        logger.info("New customer approval status for the customer account opening application is set");
	        //save the customer account application
	        customerAccountOpeningApplication=customerAccountOpeningApplicationRepository.save(customerAccountOpeningApplication);
	        logger.info("Customer account opening application is saved");
	        logger.info("Updating customer approval status in account opening application");
	        accountOpeningApplicationService.updateCustomerApprovalStatus(customerAccountOpeningApplication.getAccountOpeningApplication().getId(), principal);
	        return customerAccountOpeningApplication;
	    }








		








		

}
