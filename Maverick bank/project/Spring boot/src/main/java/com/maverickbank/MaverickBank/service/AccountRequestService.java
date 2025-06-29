package com.maverickbank.MaverickBank.service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.AccountStatus;
import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.RequestType;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.AccountRequest;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.AccountRequestRepository;
import com.maverickbank.MaverickBank.repository.LoanRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class AccountRequestService {
	
	private AccountRequestRepository accountRequestRepository;
	private AccountRepository accountRepository;
	private UserRepository userRepository;
	private LoanRepository loanRepository;
	


	public AccountRequestService(AccountRequestRepository accountRequestRepository, AccountRepository accountRepository,
			UserRepository userRepository,LoanRepository loanRepository) {
		super();
		this.accountRequestRepository = accountRequestRepository;
		this.accountRepository = accountRepository;
		this.userRepository = userRepository;
		this.loanRepository=loanRepository;
	}



	//---------------------------------------- POST --------------------------------------------------------------------
	
	public AccountRequest createRequest(int accountId, RequestType requestType,String purpose, Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    if(requestType==null) {
	    	throw new InvalidInputException("Null request type provided...!!!");
	    }
	    
	    // get the account from the db
	    Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("No account record found with the given id...!!!"));
	    if(account.getAccountStatus()==AccountStatus.CLOSED) {
	    	 throw new InvalidActionException("This account is permanently closed,Cannot perform any action...!!!");
	    }
	    
	    
	    //Check if there is any balance in the bank account
	    
	    if(requestType==RequestType.CLOSE) {
	    
			    if(account.getBalance().compareTo(BigDecimal.ZERO)>0 ) {
			    	throw new InvalidActionException("This account has some amount in it. You cannot close the account...!!!");
			    }
	    
			    List<Loan> loanList=loanRepository.getByAccountId(accountId);
			    
			    for(Loan l : loanList) {
			    	if(l.isCleared()==false) {
			    		throw new InvalidActionException("You have pending loans, you cannot close account...!!!");
			    	}
			    }
	    
	    }
	    // Check if the request already exists or not
	    AccountRequest existingRequest = accountRequestRepository.getPreviousRequest(
	            accountId, requestType, ApplicationStatus.PENDING);

	    if (existingRequest != null) {
	        throw new InvalidActionException("You have already filed a request...!!!");
	    }

	    // Create and save the new request
	    AccountRequest newRequest = new AccountRequest();
	    newRequest.setAccount(account);
	    newRequest.setRequestType(requestType);
	    newRequest.setRequestStatus(ApplicationStatus.PENDING);
	    if(purpose!=null) {
	    	newRequest.setPurpose(purpose);
	    }
	    
	    //return saved request
	    return accountRequestRepository.save(newRequest);
	}
	
	
	
//--------------------------------------------- GET ----------------------------------------------------------------------
	
	public List<AccountRequest> getAllRequests(Integer page, Integer size, Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    Pageable pageable = PageRequest.of(page, size);
	    return accountRequestRepository.findAll(pageable).getContent();
	}
	
	
	
	public AccountRequest getRequestById(int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    
	    return accountRequestRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("No account request found with given id...!!!"));
	}

	
	public List<AccountRequest> getRequestsByAccountId(int accountId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    
	    List<AccountRequest> requests = accountRequestRepository.findByAccountId(accountId);
	    if(requests.isEmpty()) {
	        throw new ResourceNotFoundException("No account requests found for the given account id...!!! ");
	    }
	    return requests;
	}

	
	public List<AccountRequest> getRequestsByStatus(Integer page, Integer size, ApplicationStatus status, Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    Pageable pageable = PageRequest.of(page, size);
	    return accountRequestRepository.findByRequestStatus(status,pageable);
	}

	
	//-------------------------------- PUT ------------------------------------------------------------------------------
	
	/**
	 * @param requestId
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidActionException
	 * @throws InvalidInputException
	 */
	public AccountRequest acceptRequest(int requestId, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    AccountRequest request = accountRequestRepository.findById(requestId) .orElseThrow(() -> new ResourceNotFoundException("No account request found with the given id...!!!"));

	    if (request.getRequestStatus() == ApplicationStatus.ACCEPTED) {
	        throw new InvalidActionException("This request is already Accepted...!!!");
	    }
	    if (request.getRequestStatus() == ApplicationStatus.REJECTED) {
	        throw new InvalidActionException("This request is already REJECTED...!!!");
	    }

	    // Update request status
	    request.setRequestStatus(ApplicationStatus.ACCEPTED);
	    accountRequestRepository.save(request);

	    // Update account status based on request type
	    Account account = request.getAccount();

	    switch (request.getRequestType()) {
	        case CLOSE:
	            account.setAccountStatus(AccountStatus.CLOSED);
	            break;
	        case SUSPEND:
	            account.setAccountStatus(AccountStatus.SUSPENDED);
	            break;
	        case OPEN:
	            account.setAccountStatus(AccountStatus.OPEN);
	            break;
	        default:
	            throw new InvalidActionException("Invalid request type for account update...!!!");
	    }

	    accountRepository.save(account);

	    return request;
	}

	
	/**
	 * @param requestId
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidActionException
	 * @throws InvalidInputException
	 */
	public AccountRequest rejectRequest(int requestId, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    AccountRequest request = accountRequestRepository.findById(requestId)
	            .orElseThrow(() -> new ResourceNotFoundException("No account request found with the given id...!!!"));

	    if (request.getRequestStatus() == ApplicationStatus.REJECTED) {
	        throw new InvalidActionException("This request is already REJECTED...!!!");
	    }
	    if (request.getRequestStatus() == ApplicationStatus.ACCEPTED) {
	        throw new InvalidActionException("This request is already ACCEPTED...!!!");
	    }

	    request.setRequestStatus(ApplicationStatus.REJECTED);
	    return accountRequestRepository.save(request);
	}

	
	

}
