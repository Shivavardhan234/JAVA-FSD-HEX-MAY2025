package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.LoanType;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.LoanPlan;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.LoanPlanRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.LoanPlanValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class LoanPlanService {
	
	private LoanPlanRepository loanPlanRepository;
	private UserRepository userRepository;
	
	
	public LoanPlanService(LoanPlanRepository loanPlanRepository, UserRepository userRepository) {
		super();
		this.loanPlanRepository = loanPlanRepository;
		this.userRepository = userRepository;
	}
	
	
	//-------------------------------- POST -----------------------------------------------------------------------------
	
	
	public LoanPlan createLoanPlan(LoanPlan loanPlan, Principal principal)throws DeletedUserException, InvalidInputException, InvalidActionException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    LoanPlanValidation.validateLoanPlan(loanPlan);

	    // Check if loan plan already exists or not
	    
	    if (loanPlanRepository.getLoanPlanByName(loanPlan.getLoanName())!=null) {
	        throw new InvalidActionException("A loan plan with this name already exists...!!!");
	    }

	    return loanPlanRepository.save(loanPlan);
	}

//------------------------------------------------------- GET ------------------------------------------------------------
	
	/**
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public List<LoanPlan> getAllLoanPlans(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    return loanPlanRepository.findAll();
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
	public LoanPlan getLoanPlanById(int id, Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    return loanPlanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No loan plan found with the given id...!!!"));
	}
	
	
	
	
	
	/**
	 * @param loanType
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public List<LoanPlan> getLoanPlansByType(LoanType loanType, Principal principal)
	        throws DeletedUserException, InvalidInputException, InvalidActionException {
	    
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    return loanPlanRepository.getByLoanType(loanType);
	}


//--------------------------------------------- PUT ---------------------------------------------------------------------
	public LoanPlan updateLoanPlan(LoanPlan loanPlan, Principal principal)
	        throws DeletedUserException, ResourceNotFoundException, InvalidInputException, InvalidActionException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    LoanPlan oldLoanPlan = loanPlanRepository.findById(loanPlan.getId())
	            .orElseThrow(() -> new ResourceNotFoundException("No Loan Plan record found with the given id...!!!"));

	    // Apply validations and updates only for non-null fields
	    if (loanPlan.getLoanName() != null) {
	        LoanPlanValidation.validateLoanName(loanPlan.getLoanName());
	        oldLoanPlan.setLoanName(loanPlan.getLoanName());
	    }

	    if (loanPlan.getLoanType() != null) {
	        LoanPlanValidation.validateLoanType(loanPlan.getLoanType());
	        oldLoanPlan.setLoanType(loanPlan.getLoanType());
	    }

	    if (loanPlan.getPrincipalAmount() != null) {
	        LoanPlanValidation.validatePrincipalAmount(loanPlan.getPrincipalAmount());
	        oldLoanPlan.setPrincipalAmount(loanPlan.getPrincipalAmount());
	    }

	    if (loanPlan.getLoanTerm() != 0) {
	        LoanPlanValidation.validateLoanTerm(loanPlan.getLoanTerm());
	        oldLoanPlan.setLoanTerm(loanPlan.getLoanTerm());
	    }

	    if (loanPlan.getInterestRate() != null) {
	        LoanPlanValidation.validateIntrestRate(loanPlan.getInterestRate());
	        oldLoanPlan.setInterestRate(loanPlan.getInterestRate());
	    }

	    if (loanPlan.getInstallmentAmount() != null) {
	        LoanPlanValidation.validateInstallmentAmount(loanPlan.getInstallmentAmount());
	        oldLoanPlan.setInstallmentAmount(loanPlan.getInstallmentAmount());
	    }

	    if (loanPlan.getRepaymentFrequency() != 0) {
	        LoanPlanValidation.validateRepaymentFrequency(loanPlan.getRepaymentFrequency());
	        oldLoanPlan.setRepaymentFrequency(loanPlan.getRepaymentFrequency());
	    }

	    if (loanPlan.getPenaltyRate() != null) {
	        LoanPlanValidation.validatePenaltyRate(loanPlan.getPenaltyRate());
	        oldLoanPlan.setPenaltyRate(loanPlan.getPenaltyRate());
	    }

	    if (loanPlan.getPrePaymentPenalty() != null) {
	        LoanPlanValidation.validatePrePaymentPenalty(loanPlan.getPrePaymentPenalty());
	        oldLoanPlan.setPrePaymentPenalty(loanPlan.getPrePaymentPenalty());
	    }

	    if (loanPlan.getGracePeriod() != 0) {
	        LoanPlanValidation.validateGracePeriod(loanPlan.getGracePeriod());
	        oldLoanPlan.setGracePeriod(loanPlan.getGracePeriod());
	    }

	    return loanPlanRepository.save(oldLoanPlan);
	}

	

}
