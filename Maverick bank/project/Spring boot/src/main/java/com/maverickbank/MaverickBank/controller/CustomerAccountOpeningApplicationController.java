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

import com.maverickbank.MaverickBank.dto.CustomerAccountOpeningInputDto;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.IdentityNotMatchException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.CustomerAccountOpeningApplication;
import com.maverickbank.MaverickBank.service.CustomerAccountOpeningApplicationService;


@RestController
@RequestMapping("/api/customer-account-opening-application")
public class CustomerAccountOpeningApplicationController {
	
	
	@Autowired
	private CustomerAccountOpeningApplicationService customerAccountOpeningApplicationService;
	
	
	
//---------------------------------------- POST -------------------------------------------------------------------------
	@PostMapping("/add")
	public List<CustomerAccountOpeningApplication> addcustomerAccountOpeningApplication(CustomerAccountOpeningInputDto customerAccountOpeningInputDto,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException, IdentityNotMatchException{
		return customerAccountOpeningApplicationService.addcustomerAccountOpeningApplication(customerAccountOpeningInputDto,principal);
	}
	
//------------------------------------------- GET ------------------------------------------------------------------------
	
	
	@GetMapping("/get/all")
    public List<CustomerAccountOpeningApplication> getAllApplications(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
        return customerAccountOpeningApplicationService.getAllApplications(principal);
    }

    @GetMapping("/get/by-id/{id}")
    public CustomerAccountOpeningApplication getById(@PathVariable int id,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
        return customerAccountOpeningApplicationService.getById(id,principal);
    }

    @GetMapping("/get/by-customer-id/{customerId}")
    public List<CustomerAccountOpeningApplication> getByCustomerId(@PathVariable int customerId,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
        return customerAccountOpeningApplicationService.getByCustomerId(customerId,principal);
    }

    @GetMapping("/get/by-account-holder-id/{accountHolderId}")
    public List<CustomerAccountOpeningApplication> getByAccountHolderId(@PathVariable int accountHolderId,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
        return customerAccountOpeningApplicationService.getByAccountHolderId(accountHolderId,principal);
    }

    @GetMapping("/get/by-application-id/{applicationId}")
    public List<CustomerAccountOpeningApplication> getByAccountOpeningApplicationId(@PathVariable int applicationId,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
        return customerAccountOpeningApplicationService.getByAccountOpeningApplicationId(applicationId,principal);
    }
    
    
    //-------------------------------------------- PUT -------------------------------------------------------------------
    @PutMapping("/update")
    public CustomerAccountOpeningApplication updateCustomerAccountOpeningApplication(@RequestBody CustomerAccountOpeningApplication applicationUpdate,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {

        return customerAccountOpeningApplicationService.updateCustomerAccountOpeningApplication(applicationUpdate, principal);
    }
    
    @PutMapping("/update/approval/{id}")
    public CustomerAccountOpeningApplication approveApplication(@PathVariable int id, Principal principal) throws ResourceNotFoundException, InvalidActionException, DeletedUserException, InvalidInputException {

        return customerAccountOpeningApplicationService.approveApplication(id, principal);
    }

    
}
