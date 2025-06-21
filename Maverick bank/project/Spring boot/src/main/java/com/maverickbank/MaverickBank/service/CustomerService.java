package com.maverickbank.MaverickBank.service;


import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.repository.CustomerRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.ActorValidation;
import com.maverickbank.MaverickBank.validation.CustomerValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class CustomerService {
	private CustomerRepository customerRepository;
	private UserRepository userRepository;
	private UserService userService;
	

	
public CustomerService(CustomerRepository customerRepository, UserRepository userRepository,
			UserService userService) {
		super();
		this.customerRepository = customerRepository;
		this.userRepository = userRepository;
		this.userService = userService;
	}



//-------------------------------- ADD -------------------------------------------------------------
	
	/** Add Customer() is a method where we are validating the details of a customer and saves into database
	 * Step 3: Validate customer using validation classes 
	 * Step 4: As contact number and email are unique check weather they exists, if exists throw resource exists exception
	 * Step 5: Encode the password using password encoder
	 * Step 6: set status to active as we are creating it now
	 * Step 7: save the user and set the returned user object to customer object
	 * Step 8: Save customer in the db
	 * @param customer
	 * @return
	 * @throws ResourceExistsException 
	 * @throws InvalidInputException 
	 * @throws Exception
	 */
	public Customer signUp(Customer customer) throws InvalidInputException, ResourceExistsException  {
		
		//Validating customer details
		CustomerValidation.customerValidation1(customer);
		
		//Checking the contact number and email already exists in db
		if(customerRepository.getByContactNumber(customer.getContactNumber())!=null){
			throw new ResourceExistsException("This Contact number already exists!!!");	
		}
		
		if(customerRepository.getByEmail(customer.getEmail())!=null){
			throw new ResourceExistsException("This Email already exists!!!");	
		}
		
		//Extract user object from customer
				User user=customer.getUser();
		
		
		// Set remaining properties
		user.setRole(Role.CUSTOMER);
		
		
		//set the saved user with id to customer
		customer.setUser(userService.addUser(user));
		//save customer
		return customerRepository.save(customer);
	}
	
	
	
	
	
	
	
	/**AIM : To add additional customer details like aadhar number, PAN card number .
	 * @param customer
	 * @param principal
	 * @return
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public Customer addAdditionalDetails(Customer customer, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Check customer is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		Customer currentCustomer=customerRepository.getByUserId(currentUser.getId());
	
		//Validate and set aadhar number
		if(customer.getAadharNumber()!=null) {
			CustomerValidation.validateAadharNumber(customer.getAadharNumber());
			currentCustomer.setAadharNumber(customer.getAadharNumber());
		}
		
		//Validate and set pan card number
		if(customer.getPanCardNumber()!=null) {
			CustomerValidation.validatePanCardNumber(customer.getPanCardNumber());
			currentCustomer.setPanCardNumber(customer.getPanCardNumber());
		}
		
		//Save updated customer
		return customerRepository.save(currentCustomer);
	}




//---------------------------------------------- GET -----------------------------------------------

	/**Fetch all customers 
	 * 
	 * @param principal 
	 * @return
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public List<Customer> getAllCustomer(int page, int size,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		Pageable pageable =PageRequest.of(page, size);
		return customerRepository.findAll(pageable).getContent();
	}




	public Customer getCustomerById(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		//get by id or throw exception
		return customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No customer record with given id...!!!"));
	}




	/**Gets the details of current customer
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 */
	public Customer getCurrentCustomer(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		//return the customer
		return customerRepository.getByUserId(currentUser.getId());
	}




	public Customer getCustomerByUserid(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		//Fetch the customer		
		Customer currentCustomer=customerRepository.getByUserId(id);
		//
		if(currentCustomer==null) {
			throw new ResourceNotFoundException("No customer record with the given user id...!!!");
		}
		//return the customer
		return currentCustomer;
	}


//-------------------------------------------- UPDATE -------------------------------------------------------------------

	public Customer updateCustomer(Customer customer, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		//Get old customer details
		Customer currentCustomer = customerRepository.getByUserId(currentUser.getId());
		
		//Validate and update
		if(customer.getName()!=null) {
			ActorValidation.validateName(customer.getName());
			currentCustomer.setName(customer.getName());
		}
		if(customer.getDob()!=null) {
			ActorValidation.validateDob(customer.getDob());
			currentCustomer.setDob(customer.getDob());
		}
		if(customer.getGender()!=null) {
			ActorValidation.validateGender(customer.getGender());
			currentCustomer.setGender(customer.getGender());
		}
		if(customer.getEmail()!=null) {
			ActorValidation.validateEmail(customer.getEmail());
			currentCustomer.setEmail(customer.getEmail());
		}
		if(customer.getContactNumber()!=null) {
			ActorValidation.validateContactNumber(customer.getContactNumber());
			currentCustomer.setContactNumber(customer.getContactNumber());
		}
		if(customer.getAddress()!=null) {
			ActorValidation.validateAddress(customer.getAddress());
			currentCustomer.setAddress(customer.getAddress());
		}
		
		
		
		
		//Validate and set aadhar number
		if(customer.getAadharNumber()!=null) {
			CustomerValidation.validateAadharNumber(customer.getAadharNumber());
			currentCustomer.setAadharNumber(customer.getAadharNumber());
		}
				
		//Validate and set pan card number
		if(customer.getPanCardNumber()!=null) {
			CustomerValidation.validatePanCardNumber(customer.getPanCardNumber());
			currentCustomer.setPanCardNumber(customer.getPanCardNumber());
		}		
		
		return customerRepository.save(currentCustomer);
	}



	
	
	
	

	

}
