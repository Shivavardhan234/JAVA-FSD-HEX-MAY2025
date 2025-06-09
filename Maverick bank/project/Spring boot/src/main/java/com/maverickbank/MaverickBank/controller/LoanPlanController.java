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

import com.maverickbank.MaverickBank.enums.LoanType;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.LoanPlan;
import com.maverickbank.MaverickBank.service.LoanPlanService;

@RestController
@RequestMapping("/api/loan-plan")
public class LoanPlanController {
	@Autowired
	private LoanPlanService loanPlanService;
	
//----------------------------------------- POST -------------------------------------------------------------------------	
	
	@PostMapping("/add")
	public LoanPlan createLoanPlan(@RequestBody LoanPlan loanPlan, Principal principal) throws Exception {
	    return loanPlanService.createLoanPlan(loanPlan, principal);
	}

//------------------------------------------ GET ------------------------------------------------------------------------
	
	@GetMapping("/get/all")
	public List<LoanPlan> getAllLoanPlans(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanPlanService.getAllLoanPlans(principal);
	}

	
	@GetMapping("/get/by-id/{id}")
	public LoanPlan getLoanPlanById(@PathVariable int id, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanPlanService.getLoanPlanById(id, principal);
	}

	
	@GetMapping("/get/by-loan-type/{loanType}")
	public List<LoanPlan> getLoanPlansByType(@PathVariable LoanType loanType, Principal principal)
	        throws DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanPlanService.getLoanPlansByType(loanType, principal);
	}

	
	
//-------------------------------------------- PUT ---------------------------------------------------------------------
	
	@PutMapping("/update")
	public LoanPlan updateLoanPlan(@RequestBody LoanPlan loanPlan,
	                               Principal principal) throws Exception {
	    return loanPlanService.updateLoanPlan(loanPlan, principal);
	}

	
	
	
	
	
	
}
