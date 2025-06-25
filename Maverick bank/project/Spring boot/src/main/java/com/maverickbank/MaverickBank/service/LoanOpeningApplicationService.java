package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

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
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public List<LoanOpeningApplication> getAllLoanApplications(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    return loanOpeningApplicationRepository.findAll();
	}

	
	
	public LoanOpeningApplication getById(int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    return loanOpeningApplicationRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No loan application record eas found with the given id...!!!"));
	}
	
	
	
	
	public List<LoanOpeningApplication> getByAccountId(int accountId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    accountRepository.findById(accountId)
	            .orElseThrow(() -> new ResourceNotFoundException("No account record found with the given id...!!!"));
	    
	    return loanOpeningApplicationRepository.getByAccountId(accountId);
	}

	
	public List<LoanOpeningApplication> getByLoanPlanId(int loanPlanId, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    loanPlanRepository.findById(loanPlanId)
	            .orElseThrow(() -> new ResourceNotFoundException("No loan plan record found with the given id...!!!"));
	    
	    return loanOpeningApplicationRepository.getByLoanPlanId(loanPlanId);
	}

	
	

	
	
	
	public List<LoanOpeningApplication> getByAccountIdAndStatus(int accountId, ApplicationStatus status, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("No account record was found with the given id...!!!"));

	    return loanOpeningApplicationRepository.getByAccountIdAndStatus(accountId, status);
	}

	public List<LoanOpeningApplication> getLoanApplicationsByStatus(ApplicationStatus status, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		 User currentUser = userRepository.getByUsername(principal.getName());
		    UserValidation.checkActiveStatus(currentUser.getStatus());

		    return loanOpeningApplicationRepository.getByStatus(status);
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
