package com.maverickbank.MaverickBank.validation;

import java.math.BigDecimal;

import com.maverickbank.MaverickBank.enums.LoanType;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.LoanPlan;

public class LoanPlanValidation {
	public  static void validateId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Loan plan ID is Invalid. Please enter appropriate Loan plan ID...!!!");
		}
		return;
	}

	public  static void validateLoanName(String loanName) throws InvalidInputException {
		if(loanName==null || loanName.trim().isEmpty() ) {
			throw new InvalidInputException("Invalid loan name provided. Please provide appropriate loan name...!!!");
		}
		return;
	}

	public  static void validateLoanType(LoanType loanType) throws InvalidInputException {
		if(loanType==null ) {
			throw new InvalidInputException("Invalid loan type provided. Please provide appropriate loan type...!!!");
		}
		return;
	}


	public  static void validatePrincipalAmount(BigDecimal principalAmount) throws InvalidInputException{
		if(principalAmount==null || principalAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidInputException("Invalid principal amount provided. Please provide appropriate principal amount...!!!");
		}
		return;
	}


	public  static void validateLoanTerm(int loanTerm)throws InvalidInputException {
		if( loanTerm<=0) {
			throw new InvalidInputException("Loan term  is Invalid. Please enter appropriate Loan term...!!!");
		}
		return;
	}


	public  static void validateIntrestRate(BigDecimal intrestRate)throws InvalidInputException {
		if(intrestRate==null || intrestRate.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidInputException("Invalid intrest rate provided. Please provide appropriate intrest rate...!!!");
		}
		return;
	}


	public  static void validateInstallmentAmount(BigDecimal installmentAmount) throws InvalidInputException{
		if(installmentAmount==null || installmentAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidInputException("Invalid installment amount provided. Please provide appropriate installment amount...!!!");
		}
		return;
	}


	public  static void validateRepaymentFrequency(int repaymentFrequency) throws InvalidInputException{
		if( repaymentFrequency<=0) {
			throw new InvalidInputException("Repayment frequency  is Invalid. Please enter appropriate repayment frequency...!!!");
		}
		return;
	}


	public static  void validatePenaltyRate(BigDecimal penaltyRate) throws InvalidInputException{
		if(penaltyRate==null || penaltyRate.compareTo(BigDecimal.ZERO) < 0 ) {
			throw new InvalidInputException("Invalid penalty rate provided. Please provide appropriate penalty rate...!!!");
		}
		return;
	}


	public  static void validatePrePaymentPenalty(BigDecimal prePaymentPenalty) throws InvalidInputException{
		if(prePaymentPenalty==null || prePaymentPenalty.compareTo(BigDecimal.ZERO) < 0 ) {
			throw new InvalidInputException("Invalid pre payment penalty provided. Please provide appropriate pre payment penalty...!!!");
		}
		return;
	}


	public static void validateGracePeriod(int gracePeriod) throws InvalidInputException{
		if( gracePeriod<0) {
			throw new InvalidInputException("Grace period is Invalid. Please enter appropriate Grace period...!!!");
		}
		return;
	}
	
	public static void validateLoanPlanObject(LoanPlan loanPlan) throws InvalidInputException{
		if( loanPlan==null) {
			throw new InvalidInputException("Loan plan is Invalid. Please enter appropriate loan plan...!!!");
		}
		return;
	}
	
	public static void validateLoanPlan(LoanPlan loanPlan) throws InvalidInputException{
		validateLoanPlanObject(loanPlan);
		
		validateLoanName(loanPlan.getLoanName());
		validateLoanType(loanPlan.getLoanType());
		validatePrincipalAmount(loanPlan.getPrincipalAmount());
		validateLoanTerm(loanPlan.getLoanTerm());
		validateIntrestRate(loanPlan.getIntrestRate());
		validateInstallmentAmount(loanPlan.getInstallmentAmount());
		validateRepaymentFrequency(loanPlan.getRepaymentFrequency());
		validatePenaltyRate(loanPlan.getPenaltyRate());
		validatePrePaymentPenalty(loanPlan.getPrePaymentPenalty());
		validateGracePeriod(loanPlan.getGracePeriod());
		return;
	}
	
	
	
	
	
}
