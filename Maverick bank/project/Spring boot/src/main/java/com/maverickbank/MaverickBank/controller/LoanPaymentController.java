package com.maverickbank.MaverickBank.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.LoanPayment;
import com.maverickbank.MaverickBank.service.LoanPaymentService;

@RestController
@RequestMapping("/api/loan-payment")
public class LoanPaymentController {
	
	@Autowired
	private LoanPaymentService loanPaymentService;
	
	
//--------------------------------------------------- POST ---------------------------------------------------------------
	
	 @PostMapping("/add/{loanId}/{amountPaid}")
    public LoanPayment addLoanPayment(@PathVariable int loanId,@PathVariable BigDecimal amountPaid,Principal principal) throws ResourceNotFoundException, InvalidInputException, DeletedUserException, InvalidActionException {

        return loanPaymentService.addLoanPayment(loanId, amountPaid, principal);
    }
	 
//------------------------------------------------- GET ------------------------------------------------------------------
	 @GetMapping("/get/all")
	    public List<LoanPayment> getAllLoanPayments(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	        return loanPaymentService.getAllLoanPayments(principal);
	    }

	    @GetMapping("/get/by-id/{id}")
	    public LoanPayment getLoanPaymentById(@PathVariable int id, Principal principal)
	            throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return loanPaymentService.getLoanPaymentById(id, principal);
	    }

	    @GetMapping("/get/by-loan-id/{loanId}")
	    public List<LoanPayment> getPaymentsByLoanId(@PathVariable int loanId, Principal principal)
	            throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
	        return loanPaymentService.getPaymentsByLoanId(loanId, principal);
	    }
	    
	    
	    
	

}
