package com.maverickbank.MaverickBank.service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.LoanPayment;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.LoanPaymentRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.LoanPaymentValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class LoanPaymentService {
	
	private LoanPaymentRepository loanPaymentRepository;
	private UserRepository userRepository;
	private LoanService loanService;
	public LoanPaymentService(LoanPaymentRepository loanPaymentRepository, UserRepository userRepository,
			LoanService loanService) {
		super();
		this.loanPaymentRepository = loanPaymentRepository;
		this.userRepository = userRepository;
		this.loanService = loanService;
	}
	
	
	
	//----------------------------------------- POST ---------------------------------------------------------------------
	
	
	
	public LoanPayment addLoanPayment(int loanId, BigDecimal amountPaid, Principal principal)
	        throws ResourceNotFoundException, InvalidInputException, DeletedUserException, InvalidActionException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    Loan loan = loanService.getLoanById(loanId, principal);

	    LoanPaymentValidation.validateAmountPaid(amountPaid);

	    BigDecimal installmentAmount = loan.getLoanPlan().getInstallmentAmount();
	    if (amountPaid.compareTo(installmentAmount) < 0) {
	        throw new InvalidActionException("Amount paid should be equal or greater than installment amount...!!!");
	    }

	    LocalDate today = LocalDate.now();
	    LocalDate dueDate = loan.getDueDate();
	    
	    
	    
	    
	    
	    /*step 1: calculate the penalty 
		 *step 2:if amount paid is greater then subtract extra amount from penalty
		 *step 3: set all the values for loan payment 
		 *step 4: now add this penalty to total penalty in loan, (if penalty is negative that means total penalty will be reduced)
		 *step 5:if totalPenalty is negative, throw invalid action exception saying you are paying extra amount.cannot process
		 *step6 : save the loan payment
		 *step 7: update due date of loan by adding due date+ repaymentFrequency in loanPlan
		 *step 8: update totalpenalty and due date using 2 methods we created in loanService not by loan repo.
		 *step 9: return loan payment
	     * 
	     * */
	    

	    
	    BigDecimal penalty = BigDecimal.ZERO;
	    if (today.isAfter(dueDate)) {
	        int daysLate = (int) ChronoUnit.DAYS.between(dueDate, today);
	        BigDecimal penaltyRatePercent = loan.getLoanPlan().getPenaltyRate(); 
	        BigDecimal penaltyRateDecimal = penaltyRatePercent.divide(BigDecimal.valueOf(100)); 
	        penalty = penaltyRateDecimal.multiply(installmentAmount).multiply(BigDecimal.valueOf(daysLate));
	    }

	    
	    BigDecimal amountToBePaid = installmentAmount.add(loan.getTotalPenalty());

	    if (amountPaid.compareTo(amountToBePaid) > 0) {
	        BigDecimal overpaid = amountPaid.subtract(amountToBePaid);
	        penalty = penalty.subtract(overpaid);
	        if (penalty.compareTo(BigDecimal.ZERO) < 0) {
	            penalty = BigDecimal.ZERO;
	        }
	    }

	   
	    LoanPayment payment = new LoanPayment();
	    payment.setLoan(loan);
	    payment.setDueDate(dueDate);
	    payment.setAmountToBePaid(amountToBePaid);
	    payment.setAmountPaid(amountPaid);
	    payment.setPaymentDate(today);
	    payment.setPenalty(penalty);

	    
	    BigDecimal newTotalPenalty = loan.getTotalPenalty().add(penalty);

	    
	    if (newTotalPenalty.compareTo(BigDecimal.ZERO) < 0) {
	        throw new InvalidActionException("You're trying to pay more than required. Cannot process...!!!");
	    }

	   
	    LoanPayment savedPayment = loanPaymentRepository.save(payment);

	    
	    LocalDate newDueDate = loan.getDueDate().plusMonths(loan.getLoanPlan().getRepaymentFrequency());

	    
	    loanService.updateDueDate(loanId, newDueDate, principal);
	    loanService.updateTotalPenalty(loanId, newTotalPenalty, principal);
	    loanService.updateIsCleared(loanId, principal);
	   
	    return savedPayment;
	}
	
	
	
//---------------------------------------- GET --------------------------------------------------------------------------
	/**
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public List<LoanPayment> getAllLoanPayments(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
        User user = userRepository.getByUsername(principal.getName());
        UserValidation.checkActiveStatus(user.getStatus());
        return loanPaymentRepository.findAll();
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
    public LoanPayment getLoanPaymentById(int id, Principal principal)
            throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
        User user = userRepository.getByUsername(principal.getName());
        UserValidation.checkActiveStatus(user.getStatus());

        return loanPaymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No loan payment found with the given id...!!!"));
    }

    /**
     * @param loanId
     * @param principal
     * @return
     * @throws DeletedUserException
     * @throws ResourceNotFoundException
     * @throws InvalidInputException
     * @throws InvalidActionException
     */
    public List<LoanPayment> getPaymentsByLoanId(int loanId, Principal principal)
            throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {
        User user = userRepository.getByUsername(principal.getName());
        UserValidation.checkActiveStatus(user.getStatus());
        
        List<LoanPayment> loanPaymentList = loanPaymentRepository.getByLoan(loanId);
        
        if(loanPaymentList==null||loanPaymentList.isEmpty()) {
        	throw new ResourceNotFoundException("No loan payments found for the fiven loan id...!!!");
        }
        
        return loanPaymentList;
    }

}
