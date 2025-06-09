package com.maverickbank.MaverickBank.model;


import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.users.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="customer_account")
public class CustomerAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(nullable=false)
	private Customer customer;
	@ManyToOne
	@JoinColumn(name="account_holder_id",nullable=false)
	private AccountHolder accountHolder;
	@ManyToOne
	@JoinColumn(nullable=false)
	private Account account;
	@Column(nullable=false)
	private String pin;

	
//--------------------------------------- Constructor -----------------------------------------------------------
	public CustomerAccount() {}



	
	public CustomerAccount(int id, Customer customer,AccountHolder accountHolder, Account account,String pin) throws InvalidInputException {
	this.setId(id);
	this.setCustomer(customer);
	this.setAccountHolder(accountHolder);
	this.setAccount(account);
	this.setPin(pin);
	}



//---------------------------------- Getters & Setters -----------------------------------------------------------




	public int getId() {return id;}
	public Customer getCustomer() {return customer;}
	public AccountHolder getAccountHolder() {return accountHolder;}
	public Account getAccount() {return account;}
    public String getPin() { return pin; }
	
	
	
	public void setId(int id) throws InvalidInputException  {
		if( id<=0) {
			throw new InvalidInputException("Customer_Account ID is Invalid. Please enter appropriate Customer_Account ID...!!!");
		}
		this.id = id;
	}


	public void setCustomer(Customer customer) throws InvalidInputException {
		if (customer==null) {
			throw new InvalidInputException("Provided customer object is null. Please provide appropriate customer object...!!!");
		}
		this.customer = customer;
	}

	
	public void setAccountHolder(AccountHolder accountHolder) throws InvalidInputException {
		if(accountHolder==null ) {
			throw new InvalidInputException("Invalid account holder object provided. Please provide appropriate accountHolder object...!!!");
		}
		this.accountHolder = accountHolder;
	}
	



	public void setAccount(Account account) throws InvalidInputException {
		if (account==null) {
			throw new InvalidInputException("Provided account object is null. Please provide appropriate account object...!!!");
		}
		this.account = account;
	}

	public void setPin(String pin) throws InvalidInputException{
		if(pin==null || pin.trim().isEmpty() || !pin.matches("^[0-9]{6}$")) {
			throw new InvalidInputException("Invalid pin number provided. Please provide appropriate pin number...!!!");
		}
		this.pin = pin;
	}



	
}
