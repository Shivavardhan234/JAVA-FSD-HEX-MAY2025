package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.LoanOpeningApplication;
import com.maverickbank.MaverickBank.service.LoanOpeningApplicationService;

@RestController
@RequestMapping("/api/loan-opening-application")
public class LoanOpeningApplicationController {
	
	
	@Autowired
	private LoanOpeningApplicationService loanOpeningApplicationService;
	
//-------------------------------------------------- POST ----------------------------------------------------------------
	
	@PostMapping("/add/{accountId}/{loanPlanId}")
	public LoanOpeningApplication applyForLoan(@PathVariable int accountId,@PathVariable int loanPlanId,@PathVariable String purpose,Principal principal) throws ResourceNotFoundException, InvalidActionException, DeletedUserException, InvalidInputException {
	    return loanOpeningApplicationService.createLoanApplication(accountId, loanPlanId, purpose, principal);
	}
//--------------------------------------------------- GET ----------------------------------------------------------------
	@GetMapping("/get/all")
	public List<LoanOpeningApplication> getAllLoanApplications(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanOpeningApplicationService.getAllLoanApplications(principal);
	}
	
	
	
	@GetMapping("/get/by-id/{id}")
	public LoanOpeningApplication getLoanApplicationById(@PathVariable int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanOpeningApplicationService.getById(id, principal);
	}

	@GetMapping("/get/by-account-id/{accountId}")
	public List<LoanOpeningApplication> getLoanApplicationsByAccount(@PathVariable int accountId, Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanOpeningApplicationService.getByAccountId(accountId, principal);
	}
	
	
	@GetMapping("/get/by-loan-plan-id/{loanPlanId}")
	public List<LoanOpeningApplication> getLoanApplicationsByLoanPlan(@PathVariable int loanPlanId, Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanOpeningApplicationService.getByLoanPlanId(loanPlanId, principal);
	}
	
	
	@GetMapping("/get/by-account-id-status/{accountId}/{status}")
	public List<LoanOpeningApplication> getLoanApplicationsByAccountAndStatus(@PathVariable int accountId,@PathVariable ApplicationStatus status,Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanOpeningApplicationService.getByAccountIdAndStatus(accountId, status, principal);
	}
	
	@GetMapping("/get/by-status/{status}")
	public List<LoanOpeningApplication> getLoanApplicationsByStatus(@PathVariable ApplicationStatus status,Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanOpeningApplicationService.getLoanApplicationsByStatus( status, principal);
	}
	

	
	//--------------------------------------------- PUT ------------------------------------------------------------------
	@PutMapping("/update/accept/{id}")
	public Loan acceptLoanOpeningApplication(@PathVariable int id, Principal principal) throws Exception {
	    return loanOpeningApplicationService.acceptLoanOpeningApplication(id, principal);
	}
	@PutMapping("/update/reject/{id}")
	public LoanOpeningApplication rejectLoanOpeningApplication(@PathVariable int id, Principal principal) throws Exception {
	    return loanOpeningApplicationService.rejectLoanOpeningApplication(id, principal);
	}

}
