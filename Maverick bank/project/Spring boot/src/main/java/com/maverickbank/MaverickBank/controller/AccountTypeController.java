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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.service.AccountTypeService;

@RestController
@RequestMapping("/api/account-type")
public class AccountTypeController {
	@Autowired
	private AccountTypeService accountTypeService;
	
	//----------------------------------------------- POST ---------------------------------------------------------------
	
	@PostMapping("/add")
	public AccountType addAccountType(AccountType accountType,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceExistsException {
		return accountTypeService.addAccountType(accountType,principal);
	}
	
	
	//----------------------------------------------- GET ----------------------------------------------------------------
	@GetMapping("/get/all")
	public List<AccountType> getAllAccountType(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
											   @RequestParam(name="size",required = false, defaultValue = "100000") Integer size,
											   Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException{
		return accountTypeService.getAllAccountType(page, size,principal);
	}
	
	@GetMapping("/get/by-id/{id}")
	public AccountType getAccountTypeById(@PathVariable int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return accountTypeService.getAccountTypeById(id,principal);
	}
	
	@GetMapping("/get/by-name/{type}")
	public AccountType getAccountTypeByName(@PathVariable BankAccountType type, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException{
		return accountTypeService.getAccountTypeByName(type,principal);
	}

	
	//------------------------------------------------ PUT ---------------------------------------------------------------
	
	@PutMapping("/update")
	public AccountType updateAccountType(@RequestBody AccountType accountType, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return accountTypeService.updateAccountType(accountType,principal);
	}
}

