package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.CustomerAccount;
import com.maverickbank.MaverickBank.service.CustomerAccountService;

@RestController
@RequestMapping("/api/customer-account")
public class CustomerAccountController {
	
	@Autowired
	private CustomerAccountService customerAccountService;
	
//----------------------------------------------- POST -------------------------------------------------------------------
	  @PostMapping("/add/{applicationId}")
	    public CustomerAccount createCustomerAccount(@PathVariable int applicationId,@RequestBody Account account, Principal principal) 
	            throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return customerAccountService.createCustomerAccount(applicationId,account, principal);
	    }
	  
	  
//------------------------------------------------ GET -------------------------------------------------------------------
	  @GetMapping("/get/all")
	    public List<CustomerAccount> getAllCustomerAccounts(Principal principal) throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
	        return customerAccountService.getAllCustomerAccounts(principal);
	    }

	    @GetMapping("/get/by-customer-id/{customerId}")
	    public List<CustomerAccount> getByCustomerId(@PathVariable int customerId, Principal principal)
	            throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return customerAccountService.getByCustomerId(customerId, principal);
	    }

	    @GetMapping("/get/by-accountholder-id/{accountHolderId}")
	    public List<CustomerAccount> getByAccountHolderId(@PathVariable int accountHolderId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return customerAccountService.getByAccountHolderId(accountHolderId, principal);
	    }

	    @GetMapping("/get/by-account-id/{accountId}")
	    public List<CustomerAccount> getByAccountId(@PathVariable int accountId, Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return customerAccountService.getByAccountId(accountId, principal);
	    }
	    @GetMapping("/get/by-customer-account/{customerId}/{accountId}")
	    public CustomerAccount getByCustomerIdAndAccountId(@PathVariable int customerId,@PathVariable int accountId, Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return customerAccountService.getByCustomerIdAndAccountId(customerId,accountId, principal);
	    }
	    
	    

	    @GetMapping("/get/by-id/{id}")
	    public CustomerAccount getById(@PathVariable int id, Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return customerAccountService.getById(id, principal);
	    }
	    
//-------------------------------------- PUT -----------------------------------------------------------------------------
	    @PutMapping("/update/account-holder/{id}")
	    public CustomerAccount updateAccountHolder(@PathVariable int customerAccountId,@RequestBody AccountHolder accountHolder,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
	    	return customerAccountService.updateAccountHolder(customerAccountId,accountHolder,principal);
	    }
}
