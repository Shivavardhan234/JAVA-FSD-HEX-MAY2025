package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.service.AccountOpeningApplicationService;

@RestController
@RequestMapping("/api/account-opening-application")
public class AccountOpeningApplicationController {
	@Autowired
	AccountOpeningApplicationService accountOpeningApplicationService;
	
	
	
//-------------------------------------------- POST ----------------------------------------------------------------------	
	@PostMapping("/add")
	public AccountOpeningApplication addAccountOpeningApplication(@RequestBody AccountOpeningApplication accountOpeningApplication, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
		
		return accountOpeningApplicationService.addAccountOpeningApplication(accountOpeningApplication,principal);
	}
	
//--------------------------------------------- GET ----------------------------------------------------------------------
	@GetMapping("/get/all")
	public List<AccountOpeningApplication> getAllAccountOpeningApplication(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException{
		return accountOpeningApplicationService.getAllAccountOpeningApplication(principal);
		
	}
	
	@GetMapping("/get/by-id/{id}")
	public AccountOpeningApplication getAccountOpeningApplicationById(@PathVariable int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return accountOpeningApplicationService.getAccountOpeningApplicationById(id,principal);
		
	}
	
	@GetMapping("/get/by-branch")
	public List<AccountOpeningApplication> getAccountOpeningApplicationByBranch(@RequestBody Branch branch,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return accountOpeningApplicationService.getAccountOpeningApplicationByBranch(branch,principal);
		
	}
	
	@GetMapping("/get/by-account-type")
	public List<AccountOpeningApplication> getAccountOpeningApplicationByAccountType(@RequestBody AccountType accountType,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return accountOpeningApplicationService.getAccountOpeningApplicationByAccountType(accountType,principal);
		
	}
	
	@GetMapping("/get/by-customer-approval-status/{customerApprovalStatus}")
	public List<AccountOpeningApplication> getAccountOpeningApplicationByCustomerApprovalStatus(@PathVariable ApplicationStatus customerApprovalStatus, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return accountOpeningApplicationService.getAccountOpeningApplicationByCustomerApprovalStatus(customerApprovalStatus,principal);
		
	}
	
	@GetMapping("/get/by-employee-approval-status/{employeeApprovalStatus}")
	public List<AccountOpeningApplication> getAccountOpeningApplicationByEmployeeApprovalStatus(@PathVariable ApplicationStatus employeeApprovalStatus,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return accountOpeningApplicationService.getAccountOpeningApplicationByEmployeeApprovalStatus(employeeApprovalStatus,principal);
		
	}
	
	
	@GetMapping("/get/by-date/{date}")
	public List<AccountOpeningApplication> getAccountOpeningApplicationByDate(@PathVariable LocalDate date,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return accountOpeningApplicationService.getAccountOpeningApplicationByDate(date,principal);
		
	}
	
	
//--------------------------------------------- PUT ----------------------------------------------------------------------
	
	@PutMapping("/update/customer-approval/{id}")
	public  AccountOpeningApplication updateCustomerApprovalStatus(@PathVariable int id,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		return accountOpeningApplicationService.updateCustomerApprovalStatus(id,principal);
	}
	
	@PutMapping("/update/approve/{id}")
	public  AccountOpeningApplication approveApplication(@PathVariable int id,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		return accountOpeningApplicationService.approveApplication(id,principal);
	}
	
	
	@PutMapping("/update/reject/{id}")
	public  AccountOpeningApplication rejectApplication(@PathVariable int id,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		return accountOpeningApplicationService.rejectApplication(id,principal);
	}
	

}
