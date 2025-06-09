package com.maverickbank.MaverickBank.model;

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
@Table(name="customer_account_opening_application")
public class CustomerAccountOpeningApplication {
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
	private AccountOpeningApplication accountOpeningApplication;
	@Enumerated(EnumType.STRING)
	@Column(name="customer_approval", nullable=false)
	private ApplicationStatus customerApproval;
	
	
	//--------------------------Constructors--------------------------------------------------------
	public CustomerAccountOpeningApplication() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerAccountOpeningApplication(int id, Customer customer,AccountHolder accountHolder,
			AccountOpeningApplication accountOpeningApplication, ApplicationStatus customerApproval) throws InvalidInputException {
		this.setId(id);
		this.setCustomer(customer);
		this.setAccountOpeningApplication(accountOpeningApplication);
		this.setCustomerApproval(customerApproval);
		this.setAccountHolder(accountHolder);
	}
	
	
	
	//-------------------------------Getters and setters---------------------------------------------
	public int getId() {return id;}
	public Customer getCustomer() {return customer;}
	public AccountHolder getAccountHolder() {return accountHolder;}
	public AccountOpeningApplication getAccountOpeningApplication() {return accountOpeningApplication;}
	public ApplicationStatus getCustomerApproval() {return customerApproval;}
	
	
	
	
	public void setId(int id)  throws InvalidInputException{
		this.id = id;
	}
	
	
	public void setCustomer(Customer customer) throws InvalidInputException {
		if(customer==null ) {
			throw new InvalidInputException("Invalid customer object provided. Please provide appropriate customer object...!!!");
		}
		this.customer = customer;
	}
	
	
	
	public void setAccountHolder(AccountHolder accountHolder) throws InvalidInputException {
		if(accountHolder==null ) {
			throw new InvalidInputException("Invalid account holder object provided. Please provide appropriate accountHolder object...!!!");
		}
		this.accountHolder = accountHolder;
	}
	
	
	
	
	public void setAccountOpeningApplication(AccountOpeningApplication accountOpeningApplication) throws InvalidInputException {
		if(accountOpeningApplication==null ) {
			throw new InvalidInputException("Invalid account opening application object provided. Please provide appropriate object...!!!");
		}
		this.accountOpeningApplication = accountOpeningApplication;
	}
	
	
	public void setCustomerApproval(ApplicationStatus customerApproval) throws InvalidInputException {
		if(customerApproval==null ) {
			throw new InvalidInputException("Invalid customer approval provided. Please provide appropriate customer approval...!!!");
		}
		this.customerApproval = customerApproval;
	}
	
	
	
	
}
