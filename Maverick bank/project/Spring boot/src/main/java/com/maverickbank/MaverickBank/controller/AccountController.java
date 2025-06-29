package com.maverickbank.MaverickBank.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.enums.AccountStatus;
import com.maverickbank.MaverickBank.enums.PaymentMedium;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.NotEnoughFundsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.service.AccountService;

@RestController
@RequestMapping("/api/account")
public class AccountController {

	
	@Autowired
	private AccountService accountService;
	
//------------------------------------------------ POST ------------------------------------------------------------------
	@PostMapping("/add")
	public Account createAccount(@RequestBody Account account, Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.createAccount(account, principal);
	}
	
//-------------------------------------------------- GET -----------------------------------------------------------------
	@GetMapping("/get/all")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Account> getAllAccounts(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException, ResourceNotFoundException {
	    return accountService.getAllAccounts(page,size,principal);
	}

	@GetMapping("/get/by-id/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Account getAccountById(@PathVariable int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.getAccountById(id, principal);
	}

	@GetMapping("/get/by-account-number")
	@CrossOrigin(origins = "http://localhost:5173")
	public Account getAccountByAccountNumber(@RequestParam String accountNumber, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.getAccountByAccountNumber(accountNumber, principal);
	}

	@GetMapping("/get/by-branch-id/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Account> getAccountsByBranchId(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@PathVariable int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.getAccountsByBranchId(page,size,id, principal);
	}

	@GetMapping("/get/by-account-status/{accountStatus}")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Account> getAccountsByStatus(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@PathVariable AccountStatus accountStatus, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.getAccountsByStatus(page,size,accountStatus, principal);
	}

	
	@GetMapping("/get/by-account-type-id/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Account> getAccountsByAccountTypeId(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@PathVariable int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.getAccountsByAccountTypeId(page,size,id, principal);
	}
	
	
//-------------------------------------------------- PUT -----------------------------------------------------------------

	
	@PutMapping("/update/name/{accountId}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Account updateAccountName(@PathVariable int accountId,@RequestParam String newName,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
	    return accountService.updateAccountName(accountId, newName, principal);
	}
	

	@PutMapping("/update/status/{accountId}/{status}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Account updateAccountStatus(@PathVariable int accountId,@PathVariable AccountStatus status,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
	    return accountService.updateAccountStatus(accountId, status, principal);
	}
	
	@PutMapping("/update/kyc/{accountId}/{kyc}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Account updateKyc(@PathVariable int accountId,@PathVariable boolean kyc,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
	    return accountService.updateKyc(accountId,kyc, principal);
	}


	
	
	
	@PutMapping("/update/withdraw/{accountId}/{amount}/{medium}")
	@CrossOrigin(origins = "http://localhost:5173")
    public Account withdraw(@PathVariable int accountId,@PathVariable BigDecimal amount,@PathVariable PaymentMedium medium,@RequestParam String pin,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException, NotEnoughFundsException {
        return accountService.withdraw(accountId, amount, medium, pin,principal);
    }

    @PutMapping("/update/deposit/{accountId}/{amount}/{medium}")
    @CrossOrigin(origins = "http://localhost:5173")
    public Account deposit(@PathVariable int accountId,@PathVariable BigDecimal amount,@PathVariable PaymentMedium medium,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
        return accountService.deposit(accountId, amount, medium, principal);
    }

    @PutMapping("/update/transfer/{fromAccountId}/{amount}")
    @CrossOrigin(origins = "http://localhost:5173")
    public Account transfer(@PathVariable int fromAccountId, @RequestParam String toAccountNumber,@PathVariable BigDecimal amount,@RequestParam String pin,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException, NotEnoughFundsException {
        return accountService.transfer(fromAccountId, toAccountNumber, amount, pin,principal);
    }
    
    
}
