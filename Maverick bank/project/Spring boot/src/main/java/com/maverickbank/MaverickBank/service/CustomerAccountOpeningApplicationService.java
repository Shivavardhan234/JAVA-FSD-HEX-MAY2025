package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
		
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		CustomerAccountOpeningApplicationValidation.validateCustomerAccountOpeningApplicationInputDto(customerAccountOpeningInputDto);
		
		
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
		
		
		//Check any one of the account holder matches current customer
		List<Customer> customerList=new ArrayList<>();
		
		for(AccountHolder a : customerAccountOpeningInputDto.getAccountHolderList()) {
			Customer customer=customerRepository.getByContactNumber(a.getContactNumber());
			if(customer==null) {
				throw new ResourceNotFoundException("Cannot submit application,No customer exist with given phone number"+a.getContactNumber());
			}
			customerList.add(customer);
		}
		Customer presentCustomer=customerRepository.getByUsername(currentUser.getUsername());
		Boolean isPresent=false;
		for(Customer c: customerList) {
			if(c.getId()==presentCustomer.getId()) {
				isPresent=true;
			}
		}
		
		if(isPresent==false) {
			throw new IdentityNotMatchException("Customer's identity is absent in the given account holder list...!!! ");	
		}
		
		
		
		//SAVING step1: add account holders
		customerAccountOpeningInputDto.setAccountHolderList(accountHolderRepository.saveAll(customerAccountOpeningInputDto.getAccountHolderList()));
		//SAVING step2: add application and get the updated application
		accountOpeningApplication = accountOpeningApplicationService.addAccountOpeningApplication(accountOpeningApplication, principal);
		customerAccountOpeningInputDto.setAccountOpeningApplication(accountOpeningApplication);
		
		
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
		
		customerAccountOpeningApplicationList=customerAccountOpeningApplicationRepository.saveAll(customerAccountOpeningApplicationList);

		accountOpeningApplicationService.updateCustomerApprovalStatus(accountOpeningApplication.getId(), principal);
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
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

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
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

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
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList = customerAccountOpeningApplicationRepository.getByCustomerId(id);

	        if (customerAccountOpeningApplicationList == null) {
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
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList = customerAccountOpeningApplicationRepository.getByAccountHolderId(id);

	        if (customerAccountOpeningApplicationList == null ) {
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
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList = customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(id);

	        if (customerAccountOpeningApplicationList == null) {
	            throw new ResourceNotFoundException("No application records found with the given account opening application id...!!!");
	        }
	        return customerAccountOpeningApplicationList;
	    }
	    
	    
	    
	    public List<CustomerAccountOpeningApplication> getByCustomerIdAndStatus(int customerId,ApplicationStatus status, Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
	    	 User currentUser = userRepository.getByUsername(principal.getName());
		        UserValidation.checkActiveStatus(currentUser.getStatus());

		        List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList = customerAccountOpeningApplicationRepository.getByCustomerIdAndStatus(customerId,status);

		        if (customerAccountOpeningApplicationList == null) {
		            throw new ResourceNotFoundException("No application records found with the given customer id and status...!!!");
		        }
		        return customerAccountOpeningApplicationList;
		}
	    
	    public List<CustomerAccountOpeningApplication> getCustomerApprovalPending(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        
	        Customer currentCustomer = customerRepository.getByUserId(currentUser.getId());
	        List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList =customerAccountOpeningApplicationRepository.getCustomerApprovalPending(currentCustomer.getId(),ApplicationStatus.PENDING);
	        
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
	        // Check active status
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        //Get current customer account opening application
	        CustomerAccountOpeningApplication currentApplication = customerAccountOpeningApplicationRepository.findById(customerAccountOpeningApplication.getId()).orElseThrow(() -> new ResourceNotFoundException("No application record found with the provided id...!!!"));
	        // Validate and update customer account opening application 
	        if (customerAccountOpeningApplication.getCustomer() != null) {
	            CustomerAccountOpeningApplicationValidation.validateCustomer(customerAccountOpeningApplication.getCustomer());
	            currentApplication.setCustomer(customerAccountOpeningApplication.getCustomer());
	        }

	        
	        
	        
	        if (customerAccountOpeningApplication.getAccountHolder() != null) {
	            CustomerAccountOpeningApplicationValidation.validateAccountHolder(customerAccountOpeningApplication.getAccountHolder());
	            currentApplication.setAccountHolder(customerAccountOpeningApplication.getAccountHolder());
	        }

	        if (customerAccountOpeningApplication.getAccountOpeningApplication() != null) {
	            CustomerAccountOpeningApplicationValidation.validateAccountOpeningApplication(customerAccountOpeningApplication.getAccountOpeningApplication());
	            currentApplication.setAccountOpeningApplication(customerAccountOpeningApplication.getAccountOpeningApplication());
	        }

	        if (customerAccountOpeningApplication.getCustomerApproval() != null) {
	            CustomerAccountOpeningApplicationValidation.validateCustomerApproval(customerAccountOpeningApplication.getCustomerApproval());
	            currentApplication.setCustomerApproval(customerAccountOpeningApplication.getCustomerApproval());
	        }

	        		
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
	        // Check user is active or not
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        // get the customer account opening application by id
	        CustomerAccountOpeningApplication customerAccountOpeningApplication = customerAccountOpeningApplicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No customer account opening application was found with the provided id...!!!"));
	        // get the customer using username
	        Customer currentCustomer = customerRepository.getByUsername(currentUser.getUsername());

	        // Check if current customer is same as the customer in the customer account opening application
	        if (customerAccountOpeningApplication.getCustomer().getId()!=(currentCustomer.getId())) {
	            throw new InvalidActionException("Invalid identity.You are not the person in application...!!!");
	        }

	        // Check present customer approval status
	        if (customerAccountOpeningApplication.getCustomerApproval() != ApplicationStatus.PENDING) {
	            throw new InvalidActionException("Action invalid.This application is already ACCEPTED or REJECTED...!!!");
	        }

	        // set the status 
	        customerAccountOpeningApplication.setCustomerApproval(status);
	        //save the customer account application
	        customerAccountOpeningApplication=customerAccountOpeningApplicationRepository.save(customerAccountOpeningApplication);
	        accountOpeningApplicationService.updateCustomerApprovalStatus(customerAccountOpeningApplication.getAccountOpeningApplication().getId(), principal);
	        return customerAccountOpeningApplication;
	    }








		








		

}
