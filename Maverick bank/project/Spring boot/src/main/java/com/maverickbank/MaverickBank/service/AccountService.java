package com.maverickbank.MaverickBank.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	Logger logger = LoggerFactory.getLogger(AccountService.class);



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
	    logger.info("In creare account");
		// Check the user activity status
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    logger.info("User active status validated");
	    
	    AccountValidation.validateAccount(account);
	    
	    account.setAccountStatus(AccountStatus.OPEN);
	    account.setBalance(BigDecimal.ZERO);
	    
	    String accountNumber=AccountNumberGenerator.accountNumberGenerator();
	    logger.info("Account number generated");
	    while(accountRepository.getByAccountNumber(accountNumber)!=null  ) {
	    	accountNumber=AccountNumberGenerator.accountNumberGenerator();
	    }
	    
	    account.setAccountNumber(accountNumber);
	    account.setKycCompliant(false);
	    // Save and return the account with id
	    logger.info("Saving bank account");
	    return accountRepository.save(account);
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
	 * @throws ResourceNotFoundException
	 */
	public List<Account> getAllAccounts(Integer page, Integer size, Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException, ResourceNotFoundException {
		logger.info("In get all bank accounts");
		User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    logger.info("USer active status validated");
	    
	    Pageable pageable = PageRequest.of(page, size);

	    return accountRepository.findAll(pageable).getContent();
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
		logger.info("In get account by id");
		User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    logger.info("User active status verified");
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
		logger.info("In get account by account number");
		User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    logger.info("User active status verified");
	    Account account = accountRepository.getByAccountNumber(accountNumber);
	    
	    if (account == null) {
	        throw new ResourceNotFoundException("No account record found with the given account number...!!!");
	    }
	    logger.info("Account by account number fetched");
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
	public List<Account> getAccountsByBranchId(Integer page, Integer size,int branchId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
		logger.info("In get accounts by branch id");
		User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    logger.info("User active status verified");
	    Pageable pageable = PageRequest.of(page, size);
	    List<Account> accounts = accountRepository.getByBranchId(branchId,pageable);
	    if (accounts.isEmpty()) {
	        throw new ResourceNotFoundException("No account records found with the given branch id...!!!");
	    }
	    logger.info("Account list for branch is fetched");
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
	public List<Account> getAccountsByStatus(Integer page, Integer size,AccountStatus accountStatus, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
		logger.info("In get account by account status");
		User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    logger.info("User active status validated");
	    Pageable pageable = PageRequest.of(page, size);
	    List<Account> accounts = accountRepository.getByAccountStatus(accountStatus,pageable);
	    if (accounts.isEmpty()) {
	        throw new ResourceNotFoundException("No account records found with the given account status...!!!");
	    }
	    logger.info("account list for status= "+ accountStatus +" is fetched");
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
	public List<Account> getAccountsByAccountTypeId(Integer page, Integer size,int accountTypeId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
		logger.info("In get account by account type");
		User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    logger.info("User active status validated");
	    Pageable pageable = PageRequest.of(page, size);
	    List<Account> accounts = accountRepository.getByAccountTypeId(accountTypeId,pageable);
	    if (accounts.isEmpty()) {
	        throw new ResourceNotFoundException("No account records found with the given account type id...!!!");
	    }
	    logger.info("Accounts for given account type fetched");
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
		logger.info("In update account name");   
		User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status validated");
	        
	        Account account = accountRepository.findById(accountId)
	                .orElseThrow(() -> new ResourceNotFoundException("No accountRecord with the givwn id...!!!"));
	        logger.info("Old account fetched");
	        
	        if (newName == null ) {
	            throw new InvalidInputException("New name cannot be empty");
	        }
	        
	        AccountValidation.validateAccountName(newName);
	        logger.info("new account name validated");
	        
	        account.setAccountName(newName.trim());
	        logger.info("Updated the account name and saving account");
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
	    	logger.info("In update account status");
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status validated");
	        
	        Account account = accountRepository.findById(accountId)
	                .orElseThrow(() -> new ResourceNotFoundException("No account found with the given id...!!!"));
	        logger.info("Old account retrieved");
	        AccountValidation.validateAccountStatus(status);
	        
	        account.setAccountStatus(status);
	        logger.info("New account status set to account and saving account");
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
	    @Transactional
	    public Account withdraw(int accountId, BigDecimal amount, PaymentMedium medium,String pin, Principal principal) 
	            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException, NotEnoughFundsException {
	    	logger.info("User active status validated");
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status validated");
	        
	        //Check if customer exists
	        Customer customer= customerRepository.getByUserId(currentUser.getId());
	        logger.info("Retrieved the customer");
	        
	        
	        
	        //Get customerAccount
	        CustomerAccount customerAccount= customerAccountService.getByCustomerIdAndAccountId(customer.getId(), accountId, principal);
	        logger.info("Customer account retrieved");
	        
	        //chack whether customer account exists or not
	        if(customerAccount==null) {
	        	throw new InvalidActionException("Invalid request.This bank account is not yours...!!!");
	        }
	        
	        
	        //Validate pin
	        if(!passwordEncoder.matches(pin, customerAccount.getPin())) {
	        	throw new InvalidActionException("Incorrect pin. Please enter correct pin...!!!");
	        }
	        
	        
	        
	        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("No account record  with the provided id...!!!"));
	        logger.info("Account retrieved");
	        
	        // Check balance
	        if (account.getBalance().compareTo(amount) < 0) {
	            throw new NotEnoughFundsException("You dont have enough balance to perform transaction...!!!");
	        }

	        // Rounding the amount upto 2 decimal points
	        //Not  necessary but it looks good
	        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);

	        // Create transaction object
	        logger.info("Creating a transaction");
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
	        logger.info("Transaction saved");
	        
	        // Update account balance as it is deducted
	        account.setBalance(account.getBalance().subtract(roundedAmount));
	        logger.info("Account balance updated");
	         
	        return accountRepository.save(account);
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
	    @Transactional
	    public Account deposit(int accountId, BigDecimal amount, PaymentMedium medium, Principal principal) 
	            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException {
	    	logger.info("In deposit");
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status validated");

	        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("No account record with the provided id...!!!"));
	        logger.info("Bank account is retrieved");
	        // Rounding the amount up to 2 decimal points
	        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
	        // Create transaction object
	        logger.info("Creating a transaction");
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
	        logger.info("Transaction saved");
	        // Update account balance by adding the amount
	        account.setBalance(account.getBalance().add(roundedAmount));
	        
	        logger.info("Updated balance was set and saving");
	        return accountRepository.save(account);
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
	    @Transactional
	    public Account transfer(int fromAccountId, String toAccountNumber, BigDecimal amount,String pin, Principal principal) 
	            throws DeletedUserException, InvalidInputException, ResourceNotFoundException, InvalidActionException, NotEnoughFundsException {
	    	logger.info("In transfer amount");
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        logger.info("User active status validated");

	      //Check if customer exists
	        Customer customer= customerRepository.getByUserId(currentUser.getId());
	        logger.info("Customer retrieved");
	        
	        
	        
	        //Get customerAccount
	        CustomerAccount customerAccount= customerAccountService.getByCustomerIdAndAccountId(customer.getId(), fromAccountId, principal);
	        logger.info("Customer account retrieved");
	        
	        //chack whether customer account exists or not
	        if(customerAccount==null) {
	        	throw new InvalidActionException("Invalid request.This bank account is not yours...!!!");
	        }
	        
	        
	        //Validate pin
	        if(!passwordEncoder.matches(pin, customerAccount.getPin())) {
	        	throw new InvalidActionException("Incorrect pin. Please enter correct pin...!!!");
	        }
	        
	        
	        
	        
	        
	        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new ResourceNotFoundException("No account record with the provided fromAccountId...!!!"));
	        logger.info("from account retrieved");
	        
	        Account toAccount = accountRepository.getByAccountNumber(toAccountNumber);
	        logger.info("to account retrieved");
	        
	        if (toAccount == null || toAccount.getAccountStatus()==AccountStatus.CLOSED) {
	            throw new ResourceNotFoundException("No account record found with the given toAccountNumber...!!!");
	        }
	        else if(toAccount.getAccountStatus()==AccountStatus.SUSPENDED) {
	        	throw new InvalidActionException("Reciever bank account nor responding! Please try again after some time...!!!");
	        }

	        // Check if fromAccount has sufficient funds
	        if (fromAccount.getBalance().compareTo(amount) < 0) {
	            throw new NotEnoughFundsException("You don't have enough balance to perform transaction...!!!");
	        }
	        
	        // Rounding the amount up to 2 decimal points
	        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
	        
	        // Create transaction object for transfer
	        logger.info("Creating a transaction");
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
	        logger.info("Transaction saved");
	        
	        // Update the  balance for both accounts
	        fromAccount.setBalance(fromAccount.getBalance().subtract(roundedAmount));
	        toAccount.setBalance(toAccount.getBalance().add(roundedAmount));
	        
	        accountRepository.save(toAccount);
	        logger.info("To account saved and from account is being updated");
	        return accountRepository.save(fromAccount);
	    }



		/**
		 * @param accountId
		 * @param kyc
		 * @param principal
		 * @return
		 * @throws InvalidInputException
		 * @throws InvalidActionException
		 * @throws DeletedUserException
		 * @throws ResourceNotFoundException
		 */
		public Account updateKyc(int accountId, boolean kyc, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
			logger.info("In update Kyc ");
			User currentUser = userRepository.getByUsername(principal.getName());
		        UserValidation.checkActiveStatus(currentUser.getStatus());
		        logger.info("User active status validated");

		        Account account = accountRepository.findById(accountId)
		                .orElseThrow(() -> new ResourceNotFoundException("No account found with the given id...!!!"));

		        logger.info("Retrieved the account");

		        account.setKycCompliant(kyc);
		        logger.info("Kyc compliant is set and saving");
			return accountRepository.save(account);
		}



}
