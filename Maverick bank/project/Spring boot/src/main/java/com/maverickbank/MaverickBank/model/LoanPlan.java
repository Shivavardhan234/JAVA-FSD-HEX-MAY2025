package com.maverickbank.MaverickBank.model;

import java.math.BigDecimal;

import com.maverickbank.MaverickBank.enums.LoanType;
import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name="loan_plan")
public class LoanPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="loan_name", nullable=false,unique=true)
	private String loanName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="loan_type", nullable=false)
	private LoanType loanType;
	
	
	@Column(name="principal_amount", nullable=false)
	private BigDecimal principalAmount;
	
	
	@Column(name="loan_term", nullable=false)
	private int loanTerm;
	
	
	@Column(name="interest_rate", nullable=false)
	private BigDecimal interestRate;
	
	
	@Column(name="installment_amount", nullable=false)
	private BigDecimal installmentAmount;
	
	
	@Column(name="repayment_frequency", nullable=false)
	private int repaymentFrequency;
	
	
	@Column(name="penalty_rate", nullable=false)
	private BigDecimal penaltyRate;
	
	
	@Column(name="pre_payment_penalty", nullable=false)
	private BigDecimal prePaymentPenalty;
	
	
	@Column(name="grace_period", nullable=false)
	private int gracePeriod;
	
	
	
	
	//----------------------------------------- Constructor -------------------------------------------------------
	public LoanPlan() {}
	
	
	public LoanPlan(int id,String loanName, LoanType loanType, BigDecimal principalAmount, int loanTerm, BigDecimal interestRate,
			BigDecimal installmentAmount, int repaymentFrequency, BigDecimal penaltyRate,
			BigDecimal prePaymentPenalty, int gracePeriod) throws InvalidInputException {
		this.setId(id);
		this.setLoanName(loanName);
		this.setLoanType(loanType);
		this.setPrincipalAmount(principalAmount);
		this.setLoanTerm(loanTerm);
		this.setIntrestRate(interestRate);
		this.setInstallmentAmount(installmentAmount);
		this.setRepaymentFrequency(repaymentFrequency);
		this.setPenaltyRate(penaltyRate);
		this.setPrePaymentPenalty(prePaymentPenalty);
		this.setGracePeriod(gracePeriod);
	}

//------------------------------------------------- Getters & Setters ---------------------------------------------
	public int getId() {return id;}
	public String getLoanName() {return loanName;}
	public LoanType getLoanType() {return loanType;}
	public BigDecimal getPrincipalAmount() {return principalAmount;}
	public int getLoanTerm() {return loanTerm;}
	public BigDecimal getIntrestRate() {return interestRate;}
	public BigDecimal getInstallmentAmount() {return installmentAmount;}
	public int getRepaymentFrequency() {return repaymentFrequency;}
	public BigDecimal getPenaltyRate() {return penaltyRate;}
	public BigDecimal getPrePaymentPenalty() {return prePaymentPenalty;}
	public int getGracePeriod() {return gracePeriod;}


	public void setId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Loan plan ID is Invalid. Please enter appropriate Loan plan ID...!!!");
		}
		this.id = id;
	}

	public void setLoanName(String loanName) throws InvalidInputException {
		if(loanName==null || loanName.trim().isEmpty() ) {
			throw new InvalidInputException("Invalid loan name provided. Please provide appropriate loan name...!!!");
		}
		this.loanName = loanName;
	}

	public void setLoanType(LoanType loanType) throws InvalidInputException {
		if(loanType==null ) {
			throw new InvalidInputException("Invalid loan type provided. Please provide appropriate loan type...!!!");
		}
		this.loanType = loanType;
	}


	public void setPrincipalAmount(BigDecimal principalAmount) throws InvalidInputException{
		if(principalAmount==null || principalAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidInputException("Invalid principal amount provided. Please provide appropriate principal amount...!!!");
		}
		this.principalAmount = principalAmount;
	}


	public void setLoanTerm(int loanTerm)throws InvalidInputException {
		if( loanTerm<=0) {
			throw new InvalidInputException("Loan term  is Invalid. Please enter appropriate Loan term...!!!");
		}
		this.loanTerm = loanTerm;
	}


	public void setIntrestRate(BigDecimal intrestRate)throws InvalidInputException {
		if(intrestRate==null || intrestRate.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidInputException("Invalid intrest rate provided. Please provide appropriate intrest rate...!!!");
		}
		this.interestRate = intrestRate;
	}


	public void setInstallmentAmount(BigDecimal installmentAmount) throws InvalidInputException{
		if(installmentAmount==null || installmentAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidInputException("Invalid installment amount provided. Please provide appropriate installment amount...!!!");
		}
		this.installmentAmount = installmentAmount;
	}


	public void setRepaymentFrequency(int repaymentFrequency) throws InvalidInputException{
		if( repaymentFrequency<=0) {
			throw new InvalidInputException("Repayment frequency  is Invalid. Please enter appropriate repayment frequency...!!!");
		}
		this.repaymentFrequency = repaymentFrequency;
	}


	public void setPenaltyRate(BigDecimal penaltyRate) throws InvalidInputException{
		if(penaltyRate==null || penaltyRate.compareTo(BigDecimal.ZERO) < 0 ) {
			throw new InvalidInputException("Invalid penalty rate provided. Please provide appropriate penalty rate...!!!");
		}
		this.penaltyRate = penaltyRate;
	}


	public void setPrePaymentPenalty(BigDecimal prePaymentPenalty) throws InvalidInputException{
		if(prePaymentPenalty==null || prePaymentPenalty.compareTo(BigDecimal.ZERO) < 0 ) {
			throw new InvalidInputException("Invalid pre payment penalty provided. Please provide appropriate pre payment penalty...!!!");
		}
		this.prePaymentPenalty = prePaymentPenalty;
	}


	public void setGracePeriod(int gracePeriod) throws InvalidInputException{
		if( gracePeriod<0) {
			throw new InvalidInputException("Grace period is Invalid. Please enter appropriate Grace period...!!!");
		}
		this.gracePeriod = gracePeriod;
	}
	
	

}
