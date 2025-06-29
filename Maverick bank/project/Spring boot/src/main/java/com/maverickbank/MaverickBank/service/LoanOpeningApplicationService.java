package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.LoanOpeningApplication;
import com.maverickbank.MaverickBank.model.LoanPlan;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.LoanOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.LoanPlanRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class LoanOpeningApplicationService {
	
	private LoanOpeningApplicationRepository loanOpeningApplicationRepository;
	private UserRepository userRepository;
	private LoanPlanRepository loanPlanRepository;
	private AccountRepository accountRepository;
	private LoanService loanService;
	
	
	
public LoanOpeningApplicationService(LoanOpeningApplicationRepository loanOpeningApplicationRepository,
			UserRepository userRepository, LoanPlanRepository loanPlanRepository, AccountRepository accountRepository,
			LoanService loanService) {
		super();
		this.loanOpeningApplicationRepository = loanOpeningApplicationRepository;
		this.userRepository = userRepository;
		this.loanPlanRepository = loanPlanRepository;
		this.accountRepository = accountRepository;
		this.loanService = loanService;
	}


//------------------------------------------ POST ------------------------------------------------------------------------
	
	public LoanOpeningApplication createLoanApplication(int accountId, int loanPlanId, String purpose, Principal principal)
	        throws ResourceNotFoundException, InvalidActionException, DeletedUserException, InvalidInputException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    Account account = accountRepository.findById(accountId)
	            .orElseThrow(() -> new ResourceNotFoundException("No account record found with the given id...!!!"));

	    LoanPlan loanPlan = loanPlanRepository.findById(loanPlanId)
	            .orElseThrow(() -> new ResourceNotFoundException("No loan plan record found with the given id...!!!"));

	    

	    if (!loanOpeningApplicationRepository.getOldLoanOpeningApplications(accountId, loanPlanId, ApplicationStatus.PENDING).isEmpty()) {
	        throw new InvalidActionException("You have already applied for this same loan loan ...!!!");
	    }

	    LoanOpeningApplication application = new LoanOpeningApplication();
	    application.setAccount(account);
	    application.setLoanPlan(loanPlan);
	    application.setPurpose(purpose);
	    application.setStatus(ApplicationStatus.PENDING);

	    return loanOpeningApplicationRepository.save(application);
	}

	
	//--------------------------------- GET -----------------------------------------------------------------------------
	
	
	
	
	
	
	
	/**
	 * @param size 
	 * @param page 
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public List<LoanOpeningApplication> getAllLoanApplications(Integer page, Integer size, Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    
	    Pageable pageable = PageRequest.of(page, size);
	    
	    return loanOpeningApplicationRepository.findAll(pageable).getContent();
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
	public LoanOpeningApplication getById(int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    return loanOpeningApplicationRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No loan application record eas found with the given id...!!!"));
	}
	
	
	
	
	/**
	 * @param accountId
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public List<LoanOpeningApplication> getByAccountId(Integer page, Integer size,int accountId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    Pageable pageable = PageRequest.of(page, size);
	    accountRepository.findById(accountId)
	            .orElseThrow(() -> new ResourceNotFoundException("No account record found with the given id...!!!"));
	    
	    return loanOpeningApplicationRepository.getByAccountId(accountId,pageable);
	}

	
	
	/**
	 * @param loanPlanId
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public List<LoanOpeningApplication> getByLoanPlanId(int loanPlanId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    loanPlanRepository.findById(loanPlanId)
	            .orElseThrow(() -> new ResourceNotFoundException("No loan plan record found with the given id...!!!"));
	    
	    return loanOpeningApplicationRepository.getByLoanPlanId(loanPlanId);
	}

	
	

	
	
	
	/**
	 * @param accountId
	 * @param status
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public List<LoanOpeningApplication> getByAccountIdAndStatus(Integer page, Integer size,int accountId, ApplicationStatus status, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    Pageable pageable = PageRequest.of(page, size);

	    accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("No account record was found with the given id...!!!"));

	    return loanOpeningApplicationRepository.getByAccountIdAndStatus(accountId, status, pageable);
	}

	/**
	 * @param size 
	 * @param page 
	 * @param status
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 */
	public List<LoanOpeningApplication> getLoanApplicationsByStatus(Integer page, Integer size, ApplicationStatus status, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		 User currentUser = userRepository.getByUsername(principal.getName());
		    UserValidation.checkActiveStatus(currentUser.getStatus());
		    Pageable pageable = PageRequest.of(page, size);
		    return loanOpeningApplicationRepository.getByStatus(status,pageable);
	}
	
	
	
	//-------------------------------------------------- PUT --------------------------------------------------------------

	
	/**
	 * @param id
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 */
	public Loan acceptLoanOpeningApplication(int id, Principal principal)
	        throws ResourceNotFoundException, InvalidActionException, DeletedUserException, InvalidInputException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    LoanOpeningApplication application = loanOpeningApplicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No loan application record found with the given id...!!!"));

	    if (application.getStatus() == ApplicationStatus.ACCEPTED) {
	        throw new InvalidActionException("This loan application is already ACCEPTED...!!!");
	    }
	    if (application.getStatus() == ApplicationStatus.REJECTED) {
	        throw new InvalidActionException("This loan application is already REJECTED. Cannot perform action...!!!");
	    }

	    
	    application.setStatus(ApplicationStatus.ACCEPTED);
	    
	    application=loanOpeningApplicationRepository.save(application);
	    
	    Loan loan=loanService.createLoan(application.getId(), principal);
	    return loan;
	}
	
	
	
	
	/**
	 * @param id
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 */
	public LoanOpeningApplication rejectLoanOpeningApplication(int id, Principal principal)
	        throws ResourceNotFoundException, InvalidActionException, DeletedUserException, InvalidInputException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    LoanOpeningApplication application = loanOpeningApplicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No loan application record found with the given id...!!!"));

	    if (application.getStatus() == ApplicationStatus.ACCEPTED) {
	        throw new InvalidActionException("This loan application is already ACCEPTED. Cannot perform action...!!!");
	    }
	    if (application.getStatus() == ApplicationStatus.REJECTED) {
	        throw new InvalidActionException("This loan application is already REJECTED...!!!");
	    }

	    
	    
	    application.setStatus(ApplicationStatus.REJECTED);
	    return loanOpeningApplicationRepository.save(application);
	}



	
	
	

}
