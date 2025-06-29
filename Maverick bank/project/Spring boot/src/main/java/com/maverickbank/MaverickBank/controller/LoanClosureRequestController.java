package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@PostMapping("/add/{loanId}")
	@CrossOrigin(origins = "http://localhost:5173")
	public LoanClosureRequest createLoanClosureRequest(@PathVariable int loanId, @RequestParam String purpose,Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {

	    return loanClosureRequestService.createLoanClosureRequest(loanId, purpose, principal);
	}
	
//----------------------------------------------------- GET --------------------------------------------------------------
	 @GetMapping("/get/all")
	 @CrossOrigin(origins = "http://localhost:5173")
	    public List<LoanClosureRequest> getAllRequests(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
														@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	        return loanClosureRequestService.getAllRequests(page,size,principal);
	    }

	    @GetMapping("/get/by-id/{id}")
	    @CrossOrigin(origins = "http://localhost:5173")
	    public LoanClosureRequest getRequestById(@PathVariable int id,Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return loanClosureRequestService.getRequestById(id,principal);
	    }

	    @GetMapping("/get/by-loan-id/{loanId}")
	    @CrossOrigin(origins = "http://localhost:5173")
	    public List<LoanClosureRequest> getByLoanId(@PathVariable int loanId,Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return loanClosureRequestService.getByLoanId(loanId,principal);
	    }
	    
	    @GetMapping("/get/by-status/{status}")
	    @CrossOrigin(origins = "http://localhost:5173")
	    public List<LoanClosureRequest> getByStatus(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
													@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@PathVariable ApplicationStatus status, Principal principal)
	            throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
	        return loanClosureRequestService.getByStatus(page,size,status, principal);
	    }

	    @GetMapping("/get/by-loan-id-status/{loanId}/{status}")
	    @CrossOrigin(origins = "http://localhost:5173")
	    public List<LoanClosureRequest> getByLoanIdAndStatus(@PathVariable int loanId,@PathVariable ApplicationStatus status,Principal principal)
	            throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	        return loanClosureRequestService.getByLoanIdAndStatus(loanId, status, principal);
	    }
	    
	    
//------------------------------------------------ PUT -------------------------------------------------------------------
	    @PutMapping("/update/reject/{id}")
	    @CrossOrigin(origins = "http://localhost:5173")
	    public LoanClosureRequest rejectLoanClosureRequest(@PathVariable int id, Principal principal)
	            throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {
	        return loanClosureRequestService.rejectLoanClosureRequest(id, principal);
	    }
	    
	    @PutMapping("/update/accept/{id}")
	    @CrossOrigin(origins = "http://localhost:5173")
	    public LoanClosureRequest acceptLoanClosureRequest(@PathVariable int id, Principal principal)
	            throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {
	        return loanClosureRequestService.acceptLoanClosureRequest(id, principal);
	    }



}
