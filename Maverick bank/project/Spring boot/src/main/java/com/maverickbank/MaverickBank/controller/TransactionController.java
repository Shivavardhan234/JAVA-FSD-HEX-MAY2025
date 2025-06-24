package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Transaction;
import com.maverickbank.MaverickBank.service.TransactionService;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
	@Autowired
	private TransactionService transactionService;
	
	
//------------------------------------------ POST -----------------------------------------------------------------------
	@PostMapping("/add")
	public Transaction createTransaction(@RequestBody Transaction transaction, Principal principal)
	        throws InvalidInputException, DeletedUserException, InvalidActionException {
	    return transactionService.createTransaction(transaction, principal);
	}
	
	
	
//----------------------------------------- GET --------------------------------------------------------------------------

	
	@GetMapping("/get/all")
	@CrossOrigin(origins = "http://localhost:5173")
    public List<Transaction> getAllTransactions(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
        return transactionService.getAllTransactions(principal);
    }

    @GetMapping("/get/by-id/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public Transaction getById(@PathVariable("id") int id, Principal principal) throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
        return transactionService.getById(id, principal);
    }

    @GetMapping("/get/by-date-range/{startDate}/{endDate}")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<Transaction> getTransactionsByDateRange(@PathVariable LocalDate startDate,@PathVariable LocalDate endDate,@RequestParam(name = "accountNumber", required = false) Optional<String> accountNumber,Principal principal)
    		throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
        return transactionService.getTransactionsByDateRange(accountNumber, startDate, endDate, principal);
    }


    @GetMapping("/get/credits/{startDate}/{endDate}/{accountNumber}")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<Transaction> getCreditTransactions(@PathVariable LocalDate startDate,@PathVariable LocalDate endDate,@RequestParam(name = "accountNumber", required = false) String accountNumber,Principal principal)
     throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
        return transactionService.getCreditTransactions(accountNumber, startDate, endDate, principal);
    }

    @GetMapping("/get/debits/{startDate}/{endDate}/{accountNumber}")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<Transaction> getDebitTransactions(@PathVariable LocalDate startDate,@PathVariable LocalDate endDate,@RequestParam(name = "accountNumber", required = false) String accountNumber,Principal principal)
    		throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
        return transactionService.getDebitTransactions(accountNumber, startDate, endDate, principal);
    }
    
    
    @GetMapping("/get/account-statement/{accountNumber}")
    public Object generateAccountStatement(@PathVariable String accountNumber,Principal principal) {
    	return transactionService.generateAccountStatement(accountNumber, principal);
    }

    
    @GetMapping("/get/last-transactions/{accountNumber}/{count}")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<Transaction> getNumberOfTransactions(@PathVariable String accountNumber, @PathVariable int count, Principal principal)
            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
        return transactionService.getNumberOfTransactions(accountNumber, count, principal);
    }

}
