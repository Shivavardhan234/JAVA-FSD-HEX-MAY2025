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
	 @PostMapping("/add/{accountId}/{requestType}")
	 @CrossOrigin(origins = "http://localhost:5173")
	    public AccountRequest createAccountRequest(@PathVariable int accountId, @PathVariable RequestType requestType,@RequestParam String purpose,Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {
	        return accountRequestService.createRequest(accountId, requestType,purpose, principal);
	    }
	
//--------------------------------------------------------- GET ----------------------------------------------------------
	 @GetMapping("/get/all")
	 @CrossOrigin(origins = "http://localhost:5173")
	 public List<AccountRequest> getAllRequests(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
				@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	     return accountRequestService.getAllRequests(page,size,principal);
	 }

	 
	 @GetMapping("/get/by-id/{id}")
	 @CrossOrigin(origins = "http://localhost:5173")
	 public AccountRequest getRequestById(@PathVariable int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException  {
	     return accountRequestService.getRequestById(id, principal);
	 }
	 
	 @GetMapping("/get/by-account-id/{accountId}")
	 @CrossOrigin(origins = "http://localhost:5173")
	 public List<AccountRequest> getRequestsByAccountId(@PathVariable int accountId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException  {
	     return accountRequestService.getRequestsByAccountId(accountId, principal);
	 }

	 @GetMapping("/get/by-status/{status}")
	 @CrossOrigin(origins = "http://localhost:5173")
	 public List<AccountRequest> getRequestsByStatus(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
				@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@PathVariable ApplicationStatus status, Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	     return accountRequestService.getRequestsByStatus(page,size,status, principal);
	 }
	 
	 
//---------------------------------------------------- PUT ---------------------------------------------------------------

	 
	 
	 @PutMapping("/update/accept/{requestId}")
	 @CrossOrigin(origins = "http://localhost:5173")
	 public AccountRequest acceptRequest(@PathVariable int requestId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {
	     return accountRequestService.acceptRequest(requestId, principal);
	 }

	 @PutMapping("/update/reject/{requestId}")
	 @CrossOrigin(origins = "http://localhost:5173")
	 public AccountRequest rejectRequest(@PathVariable int requestId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException  {
	     return accountRequestService.rejectRequest(requestId, principal);
	 }

}
