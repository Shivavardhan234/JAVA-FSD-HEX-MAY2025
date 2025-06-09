package com.maverickbank.MaverickBank.controller;

import java.math.BigDecimal;
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
	public List<Account> getAllAccounts(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException, ResourceNotFoundException {
	    return accountService.getAllAccounts(principal);
	}

	@GetMapping("/get/by-id/{id}")
	public Account getAccountById(@PathVariable int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.getAccountById(id, principal);
	}

	@GetMapping("/get/by-account-number/{accountNumber}")
	public Account getAccountByAccountNumber(@PathVariable String accountNumber, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.getAccountByAccountNumber(accountNumber, principal);
	}

	@GetMapping("/get/by-branch-id/{id}")
	public List<Account> getAccountsByBranchId(@PathVariable int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.getAccountsByBranchId(id, principal);
	}

	@GetMapping("/get/by-account-status/{accountStatus}")
	public List<Account> getAccountsByStatus(@PathVariable AccountStatus accountStatus, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.getAccountsByStatus(accountStatus, principal);
	}

	
	@GetMapping("/get/by-account-type-id/{id}")
	public List<Account> getAccountsByAccountTypeId(@PathVariable int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    return accountService.getAccountsByAccountTypeId(id, principal);
	}
	
	
//-------------------------------------------------- PUT -----------------------------------------------------------------

	
	@PutMapping("/update/name/{accountId}/{newName}")
	public Account updateAccountName(@PathVariable int accountId,@PathVariable String newName,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
	    return accountService.updateAccountName(accountId, newName, principal);
	}
	

	@PutMapping("/update/status/{accountId}/{status}")
	public Account updateAccountStatus(@PathVariable int accountId,@PathVariable AccountStatus status,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
	    return accountService.updateAccountStatus(accountId, status, principal);
	}
	
	@PutMapping("/update/kyc/{accountId}/{kyc}")
	public Account updateKyc(@PathVariable int accountId,@PathVariable boolean kyc,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
	    return accountService.updateKyc(accountId,kyc, principal);
	}


	
	
	
	@PutMapping("/update/withdraw/{accountId}/{amount}/{medium}/{pin}")
    public Account withdraw(@PathVariable int accountId,@PathVariable BigDecimal amount,@PathVariable PaymentMedium medium,@PathVariable String pin,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException, NotEnoughFundsException {
        return accountService.withdraw(accountId, amount, medium, pin,principal);
    }

    @PutMapping("/update/deposit/{accountId}/{amount}/{medium}")
    public Account deposit(@PathVariable int accountId,@PathVariable BigDecimal amount,@PathVariable PaymentMedium medium,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
        return accountService.deposit(accountId, amount, medium, principal);
    }

    @PutMapping("/update/transfer/{fromAccountId}/{toAccountNumber}/{amount}/{pin}")
    public Account transfer(@PathVariable int fromAccountId, @PathVariable String toAccountNumber,@PathVariable BigDecimal amount,@PathVariable String pin,Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException, NotEnoughFundsException {
        return accountService.transfer(fromAccountId, toAccountNumber, amount, pin,principal);
    }
    
    
}
