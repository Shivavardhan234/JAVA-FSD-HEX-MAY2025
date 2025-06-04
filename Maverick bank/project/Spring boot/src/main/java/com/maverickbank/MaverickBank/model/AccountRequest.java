package com.maverickbank.MaverickBank.model;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.RequestType;
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
@Table(name="account_request")
public class AccountRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	@JoinColumn(nullable=false)
	private Account account;
	
	private String purpose;
	@Enumerated(EnumType.STRING)
	@Column(name="request_status",nullable=false)
	private ApplicationStatus requestStatus;
	@Enumerated(EnumType.STRING)
	@Column(name="request_type",nullable=false)
	private RequestType requestType;
	
// ----------------------------------------- Constructors -------------------------------------------------------
	public AccountRequest() {}
	
	public AccountRequest(int id, Account account, String purpose, ApplicationStatus requestStatus,
			RequestType requestType) throws InvalidInputException{
		this.setId(id);
		this.setAccount(account);
		this.setPurpose(purpose);
		this.setRequestStatus(requestStatus);
		this.setRequestType(requestType);
	}
	
	
// ----------------------------------------- Getters & Setters -------------------------------------------------
	public int getId() {return id;}
	public Account getAccount() {return account;}
	public String getPurpose() {return purpose;}
	public ApplicationStatus getRequestStatus() {return requestStatus;}
	public RequestType getRequestType() {return requestType;}
	
	
	
	public void setId(int id)throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Account request ID is Invalid. Please enter appropriate ID...!!!");
		}
		this.id = id;
	}
	
	
	public void setAccount(Account account) throws InvalidInputException{
		if(account==null ) {
			throw new InvalidInputException("Invalid account object provided. Please provide appropriate account object...!!!");
		}
		this.account = account;
	}
	
	
	public void setPurpose(String purpose) throws InvalidInputException{
		this.purpose = purpose;
	}
	
	
	public void setRequestStatus(ApplicationStatus requestStatus) throws InvalidInputException{
		if(requestStatus==null) {
			throw new InvalidInputException("Null account request status provided. Account request status should not be null...!!!");
		}
		this.requestStatus = requestStatus;
	}
	
	
	public void setRequestType(RequestType requestType) throws InvalidInputException{
		if(requestType==null) {
			throw new InvalidInputException("Null request type provided. Request type should not be null...!!!");
		}
		this.requestType = requestType;
	}

}
