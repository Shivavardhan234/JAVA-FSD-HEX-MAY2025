package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.PaymentMedium;
import com.maverickbank.MaverickBank.enums.TransactionType;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Transaction;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.TransactionRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.TransactionValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class TransactionService {
	
	private TransactionRepository transactionRepository;
	private UserRepository userRepository;
	private AccountRepository accountRepository;
	
	
	public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository,AccountRepository accountRepository) {
		super();
		this.transactionRepository = transactionRepository;
		this.userRepository = userRepository;
		this.accountRepository=accountRepository;
	}
	
	
//------------------------------------------- POST -----------------------------------------------------------------------
	/**
	 * @param transaction
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws DeletedUserException
	 * @throws InvalidActionException
	 */
	public Transaction createTransaction(Transaction transaction, Principal principal)throws InvalidInputException, DeletedUserException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    // Validate the transaction
	    TransactionValidation.validateTransaction(transaction);
	    
	    if(transaction.getFromDetails().equals(transaction.getToDetails())) {
	    	throw new InvalidActionException("FAILED TO STORE TRANSACTION. From and to details are same...!!!");
	    }

	    //Any one of the transaction medium should be account or else throw invalid Action exception
	    if (transaction.getFromPaymentMedium() != PaymentMedium.ACCOUNT &&
	        transaction.getToPaymentMedium() != PaymentMedium.ACCOUNT) {
	        throw new InvalidActionException("Invalid transaction provided...!!!");
	    }
	    transaction.setTransactionDate(LocalDate.now());
	    transaction.setTransactionTime(LocalTime.now());

	    return transactionRepository.save(transaction);
	}

	
//-------------------------------------------- GET -----------------------------------------------------------------------	
	
	
	
    /**
     * @param size 
     * @param page 
     * @param principal
     * @return
     * @throws DeletedUserException
     * @throws InvalidInputException
     * @throws InvalidActionException
     */
    public List<Transaction> getAllTransactions(Integer page, Integer size, Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
    	 User currentUser = userRepository.getByUsername(principal.getName());
 	    UserValidation.checkActiveStatus(currentUser.getStatus());
 	    
 	    Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAll(pageable).getContent();
    }

    
    /**
     * @param id
     * @param principal
     * @return
     * @throws DeletedUserException
     * @throws ResourceNotFoundException
     * @throws InvalidInputException
     * @throws InvalidActionException
     */
    public Transaction getById(int id, Principal principal) throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
    	User currentUser = userRepository.getByUsername(principal.getName());
 	    UserValidation.checkActiveStatus(currentUser.getStatus());
 	    
        return transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Transaction found with the given id...!!! "));
    }

  
    /**
     * @param size 
     * @param page 
     * @param accountNumber
     * @param startDate
     * @param endDate
     * @param principal
     * @return
     * @throws DeletedUserException
     * @throws InvalidInputException
     * @throws ResourceNotFoundException
     * @throws InvalidActionException
     */
    public List<Transaction> getTransactionsByDateRange(Integer page, Integer size, Optional<String> accountNumber, LocalDate startDate, LocalDate endDate, Principal principal)
            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
    	 User currentUser = userRepository.getByUsername(principal.getName());
 	    UserValidation.checkActiveStatus(currentUser.getStatus());
 	    
 	   if (startDate.isAfter(endDate)) {
 		    throw new InvalidInputException("Start date cannot be after end date.");
 		}

 	   Pageable pageable = PageRequest.of(page, size);
 	   
        if (accountNumber.isPresent()) {
            validateAccountNumber(accountNumber.get());
            
            
            List<Transaction> transactionList=transactionRepository.findByAccountNumberAndDateRange(accountNumber.get(), startDate, endDate, pageable);
            transactionList.stream().forEach(t->{
            	    if (accountNumber.get().equals(t.getFromDetails())) {
            	        try {
							t.setTransactionType(TransactionType.DEBT);
						} catch (InvalidInputException e) {
							//do nothing
						}
            	    } else if (accountNumber.get().equals(t.getToDetails())) {
            	        try {
							t.setTransactionType(TransactionType.CREDIT);
						} catch (InvalidInputException e) {
							//do nothing
						}
            	    }
            	});
            return transactionList;
        } else {
            return transactionRepository.findByDateRange(startDate, endDate, pageable);
        }
    }

    
    /**
     * @param accountNumber
     * @param startDate
     * @param endDate
     * @param principal
     * @return
     * @throws DeletedUserException
     * @throws InvalidInputException
     * @throws ResourceNotFoundException
     * @throws InvalidActionException
     */
    public List<Transaction> getCreditTransactions(Integer page, Integer size,String accountNumber, LocalDate startDate, LocalDate endDate, Principal principal)
            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
    	 User currentUser = userRepository.getByUsername(principal.getName());
 	    UserValidation.checkActiveStatus(currentUser.getStatus());
        validateAccountNumber(accountNumber);
        
        Pageable pageable = PageRequest.of(page, size);
        
        List<Transaction> transactionList=transactionRepository.findCredits(accountNumber, startDate, endDate,pageable);
        transactionList.parallelStream().forEach(t->{
			try {
				t.setTransactionType(TransactionType.CREDIT);
			} catch (InvalidInputException e) {
				//Do nothing
			}
		});
        return transactionList;
    }

    
    /**
     * @param accountNumber
     * @param startDate
     * @param endDate
     * @param principal
     * @return
     * @throws DeletedUserException
     * @throws InvalidInputException
     * @throws ResourceNotFoundException
     * @throws InvalidActionException
     */
    public List<Transaction> getDebitTransactions(Integer page, Integer size,String accountNumber, LocalDate startDate, LocalDate endDate, Principal principal)
            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
    	 User currentUser = userRepository.getByUsername(principal.getName());
 	    UserValidation.checkActiveStatus(currentUser.getStatus());
        validateAccountNumber(accountNumber);
        
        Pageable pageable = PageRequest.of(page, size);
        
        List<Transaction> transactionList=transactionRepository.findDebits(accountNumber, startDate, endDate,pageable);
        transactionList.parallelStream().forEach(t->{
			try {
				t.setTransactionType(TransactionType.DEBT);
			} catch (InvalidInputException e) {
				//Do nothing
			}
		});
        return transactionList;
    }

  

    
    /**
     * @param size 
     * @param page 
     * @param accountNumber
     * @param count
     * @param principal
     * @return
     * @throws DeletedUserException
     * @throws InvalidInputException
     * @throws ResourceNotFoundException
     * @throws InvalidActionException
     */
    public List<Transaction> getTransactionsForAccount(Integer page, Integer size, String accountNumber, Principal principal)
            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
        
        User currentUser = userRepository.getByUsername(principal.getName());
        UserValidation.checkActiveStatus(currentUser.getStatus());
        validateAccountNumber(accountNumber);

        Pageable pageable = PageRequest.of(page, size);
        
        List<Transaction> transactionList = transactionRepository.findAllByAccountNumber(accountNumber,pageable);
        transactionList.parallelStream().forEach(t->{
    	    if (accountNumber.equals(t.getFromDetails())) {
    	        try {
					t.setTransactionType(TransactionType.DEBT);
				} catch (InvalidInputException e) {
					//do nothing
				}
    	    } else if (accountNumber.equals(t.getToDetails())) {
    	        try {
					t.setTransactionType(TransactionType.CREDIT);
				} catch (InvalidInputException e) {
					//do nothing
				}
    	    }
    	});
        return transactionList;
    }


  

    

    /**
     * @param accountNumber
     * @throws InvalidInputException
     * @throws ResourceNotFoundException
     */
    private void validateAccountNumber(String accountNumber) throws InvalidInputException, ResourceNotFoundException {
    	
        if (accountNumber == null) {
            throw new InvalidInputException("Invalid AccountNumber");
        }
        if(accountRepository.getByAccountNumber(accountNumber)==null) {
        	 throw new ResourceNotFoundException("No account found with the given account number...!!!");
        }
        
       
    }

//================================================ DELETE =====================================
	public void deleteTransaction(int id) {
		transactionRepository.deleteById(id);
		return ;
	}


}
