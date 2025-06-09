package com.mockAssessment.Ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mockAssessment.Ecom.enums.Role;
import com.mockAssessment.Ecom.exception.InvalidInputException;
import com.mockAssessment.Ecom.exception.ResourceExistsException;
import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.Customer;
import com.mockAssessment.Ecom.model.User;
import com.mockAssessment.Ecom.repository.CustomerRepository;
import com.mockAssessment.Ecom.validation.CustomerValidation;

@Service
public class CustomerService {
	UserService userService;
	CustomerRepository customerRepository;
	

	public CustomerService(UserService userService, CustomerRepository customerRepository) {
		super();
		this.userService = userService;
		this.customerRepository = customerRepository;
	}



	public Customer addCustomer(Customer customer) throws InvalidInputException,ResourceExistsException, Exception {
		CustomerValidation.validateCustomer(customer);
		User user=customer.getUser();
		user.setRole(Role.CUSTOMER);
		user=userService.addUser(user);
		customer.setUser(user);
		return customerRepository.save(customer);
	}



	public Customer getCustomerById(int id) throws ResourceNotFoundException , Exception{

		return customerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No record with the given id...!!!"));
	}



	public List<Customer> getAllCustomer() {
		
		return customerRepository.findAll();
	}



	public Customer updateCustomer(Customer customer, String username) throws InvalidInputException , Exception{
		if(customer==null) {
			throw new InvalidInputException("Provided customer object is null...!!!");
		}
		Customer previousCustomer=customerRepository.getCustomerByUsername(username);
		if(customer.getName()!=null &&!customer.getName().trim().isEmpty()) {
			previousCustomer.setName(customer.getName());
			
		}
		if(customer.getContact()!=null &&!customer.getContact().trim().isEmpty()) {
			previousCustomer.setContact(customer.getContact());
			
		}
		if(customer.getUser()!=null ) {
			previousCustomer.setUser(userService.updateUser(customer.getUser(), username));
			
		}
		
		return customerRepository.save(previousCustomer);
	}
	
	
	
	

}
