package com.maverickbank.MaverickBank.model;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="loan_opening_application")
public class LoanOpeningApplication {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	@JoinColumn(nullable=false)
	private Account account;
	@ManyToOne
	@JoinColumn(name="loan_plan",nullable=false)
	private LoanPlan loanPlan;
	
	private String purpose;
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private ApplicationStatus status;
	
	
// ---------------------------------------- Constructor -----------------------------------------------------------
	public LoanOpeningApplication() {}


	public LoanOpeningApplication(int id, Account account, LoanPlan loanPlan, String purpose, ApplicationStatus status) throws InvalidInputException {
		this.setId(id);
		this.setAccount(account);
		this.setLoanPlan(loanPlan);
		this.setPurpose(purpose);
		this.setStatus(status);
	}

	
	
// ---------------------------------- Getters & Setters ----------------------------------------------------------

	public int getId() {return id;}
	public Account getAccount() {return account;}
	public LoanPlan getLoanPlan() {return loanPlan;}
	public String getPurpose() {return purpose;}
	public ApplicationStatus getStatus() {return status;}


	public void setId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Loan application ID is Invalid. Please enter appropriate ID...!!!");
		}
		this.id = id;
	}


	public void setAccount(Account account) throws InvalidInputException{
		if(account==null ) {
			throw new InvalidInputException("Invalid account object provided. Please provide appropriate account object...!!!");
		}
		this.account = account;
	}


	public void setLoanPlan(LoanPlan loanPlan) throws InvalidInputException{
		if (loanPlan == null) {
            throw new InvalidInputException("Null loan plan object provided. Please provide appropriate loan plan object...!!!");
        }
		this.loanPlan = loanPlan;
	}


	public void setPurpose(String purpose) throws InvalidInputException{
		this.purpose = purpose;
	}


	public void setStatus(ApplicationStatus status) throws InvalidInputException{
		if(status==null) {
			throw new InvalidInputException("Null loan opening status provided. loan opening status should not be null...!!!");
		}
		this.status = status;
	}
	
	
	
	
	

}
