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
@Table(name="loan_closure_request")
public class LoanClosureRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	@JoinColumn(nullable=false)
	private Loan loan;
	private String purpose;
	@Enumerated(EnumType.STRING)
	@Column(name="request_status",nullable=false)
	private ApplicationStatus requestStatus;
	
	
	
	public LoanClosureRequest() {}



	public LoanClosureRequest(int id, Loan loan, String purpose, ApplicationStatus requestStatus) throws InvalidInputException {
		this.id = id;
		this.loan = loan;
		this.purpose = purpose;
		this.requestStatus = requestStatus;
	}



	public int getId() {return id;}
	public Loan getLoan() {return loan;}
	public String getPurpose() {return purpose;}
	public ApplicationStatus getRequestStatus() {return requestStatus;}



	public void setId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Loan closure ID is Invalid. Please enter appropriate ID...!!!");
		}
		this.id = id;
	}



	public void setLoan(Loan loan) throws InvalidInputException {
		if(loan==null ) {
			throw new InvalidInputException("Invalid loan object provided. Please provide appropriate loan object...!!!");
		}
		this.loan = loan;
	}



	public void setPurpose(String purpose) throws InvalidInputException {
		this.purpose = purpose;
	}



	public void setRequestStatus(ApplicationStatus requestStatus) throws InvalidInputException {
		if(requestStatus==null ) {
			throw new InvalidInputException("Invalid request status provided. Please provide appropriate request status...!!!");
		}
		this.requestStatus = requestStatus;
	}
	
	
	
	
	
	
	
	
}
