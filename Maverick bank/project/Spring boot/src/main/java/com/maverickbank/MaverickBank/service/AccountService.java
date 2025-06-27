package com.maverickbank.MaverickBank.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.Util.AccountNumberGenerator;
import com.maverickbank.MaverickBank.enums.AccountStatus;
import com.maverickbank.MaverickBank.enums.PaymentMedium;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.NotEnoughFundsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.CustomerAccount;
import com.maverickbank.MaverickBank.model.Transaction;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.CustomerRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.AccountValidation;
import com.maverickbank.MaverickBank.validation.TransactionValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class AccountService {
	
	private AccountRepository accountRepository;
	private UserRepository userRepository;
	private TransactionService transactionService;
	private PasswordEncoder passwordEncoder;
	private CustomerAccountService customerAccountService;
	private CustomerRepository customerRepository;




public AccountService(AccountRepository accountRepository, UserRepository userRepository,
			  TransactionService transactionService,
			PasswordEncoder passwordEncoder, 
			CustomerAccountService customerAccountService, CustomerRepository customerRepository) {
		super();
		this.accountRepository = accountRepository;
		this.userRepository = userRepository;
		this.transactionService = transactionService;
		this.passwordEncoder = passwordEncoder;
		this.customerAccountService = customerAccountService;
		this.customerRepository = customerRepository;
	}



//-------------------------------------------- POST ---------------------------------------------------------------------

	public Account createAccount(Account account, Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    // Check the user activity status
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    AccountValidation.validateAccount(account);
	    account.setAccountStatus(AccountStatus.OPEN);
	    account.setBalance(BigDecimal.ZERO);
	    
	    String accountNumber=AccountNumberGenerator.accountNumberGenerator();
	    
	    while(accountRepository.getByAccountNumber(accountNumber)!=null  ) {
	    	accountNumber=AccountNumberGenerator.accountNumberGenerator();
	    }
	    
	    account.setAccountNumber(accountNumber);
	    account.setKycCompliant(false);
	    // Save and return the account with id
	    return accountRepository.save(account);
	}
	
	
	
//-------------------------------------------- GET -----------------------------------------------------------------------
	
	
	/**
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws ResourceNotFoundException
	 */
	public List<Account> getAllAccounts(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException, ResourceNotFoundException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    return accountRepository.findAll();
	}

	/**
	 * @param id
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public Account getAccountById(int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No account record found with the given id...!!!"));
	}

	/**
	 * @param accountNumber
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public Account getAccountByAccountNumber(String accountNumber, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    Account account = accountRepository.getByAccountNumber(accountNumber);
	    if (account == null) {
	        throw new ResourceNotFoundException("No account record found with the given account number...!!!");
	    }
	    return account;
	}

	/**
	 * @param branchId
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public List<Account> getAccountsByBranchId(int branchId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    List<Account> accounts = accountRepository.getByBranchId(branchId);
	    if (accounts.isEmpty()) {
	        throw new ResourceNotFoundException("No account records found with the given branch id...!!!");
	    }
	    return accounts;
	}
	
	

	/**
	 * @param accountStatus
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public List<Account> getAccountsByStatus(AccountStatus accountStatus, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    List<Account> accounts = accountRepository.getByAccountStatus(accountStatus);
	    if (accounts.isEmpty()) {
	        throw new ResourceNotFoundException("No account records found with the given account status...!!!");
	    }
	    return accounts;
	}
	
	/**
	 * @param accountTypeId
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public List<Account> getAccountsByAccountTypeId(int accountTypeId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    List<Account> accounts = accountRepository.getByAccountTypeId(accountTypeId);
	    if (accounts.isEmpty()) {
	        throw new ResourceNotFoundException("No account records found with the given account type id...!!!");
	    }
	    return accounts;
	}
	
	
//---------------------------------------------- PUT --------------------------------------------------------------------
	 /**
	 * @param accountId
	 * @param newName
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws ResourceNotFoundException
	 * @throws InvalidActionException 
	 */
	public Account updateAccountName(int accountId, String newName, Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        Account account = accountRepository.findById(accountId)
	                .orElseThrow(() -> new ResourceNotFoundException("No accountRecord with the givwn id...!!!"));
	        
	        if (newName == null ) {
	            throw new InvalidInputException("New name cannot be empty");
	        }
	        AccountValidation.validateAccountName(newName);
	        
	        account.setAccountName(newName.trim());
	        return accountRepository.save(account);
	    }

	    /**
	     * @param accountId
	     * @param status
	     * @param principal
	     * @return
	     * @throws DeletedUserException
	     * @throws InvalidInputException
	     * @throws ResourceNotFoundException
	     * @throws InvalidActionException 
	     */
	    public Account updateAccountStatus(int accountId, AccountStatus status, Principal principal) throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        Account account = accountRepository.findById(accountId)
	                .orElseThrow(() -> new ResourceNotFoundException("No account found with the given id...!!!"));

	        AccountValidation.validateAccountStatus(status);

	        account.setAccountStatus(status);
	        return accountRepository.save(account);
	    }
	    
	    
	    
	    
	    /**
	     * @param accountId
	     * @param amount
	     * @param medium
	     * @param pin
	     * @param principal
	     * @return
	     * @throws DeletedUserException
	     * @throws InvalidInputException
	     * @throws ResourceNotFoundException
	     * @throws InvalidActionException
	     * @throws NotEnoughFundsException
	     */
	    public Account withdraw(int accountId, BigDecimal amount, PaymentMedium medium,String pin, Principal principal) 
	            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException, NotEnoughFundsException {

	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        //Check if customer exists
	        Customer customer= customerRepository.getByUserId(currentUser.getId());
	        
	        if(customer==null) {
	        	throw new InvalidActionException("You are not a customer of this bank...!!!");
	        }
	        
	        
	        //Get customerAccount
	        CustomerAccount customerAccount= customerAccountService.getByCustomerIdAndAccountId(customer.getId(), accountId, principal);
	        
	        //chack whether customer account exists or not
	        if(customerAccount==null) {
	        	throw new InvalidActionException("Invalid request.This bank account is not yours...!!!");
	        }
	        
	        
	        //Validate pin
	        if(!passwordEncoder.matches(pin, customerAccount.getPin())) {
	        	throw new InvalidActionException("Incorrect pin. Please enter correct pin...!!!");
	        }
	        
	        
	        
	        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("No account record  with the provided id...!!!"));

	        // Check balance
	        if (account.getBalance().compareTo(amount) < 0) {
	            throw new NotEnoughFundsException("You dont have enough balance to perform transaction...!!!");
	        }

	        // Rounding the amount upto 2 decimal points
	        //Not  necessary but it looks good
	        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);

	        // Create transaction object
	        Transaction transaction = new Transaction();
	        transaction.setFromDetails(account.getAccountNumber());
	        //this can be upi or wallet
	        transaction.setToDetails("EXTERNAL");  
	        transaction.setTransactionAmount(roundedAmount);
	        transaction.setTransactionDate(LocalDate.now());
	        transaction.setTransactionTime(LocalTime.now());
	        transaction.setFromPaymentMedium(PaymentMedium.ACCOUNT);
	        TransactionValidation.validateFromPaymentMedium(medium);
	        transaction.setToPaymentMedium(medium);

	        // Save transaction using  transaction service
	        transactionService.createTransaction(transaction, principal);

	        // Update account balance as it is deducted
	        account.setBalance(account.getBalance().subtract(roundedAmount));
	        accountRepository.save(account);

	       

	        return account;
	    }
	    
	    
	    
	    
	    /**
	     * @param accountId
	     * @param amount
	     * @param medium
	     * @param principal
	     * @return
	     * @throws DeletedUserException
	     * @throws InvalidInputException
	     * @throws ResourceNotFoundException
	     * @throws InvalidActionException
	     */
	    public Account deposit(int accountId, BigDecimal amount, PaymentMedium medium, Principal principal) 
	            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {

	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("No account record with the provided id...!!!"));

	        // Rounding the amount up to 2 decimal points
	        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
	        // Create transaction object
	        Transaction transaction = new Transaction();
	        transaction.setFromDetails("EXTERNAL");  
	        transaction.setToDetails(account.getAccountNumber());
	        transaction.setTransactionAmount(roundedAmount);
	        transaction.setTransactionDate(LocalDate.now());
	        transaction.setTransactionTime(LocalTime.now());
	        transaction.setFromPaymentMedium(medium);
	        TransactionValidation.validateFromPaymentMedium(medium);
	        transaction.setToPaymentMedium(PaymentMedium.ACCOUNT);

	        // Save transaction using transaction service
	        transactionService.createTransaction(transaction, principal);
	        // Update account balance by adding the amount
	        account.setBalance(account.getBalance().add(roundedAmount));
	        accountRepository.save(account);
	        
	        return account;
	    }


	    /**
	     * @param fromAccountId
	     * @param toAccountNumber
	     * @param amount
	     * @param principal
	     * @return
	     * @throws DeletedUserException
	     * @throws InvalidInputException
	     * @throws ResourceNotFoundException
	     * @throws InvalidActionException
	     * @throws NotEnoughFundsException
	     */
	    public Account transfer(int fromAccountId, String toAccountNumber, BigDecimal amount,String pin, Principal principal) 
	            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException, NotEnoughFundsException {

	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	      //Check if customer exists
	        Customer customer= customerRepository.getByUserId(currentUser.getId());
	        
	        if(customer==null) {
	        	throw new InvalidActionException("You are not a customer of this bank...!!!");
	        }
	        
	        
	        //Get customerAccount
	        CustomerAccount customerAccount= customerAccountService.getByCustomerIdAndAccountId(customer.getId(), fromAccountId, principal);
	        
	        //chack whether customer account exists or not
	        if(customerAccount==null) {
	        	throw new InvalidActionException("Invalid request.This bank account is not yours...!!!");
	        }
	        
	        
	        //Validate pin
	        if(!passwordEncoder.matches(pin, customerAccount.getPin())) {
	        	throw new InvalidActionException("Incorrect pin. Please enter correct pin...!!!");
	        }
	        
	        
	        
	        
	        
	        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new ResourceNotFoundException("No account record with the provided fromAccountId...!!!"));
	        Account toAccount = accountRepository.getByAccountNumber(toAccountNumber);
	        if (toAccount == null || toAccount.getAccountStatus()==AccountStatus.CLOSED) {
	            throw new ResourceNotFoundException("No account record found with the given toAccountNumber...!!!");
	        }
	        if(toAccount.getAccountStatus()==AccountStatus.SUSPENDED) {
	        	throw new InvalidActionException("Reciever bank account nor responding! Please try again after some time...!!!");
	        }

	        // Check if fromAccount has sufficient funds
	        if (fromAccount.getBalance().compareTo(amount) < 0) {
	            throw new NotEnoughFundsException("You don't have enough balance to perform transaction...!!!");
	        }
	        // Rounding the amount up to 2 decimal points
	        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
	        // Create transaction object for transfer
	        Transaction transaction = new Transaction();
	        transaction.setFromDetails(fromAccount.getAccountNumber());
	        transaction.setToDetails(toAccountNumber);
	        transaction.setTransactionAmount(roundedAmount);
	        transaction.setTransactionDate(LocalDate.now());
	        transaction.setTransactionTime(LocalTime.now());
	        transaction.setFromPaymentMedium(PaymentMedium.ACCOUNT);
	        transaction.setToPaymentMedium(PaymentMedium.ACCOUNT);

	        
	        
	        
	        // Save transaction using transaction service
	        transactionService.createTransaction(transaction, principal);
	        // Update the  balance for both accounts
	        fromAccount.setBalance(fromAccount.getBalance().subtract(roundedAmount));
	        toAccount.setBalance(toAccount.getBalance().add(roundedAmount));
	        accountRepository.save(fromAccount);
	        accountRepository.save(toAccount);
	        
	        return fromAccount;
	    }



		public Account updateKyc(int accountId, boolean kyc, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
			 User currentUser = userRepository.getByUsername(principal.getName());
		        UserValidation.checkActiveStatus(currentUser.getStatus());

		        Account account = accountRepository.findById(accountId)
		                .orElseThrow(() -> new ResourceNotFoundException("No account found with the given id...!!!"));

		        

		        account.setKycCompliant(kyc);
			return account;
		}



}
