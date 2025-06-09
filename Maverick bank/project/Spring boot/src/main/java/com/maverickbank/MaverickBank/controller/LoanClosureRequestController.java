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
import com.maverickbank.MaverickBank.model.LoanClosureRequest;
import com.maverickbank.MaverickBank.service.LoanClosureRequestService;

@RestController
@RequestMapping("/api/loan-closure")
public class LoanClosureRequestController {
	
	
	@Autowired
	private LoanClosureRequestService loanClosureRequestService;
	
	
//-------------------------------------------------- POST ----------------------------------------------------------------
	
	@PostMapping("/add/{loanId}/{purpose}")
	public LoanClosureRequest createLoanClosureRequest(@PathVariable int loanId, @PathVariable String purpose,Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {

	    return loanClosureRequestService.createLoanClosureRequest(loanId, purpose, principal);
	}
	
//----------------------------------------------------- GET --------------------------------------------------------------
	 @GetMapping("/get/all")
	    public List<LoanClosureRequest> getAllRequests(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	        return loanClosureRequestService.getAllRequests(principal);
	    }

	    @GetMapping("/get/by-id/{id}")
	    public LoanClosureRequest getRequestById(@PathVariable int id,Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return loanClosureRequestService.getRequestById(id,principal);
	    }

	    @GetMapping("/get/by-loan-id/{loanId}")
	    public List<LoanClosureRequest> getByLoanId(@PathVariable int loanId,Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return loanClosureRequestService.getByLoanId(loanId,principal);
	    }
	    
	    @GetMapping("/get/by-status/{status}")
	    public List<LoanClosureRequest> getByStatus(@PathVariable ApplicationStatus status, Principal principal)
	            throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
	        return loanClosureRequestService.getByStatus(status, principal);
	    }

	    @GetMapping("/get/by-loan-id-status/{loanId}/{status}")
	    public List<LoanClosureRequest> getByLoanIdAndStatus(@PathVariable int loanId,@PathVariable ApplicationStatus status,Principal principal)
	            throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return loanClosureRequestService.getByLoanIdAndStatus(loanId, status, principal);
	    }
	    
	    
//------------------------------------------------ PUT -------------------------------------------------------------------
	    @PutMapping("/update/reject/{id}")
	    public LoanClosureRequest rejectLoanClosureRequest(@PathVariable int id, Principal principal)
	            throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {
	        return loanClosureRequestService.rejectLoanClosureRequest(id, principal);
	    }
	    
	    @PutMapping("/update/accept/{id}")
	    public LoanClosureRequest acceptLoanClosureRequest(@PathVariable int id, Principal principal)
	            throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {
	        return loanClosureRequestService.acceptLoanClosureRequest(id, principal);
	    }



}
