package com.maverickbank.MaverickBank.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.model.users.Employee;
import com.maverickbank.MaverickBank.repository.CustomerRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.CustomerValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;
import com.maverickbank.MaverickBank.validation.ActorValidation;

@Service
public class CustomerService {
	CustomerRepository cr;
	UserRepository ur;
	PasswordEncoder pe;
	
	public CustomerService(CustomerRepository cr, UserRepository ur, PasswordEncoder pe) {
		this.cr=cr;
		this.ur=ur;
		this.pe=pe;
	}

	
	
	
	
	
	/** Add Customer() is a method where we are validating the details of a customer and saves into database
	 * Step 1:Check weather given customer object is null
	 * Step 2: Check weather username already exists or not, if exists throw resource exists exception
	 * Step 3: Validate customer details using validation classes 
	 * Step 4: As contact number and email are unique check weather they exists, if exists throw resource exists exception
	 * Step 5: Encode the password using password encoder
	 * Step 6: set status to active as we are creating it now
	 * Step 7: save the user and set the returned user object to customer object
	 * Step 8: Save customer in the db
	 * @param customer
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceExistsException
	 * @throws Exception
	 */
	public Customer signUp(Customer customer) throws InvalidInputException, ResourceExistsException, Exception {
		if(customer==null) {
			throw new InvalidInputException("Customer object is null...!!!");
		}
		
		//Validating customer details
		CustomerValidation.customerValidation1(customer);
		//Checking user is null or not
		ActorValidation.validateUser(customer.getUser());
		//Extract user object from customer
		User user=customer.getUser();
		//Validate username
		UserValidation.validateUsername(user.getUsername());
		//Checking username exists in db
		if(ur.getByUsername(user.getUsername())!=null){
			throw new ResourceExistsException("This username already exists!!!");
			
		}
		//Validating for strong password
		UserValidation.validatePassword(user.getPassword());
		
		//Checking the contact number and email already exists in db
		if(cr.getByContactNumber(customer.getContactNumber())!=null){
			throw new ResourceExistsException("This Contact number already exists!!!");	
		}
		
		if(cr.getByEmail(customer.getEmail())!=null){
			throw new ResourceExistsException("This Email already exists!!!");	
		}
		// Set remaining properties
		user.setRole(Role.CUSTOMER);
		String password = pe.encode(user.getPassword());
		user.setPassword(password);
		customer.setStatus(ActiveStatus.ACTIVE);
		//set the saved user with id to customer
		customer.setUser(ur.save(user));
		//save customer
		return cr.save(customer);
	}






	/**Fetch all customers
	 * @return
	 */
	public List<Customer> getAllCustomer() throws Exception {
		return cr.findAll();
	}

}
