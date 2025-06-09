package com.maverickbank.MaverickBank.model;

import java.math.BigDecimal;

import com.maverickbank.MaverickBank.enums.AccountStatus;
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


@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="account_number",unique=true,nullable=false)
	private String accountNumber;
	
	@Column(name="account_name")
	private String accountName;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Branch branch;
	@Column(nullable=false)
	private BigDecimal balance;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private AccountStatus accountStatus;
	@ManyToOne
	@JoinColumn(name="account_type_id",nullable=false)
	private AccountType accountType;
	@Column(name="kyc_compliant",nullable=false)
	private boolean kycCompliant;
	
	
	
	
	//----------------------------constructors---------------------------------------------------
	


	public Account() {}
	
	
	
	
	
	



	
	
	public Account(int id, String accountNumber, String accountName, Branch branch, BigDecimal balance,
			AccountStatus accountStatus, AccountType accountType) throws InvalidInputException {
		this.id = id;
		this.setAccountNumber(accountNumber);
		  this.setAccountName(accountName);
		  this.setBranch(branch);
		  this.setBalance(balance);
		  this.setAccountStatus(accountStatus);
		  this.setAccountType(accountType);
	}











	//-----------------------------  Getters & Setters ----------------------------------------------
	
	public int getId() {return id;}
	public String getAccountNumber() { return accountNumber; }
    public String getAccountName() { return accountName; }
    public Branch getBranch() { return branch; }
    public BigDecimal getBalance() { return balance; }
    public AccountStatus getAccountStatus() { return accountStatus; }
    public AccountType getAccountType() { return accountType; }
    public boolean getKycCompliant() {return kycCompliant;}




	


    public void setId(int id) throws InvalidInputException  {
		if( id<=0) {
			throw new InvalidInputException("Account ID is Invalid. Please enter appropriate Account ID...!!!");
		}
		this.id = id;
	}

	



	public void setAccountNumber(String accountNumber) throws InvalidInputException {
		if(accountNumber==null || accountNumber.trim().isEmpty() || !accountNumber.matches("^[0-9]{11}$")) {
			throw new InvalidInputException("Invalid account number provided. Please provide appropriate account number...!!!");
		}
		this.accountNumber = accountNumber;
	}




	public void setAccountName(String accountName) throws InvalidInputException{
		if( accountName.trim().isEmpty() ) {
			throw new InvalidInputException("Invalid account name  provided. Please provide appropriate account name...!!!");
		}
		this.accountName = accountName;
	}




	public void setBranch(Branch branch)throws InvalidInputException {
		if(branch==null ) {
			throw new InvalidInputException("Invalid branch object provided. Please provide appropriate branch object...!!!");
		}
		this.branch = branch;
	}




	public void setBalance(BigDecimal balance) throws InvalidInputException{
		if(balance==null || balance.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidInputException("Invalid balance provided. Please provide appropriate account balance...!!!");
		}
		this.balance = balance;
	}


	public void setAccountStatus(AccountStatus accountStatus) throws InvalidInputException{
		if(accountStatus==null) {
			throw new InvalidInputException("Null account status provided. Account status should not be null...!!!");
		}
		this.accountStatus = accountStatus;
	}




	
	
	
	public void setAccountType(AccountType accountType) throws InvalidInputException {
        if (accountType == null) {
            throw new InvalidInputException("Null account type object provided. Please provide appropriate account type object...!!!");
        }
        this.accountType = accountType;
    }
	


	public void setKycCompliant(boolean kycCompliant) {
		this.kycCompliant = kycCompliant;
	}









	
	
	
	
	
	
	
	
	
	
}
