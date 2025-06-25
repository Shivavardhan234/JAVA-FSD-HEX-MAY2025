package com.maverickbank.MaverickBank.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.enums.LoanStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.service.LoanService;

@RestController
@RequestMapping("/api/loan")
public class LoanController {
	
	
	@Autowired
	private LoanService loanService;
	
	
//----------------------------------------------- POST ------------------------------------------------------------------	
	@PostMapping("/add/{loanApplicationId}")
	public Loan createLoan(@PathVariable int loanApplicationId, Principal principal) throws ResourceNotFoundException, InvalidActionException, DeletedUserException, InvalidInputException {
	    return loanService.createLoan(loanApplicationId, principal);
	}
	
//------------------------------------------------ GET --------------------------------------------------------------------
	@GetMapping("/get/all")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Loan> getAllLoans(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanService.getAllLoans(principal);
	}

	
	
	@GetMapping("/get/by-id/{loanId}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Loan getLoanById(@PathVariable int loanId, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanService.getLoanById(loanId, principal);
	}

	
	
	
	@GetMapping("/get/by-account-id/{accountId}")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Loan> getLoansByAccountId(@PathVariable int accountId, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanService.getLoansByAccountId(accountId, principal);
	}

	@GetMapping("/get/by-status/{status}")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Loan> getLoansByStatus(@PathVariable LoanStatus status, Principal principal)
	        throws DeletedUserException, InvalidInputException, InvalidActionException, ResourceNotFoundException {
	    return loanService.getLoansByStatus(status, principal);
	}
	
	

	@GetMapping("/get/by-account-id-status/{accountId}/{status}")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Loan> getLoansByAccountIdAndStatus(@PathVariable int accountId,@PathVariable LoanStatus status, Principal principal)
	        throws DeletedUserException, InvalidInputException, InvalidActionException, ResourceNotFoundException {
	    return loanService.getLoansByAccountIdAndStatus(accountId,status, principal);
	}
	
	
//------------------------------------------ PUT ------------------------------------------------------------------------

	@PutMapping("/update/close/{loanId}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Loan closeLoan(@PathVariable int loanId, Principal principal) throws DeletedUserException, ResourceNotFoundException, InvalidActionException, InvalidInputException  {
	    return loanService.closeLoan(loanId, principal);
	}

	@PutMapping("/update/penalty/{loanId}/{penalty}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Loan updatePenalty(@PathVariable int loanId,@PathVariable BigDecimal penalty,Principal principal) throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
	    return loanService.updateTotalPenalty(loanId, penalty, principal);
	}

	@PutMapping("/update/due-date/{loanId}/{dueDate}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Loan updateDueDate(@PathVariable int loanId,@PathVariable LocalDate dueDate,Principal principal) throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException  {
	    return loanService.updateDueDate(loanId, dueDate, principal);
	}
	
	
	@PutMapping("/update/is-cleared/{loanId}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Loan updateIsCleared(@PathVariable int loanId,Principal principal ) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return loanService.updateIsCleared(loanId, principal);
	}
	


}
