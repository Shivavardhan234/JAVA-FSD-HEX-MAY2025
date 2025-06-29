package com.maverickbank.MaverickBank.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.LoanStatus;
import com.maverickbank.MaverickBank.enums.PaymentMedium;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.LoanOpeningApplication;
import com.maverickbank.MaverickBank.model.LoanPayment;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.LoanOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.LoanPaymentRepository;
import com.maverickbank.MaverickBank.repository.LoanRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class LoanService {
	private LoanRepository loanRepository;
	private UserRepository userRepository;
	private AccountService accountService;
	private LoanOpeningApplicationRepository loanOpeningApplicationRepository;
	private LoanPaymentRepository loanPaymentRepository;
	
	
	



public LoanService(LoanRepository loanRepository, UserRepository userRepository, AccountService accountService,
			LoanOpeningApplicationRepository loanOpeningApplicationRepository,
			LoanPaymentRepository loanPaymentRepository) {
		super();
		this.loanRepository = loanRepository;
		this.userRepository = userRepository;
		this.accountService = accountService;
		this.loanOpeningApplicationRepository = loanOpeningApplicationRepository;
		this.loanPaymentRepository = loanPaymentRepository;
	}



//---------------------------------------------- PUT  --------------------------------------------------------------------
	
	
	
	public Loan createLoan(int loanApplicationId, Principal principal)throws ResourceNotFoundException, InvalidActionException, DeletedUserException, InvalidInputException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    
	    LoanOpeningApplication loanOpeningApplication = loanOpeningApplicationRepository.findById(loanApplicationId).orElseThrow(() -> new ResourceNotFoundException("No loan application found with the given ID...!!!"));

	    if (loanOpeningApplication.getStatus() != ApplicationStatus.ACCEPTED) {
	        throw new InvalidActionException("Loan application is not approved. Cannot create loan...!!!");
	    }

	    // Creating a new loan
	    Loan loan = new Loan();
	    loan.setAccount(loanOpeningApplication.getAccount());
	    loan.setLoanPlan(loanOpeningApplication.getLoanPlan());

	    LocalDate startDate = LocalDate.now();
	    loan.setLoanStartDate(startDate);
	    loan.setDisbursementDate(startDate);

	    int termInMonths = loanOpeningApplication.getLoanPlan().getLoanTerm();
	    loan.setLoanEndDate(startDate.plusMonths(termInMonths));

	    loan.setStatus(LoanStatus.ACTIVE);
	    
	    loan.setTotalPenalty(BigDecimal.ZERO);
	    loan.setDueDate(startDate.plusMonths(loanOpeningApplication.getLoanPlan().getRepaymentFrequency()));
	    loan.setCleared(false);
	    
	   //Save the loan
	    loan = loanRepository.save(loan);

	    //Add the principal amount to account
	    BigDecimal principalAmount = loanOpeningApplication.getLoanPlan().getPrincipalAmount();
	    accountService.deposit(loanOpeningApplication.getAccount().getId(), principalAmount, PaymentMedium.BANK, principal);

	    return loan;
	}
	
	
	
//--------------------------------------------- GET ----------------------------------------------------------------------
	
	
	
	/**
	 * @param size 
	 * @param page 
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public List<Loan> getAllLoans(Integer page, Integer size, Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    Pageable pageable = PageRequest.of(page, size);
	    return loanRepository.findAll(pageable).getContent();
	}
	
	

	public Loan getLoanById(int loanId, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());
	    
	    return loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException("No loan found with the given ID...!!!"));
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
	public List<Loan> getLoansByAccountId(int accountId, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    
	    List<Loan> loanList =loanRepository.getByAccountId(accountId);
	    
	    
	    if(loanList.isEmpty()) {
	    	throw new ResourceNotFoundException("No loan found with the given account id...!!!");
	    }
	    return loanList;
	}

	
	
	
	/**
	 * @param size 
	 * @param page 
	 * @param status
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 * @throws ResourceNotFoundException 
	 */
	public List<Loan> getLoansByStatus(Integer page, Integer size, LoanStatus status, Principal principal)
	        throws DeletedUserException, InvalidInputException, InvalidActionException, ResourceNotFoundException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    Pageable pageable = PageRequest.of(page, size);
	    
	    List<Loan> loanList =loanRepository.getByStatus(status,pageable);
	    
	    
	    if(loanList.isEmpty()) {
	    	throw new ResourceNotFoundException("No loan found with the given status...!!!");
	    }
	    return loanList;
	}

	/**
	 * @param accountId
	 * @param status
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws ResourceNotFoundException
	 */
	public List<Loan> getLoansByAccountIdAndStatus(int accountId,LoanStatus status, Principal principal)
	        throws DeletedUserException, InvalidInputException, InvalidActionException, ResourceNotFoundException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    List<Loan> loanList =loanRepository.getByAccountIdAndStatus(accountId,status);
	    
	    
	    if(loanList.isEmpty()) {
	    	throw new ResourceNotFoundException("No loan found with the given account number and status...!!!");
	    }
	    return loanList;
	}


//--------------------------------------------------- PUT ----------------------------------------------------------------
	/**
	 * @param loanId
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 * @throws InvalidActionException
	 * @throws InvalidInputException 
	 */
	public Loan closeLoan(int loanId, Principal principal)throws DeletedUserException, ResourceNotFoundException, InvalidActionException, InvalidInputException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException("No loan found with the given ID...!!!"));

	    if (loan.getStatus() == LoanStatus.CLOSED) {
	        throw new InvalidActionException("This loan is already closed...!!!");
	    }

	    loan.setStatus(LoanStatus.CLOSED);

	    return loanRepository.save(loan);
	}
	
	
	
	
	
	

	/**
	 * @param loanId
	 * @param newPenalty
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 * @throws InvalidInputException
	 * @throws InvalidActionException 
	 */
	public Loan updateTotalPenalty(int loanId, BigDecimal newPenalty, Principal principal)
	        throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    if (newPenalty.compareTo(BigDecimal.ZERO) < 0) {
	        throw new InvalidInputException("Penalty cannot be negative");
	    }

	    Loan loan = loanRepository.findById(loanId)
	            .orElseThrow(() -> new ResourceNotFoundException("No loan found with the given ID...!!!"));

	    loan.setTotalPenalty(newPenalty.setScale(2, RoundingMode.HALF_UP));
	    return loanRepository.save(loan);
	}

	/**
	 * @param loanId
	 * @param newDueDate
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 * @throws InvalidInputException
	 * @throws InvalidActionException 
	 */
	public Loan updateDueDate(int loanId, LocalDate newDueDate, Principal principal)
	        throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    if (newDueDate == null || newDueDate.isBefore(LocalDate.now())) {
	        throw new InvalidInputException("Invalid due date provided");
	    }

	    Loan loan = loanRepository.findById(loanId)
	            .orElseThrow(() -> new ResourceNotFoundException("No loan found with the given ID...!!!"));

	    loan.setDueDate(newDueDate);
	    return loanRepository.save(loan);
	}
	
	
	
	
	/**
	 * @param loanId
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	
	public Loan updateIsCleared(int loanId, Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException("No loan found with the given ID...!!!"));

	    
	    List<LoanPayment> loanPaymentList = loanPaymentRepository.getByLoan(loanId);
	    int numberOfPayments = loan.getLoanPlan().getLoanTerm()/loan.getLoanPlan().getRepaymentFrequency();

	    
	    if(loanPaymentList.size() >= numberOfPayments){
	    	loan.setDueDate(null);
	        loan.setCleared(true);
	        return loanRepository.save(loan);
	    	
	    }
	   
	    return loan; 
	}



}
