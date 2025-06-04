package com.maverickbank.MaverickBank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.model.users.Employee;
import com.maverickbank.MaverickBank.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
	@Autowired
	CustomerService cs ;
	
	
	/**AIM : To add a customer.
	 * PATH : /api/customer/add
	 * INPUT : customer object
	 * RETURNS: customer object
	 * @param customer
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/signup")
	public Customer signUp( @RequestBody Customer customer) throws InvalidInputException,ResourceExistsException, Exception {
		return cs.signUp(customer);
		
	}
	
	
	@GetMapping("/get-all")
	public List<Customer> getAllCustomer() throws Exception{
		return cs.getAllCustomer();
	}
}
