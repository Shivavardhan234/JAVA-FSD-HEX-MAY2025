package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.service.AccountHolderService;

@RestController
@RequestMapping("/api/account-holder")
public class AccountHolderController {
	
	private AccountHolderService accountHolderService;
  
 //----------------------------------- POST ------------------------------------------------------------------------------
  @PostMapping("/add")
  public AccountHolder addAccountHolder(AccountHolder accountHolder, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException{
	  return accountHolderService.addAccountHolder(accountHolder, principal);
  }
  
  
  
  
  //--------------------------------------------- GET --------------------------------------------------------------------
  @GetMapping("/get/all")
  public List<AccountHolder> getAllAccountHolder( Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException{
	  return accountHolderService.getAllAccountHolder(principal);
  }
  @GetMapping("/get/by-id/{id}")
  public AccountHolder getAccountHolderById(@PathVariable int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
	  return accountHolderService.getAccountHolderById(id,principal);
  }
  @GetMapping("/get/by-email")
  public List<AccountHolder> getAccountHolderByEmail(@RequestParam String email, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
	  return accountHolderService.getAccountHolderByEmail(email,principal);
  }
  @GetMapping("/get/by-contact")
  public List<AccountHolder> getAccountHolderByContactNumber(@RequestParam String contactNumber, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
	  return accountHolderService.getAccountHolderByContactNumber(contactNumber,principal);
  }
  @GetMapping("/get/by-aadhar")
  public List<AccountHolder> getAccountHolderByAadharNumber(@RequestParam String aadharNumber, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
	  return accountHolderService.getAccountHolderByAadharNumber(aadharNumber,principal);
  }
  @GetMapping("/get/by-pan")
  public List<AccountHolder> getAccountHolderByPanCardNumber(@RequestParam String panCardNumber, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
	  return accountHolderService.getAccountHolderByPanCardNumber(panCardNumber,principal);
  }
  
  
  
  
  //--------------------------------------------- PUT --------------------------------------------------------------------
  
  @PutMapping("/update")
  public AccountHolder updateAccountHolder(AccountHolder accountHolder, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
	  return accountHolderService.updateAccountHolder(accountHolder, principal);
  }

}
