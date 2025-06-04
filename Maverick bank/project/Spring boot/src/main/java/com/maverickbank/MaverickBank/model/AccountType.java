package com.maverickbank.MaverickBank.model;

import java.math.BigDecimal;

import com.maverickbank.MaverickBank.enums.BankAccountType;
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
@Table(name="account_type")
public class AccountType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Enumerated(EnumType.STRING)
	@Column(name= "account_type",nullable=false)
	private BankAccountType accountType;
	
	@Column(name= "interest_rate",nullable=false)
	private BigDecimal interestRate;
	
	@Column(name= "minimum_balance",nullable=false)
	private BigDecimal minimumBalance;
	
	@Column(name= "transaction_limit",nullable=false)
	private int transactionLimit;
	
	@Column(name= "transaction_amount_limit",nullable=false)
	private BigDecimal transactionAmountLimit;
	
	@Column(name= "withdraw_limit",nullable=false)
	private int withdrawLimit;
	
	
	//----------------------------------- Default Constructor -----------------------------------
    public AccountType() {}

    //--------------------------- Parameterized Constructor -------------------------------------
    public AccountType(int id, BankAccountType accountType, BigDecimal interestRate, 
                       BigDecimal minimumBalance, int transactionLimit, 
                       BigDecimal transactionAmountLimit, int withdrawLimit) throws InvalidInputException {
        this.setAccountTypeId(id);
        this.setAccountType(accountType);
        this.setInterestRate(interestRate);
        this.setMinimumBalance(minimumBalance);
        this.setTransactionLimit(transactionLimit);
        this.setTransactionAmountLimit(transactionAmountLimit);
        this.setWithdrawLimit(withdrawLimit);
    }

    // ----------------------------------- Getters -----------------------------------
    public int getAccountTypeId() { return id; }
    public BankAccountType getAccountType() { return accountType; }
    public BigDecimal getInterestRate() { return interestRate; }
    public BigDecimal getMinimumBalance() { return minimumBalance; }
    public int getTransactionLimit() { return transactionLimit; }
    public BigDecimal getTransactionAmountLimit() { return transactionAmountLimit; }
    public int getWithdrawLimit() { return withdrawLimit; }

    // ----------------------------------- Setters -----------------------------------

    public void setAccountTypeId(int id) throws InvalidInputException {
        if (id <= 0) {
            throw new InvalidInputException("Account Type ID is invalid. It must be greater than zero...!!!");
        }
        this.id = id;
    }

    public void setAccountType(BankAccountType accountType) throws InvalidInputException {
        if (accountType == null) {
            throw new InvalidInputException("Account Type cannot be null.");
        }
        this.accountType = accountType;
    }

    public void setInterestRate(BigDecimal interestRate) throws InvalidInputException {
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Invalid intrest rate provided. Please provide appropriate intrest rate...!!!");
        }
        this.interestRate = interestRate;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) throws InvalidInputException {
        if (minimumBalance == null || minimumBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Invalid Minimum balance provided. Please provide appropriate min balance...!!!");
        }
        this.minimumBalance = minimumBalance;
    }

    public void setTransactionLimit(int transactionLimit) throws InvalidInputException {
        if (transactionLimit < 0) {
            throw new InvalidInputException("Invalid transaction limit provided. Transaction limit should not be less than zero...!!!");
        }
        this.transactionLimit = transactionLimit;
    }

    public void setTransactionAmountLimit(BigDecimal transactionAmountLimit) throws InvalidInputException {
        if (transactionAmountLimit == null || transactionAmountLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Invalid transaction amount limit provided. Please provide appropriate transaction amount limit...!!!");
        }
        this.transactionAmountLimit = transactionAmountLimit;
    }

    public void setWithdrawLimit(int withdrawLimit) throws InvalidInputException {
        if (withdrawLimit < 0) {
            throw new InvalidInputException("Invalid Withdraw limit provided. Withdraw limit should not be less than zero...!!!");
        }
        this.withdrawLimit = withdrawLimit;
    }
    
    
}
