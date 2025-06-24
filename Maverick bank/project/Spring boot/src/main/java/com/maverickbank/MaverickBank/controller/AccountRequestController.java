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
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.RequestType;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountRequest;
import com.maverickbank.MaverickBank.service.AccountRequestService;

@RestController
@RequestMapping("/api/account-request")
public class AccountRequestController {
	
	
	@Autowired
	private AccountRequestService accountRequestService;
	
	
//------------------------------------------------------- POST -----------------------------------------------------------
	 @PostMapping("/add/{accountId}/{requestType}/{purpose}")
	 @CrossOrigin(origins = "http://localhost:5173")
	    public AccountRequest createAccountRequest(@PathVariable int accountId, @PathVariable RequestType requestType,@PathVariable String purpose,Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {
	        return accountRequestService.createRequest(accountId, requestType,purpose, principal);
	    }
	
//--------------------------------------------------------- GET ----------------------------------------------------------
	 @GetMapping("/get/all")
	 public List<AccountRequest> getAllRequests(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	     return accountRequestService.getAllRequests(principal);
	 }

	 
	 @GetMapping("/get/by-id/{id}")
	 public AccountRequest getRequestById(@PathVariable int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException  {
	     return accountRequestService.getRequestById(id, principal);
	 }
	 
	 @GetMapping("/get/by-account-id/{accountId}")
	 public List<AccountRequest> getRequestsByAccountId(@PathVariable int accountId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException  {
	     return accountRequestService.getRequestsByAccountId(accountId, principal);
	 }

	 @GetMapping("/get/by-status/{status}")
	 public List<AccountRequest> getRequestsByStatus(@PathVariable ApplicationStatus status, Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	     return accountRequestService.getRequestsByStatus(status, principal);
	 }
	 
	 
//---------------------------------------------------- PUT ---------------------------------------------------------------

	 
	 
	 @PutMapping("/update/accept/{requestId}")
	 public AccountRequest acceptRequest(@PathVariable int requestId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {
	     return accountRequestService.acceptRequest(requestId, principal);
	 }

	 @PutMapping("/update/reject/{requestId}")
	 public AccountRequest rejectRequest(@PathVariable int requestId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException  {
	     return accountRequestService.rejectRequest(requestId, principal);
	 }

}
