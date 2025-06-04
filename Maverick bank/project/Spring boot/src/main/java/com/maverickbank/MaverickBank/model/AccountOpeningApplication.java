package com.maverickbank.MaverickBank.model;



import java.time.LocalDateTime;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.users.Customer;

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
@Table(name="account_opening_application")
public class AccountOpeningApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Branch branch;
	@ManyToOne
	@JoinColumn(name="account_type_id",nullable=false)
	private AccountType accountType;
	@Column(name="account_name")
	private String accountName;
	private String purpose;
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private ApplicationStatus status;
	
	@Column(name="application_date_time",nullable=false)
	private LocalDateTime applicationDateTime;
	
	
//-------------------------------------------- Constructors --------------------------------------------------------	
	public AccountOpeningApplication() {}



	public AccountOpeningApplication(int id, Branch branch, AccountType accountType,String accountName, String purpose,
			ApplicationStatus status,LocalDateTime applicationDateTime) throws InvalidInputException {
		this.setId(id);
		this.setBranch(branch);
		this.accountName=accountName;
		this.setPurpose(purpose);
		this.setStatus(status);
	}


// ------------------------------------Getters & Setters -----------------------------------------------------------


	public int getId() {return id;}
	public Branch getBranch() {return branch;}
	public AccountType getAccountType() {return accountType;}
	public String getAccountName() {return accountName;}
	public String getPurpose() {return purpose;}
	public ApplicationStatus getStatus() {return status;}
	public LocalDateTime getApplicationDateTime() {return applicationDateTime;}


	public void setId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Account application ID is Invalid. Please enter appropriate ID...!!!");
		}
		this.id = id;
	}





	public void setBranch(Branch branch) throws InvalidInputException{
		if(branch==null ) {
			throw new InvalidInputException("Invalid branch object provided. Please provide appropriate branch object...!!!");
		}
		this.branch = branch;
	}



	public void setAccountType(AccountType accountType) throws InvalidInputException{
		if(accountType==null ) {
			throw new InvalidInputException("Invalid account type object provided. Please provide appropriate account type object...!!!");
		}
		this.accountType = accountType;
	}


	
	public void setAccountName(String accountName) throws InvalidInputException{
		if(accountName==null ) {
			throw new InvalidInputException("Invalid account name provided. Please provide appropriate account name...!!!");
		}
		this.accountName = accountName;
	}
	
	

	public void setPurpose(String purpose) throws InvalidInputException{
		if(purpose==null ) {
			throw new InvalidInputException("Invalid purpose provided. Please provide appropriate purpose...!!!");
		}
		this.purpose = purpose;
	}



	public void setStatus(ApplicationStatus status) throws InvalidInputException{
		if(status==null) {
			throw new InvalidInputException("Null application status provided. Application status should not be null...!!!");
		}
		this.status = status;
	}



	public void setApplicationDateTime(LocalDateTime applicationDateTime) throws InvalidInputException {
		if(applicationDateTime==null) {
			throw new InvalidInputException("Null application date time provided. Application date time should not be null...!!!");
		}
		this.applicationDateTime = applicationDateTime;
	}
	
	
	
}
