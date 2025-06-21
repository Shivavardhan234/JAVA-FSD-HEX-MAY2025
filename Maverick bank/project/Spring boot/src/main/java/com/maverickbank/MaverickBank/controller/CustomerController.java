package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService ;
	
	//-------------------------------------- POST --------------------------------------------------
	
	/**AIM : To add a customer.
	 * PATH : /api/customer/add
	 * INPUT : customer object
	 * RETURNS: customer object
	 * @param customer
	 * @return
	 * @throws ResourceExistsException 
	 * @throws InvalidInputException 
	 * @throws Exception 
	 */
	@PostMapping("/signup")
	@CrossOrigin(origins = "http://localhost:5173")
	public Customer signUp( @RequestBody Customer customer) throws InvalidInputException, ResourceExistsException {
		return customerService.signUp(customer);
		
	}
	
	@PostMapping ("/add/additional-details")
	public Customer addAdditionalDetails(@RequestBody Customer customer, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		return customerService.addAdditionalDetails(customer,principal);
	}
	
	
	
	//-------------------------------------- GET ---------------------------------------------------
	
	@GetMapping("/get/all")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Customer> getAllCustomer(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			   @RequestParam(name="size",required = false, defaultValue = "100000") Integer size,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException{
		return customerService.getAllCustomer(page,size,principal);
	}
	
	@GetMapping("/get/by-id")
	public Customer getById(int id,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		return customerService.getCustomerById(id,principal);
	}
	
	
	
	@GetMapping("/get/current")
	public Customer getCurrentCustomer(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		return customerService.getCurrentCustomer(principal);
	}
	
	@GetMapping("/get/by-user-id")
	public Customer getCustomerByUserId(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		return customerService.getCustomerByUserid(id,principal);
	}
	
	//---------------------------------- UPDATE ----------------------------------------------------
	@PutMapping("/update/customer")
	public Customer updateCustomer(Customer customer, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		return customerService.updateCustomer(customer,principal);
	}
	
	
	
}
