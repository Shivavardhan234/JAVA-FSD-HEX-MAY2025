package com.maverickbank.MaverickBank.dto;

import java.time.LocalDateTime;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;



public class AccountOpeningApplicationDto {
	
	private String branch;
	private BankAccountType accountType;
	private String accountName;
	private String purpose;
	private ApplicationStatus customerApprovalStatus;
	private ApplicationStatus employeeApprovalStatus;
	public AccountOpeningApplicationDto() {
		super();
	}
	private LocalDateTime applicationDateTime;
	
	
	public AccountOpeningApplicationDto(String branch, BankAccountType accountType, String accountName, String purpose,
			ApplicationStatus customerApprovalStatus, ApplicationStatus employeeApprovalStatus,
			LocalDateTime applicationDateTime) {
		super();
		this.branch = branch;
		this.accountType = accountType;
		this.accountName = accountName;
		this.purpose = purpose;
		this.customerApprovalStatus = customerApprovalStatus;
		this.employeeApprovalStatus = employeeApprovalStatus;
		this.applicationDateTime = applicationDateTime;
	}
	
	public AccountOpeningApplicationDto(AccountOpeningApplication accountOpeningApplication) {
		super();
		this.branch = accountOpeningApplication.getBranch().getBranchName();
		this.accountType = accountOpeningApplication.getAccountType().getAccountType();
		this.accountName = accountOpeningApplication.getAccountName();
		this.purpose = accountOpeningApplication.getPurpose();
		this.customerApprovalStatus = accountOpeningApplication.getCustomerApprovalStatus();
		this.employeeApprovalStatus = accountOpeningApplication.getEmployeeApprovalStatus();
		this.applicationDateTime = accountOpeningApplication.getApplicationDateTime();
	}
	
	
	
	
	
	public String getBranch() {
		return branch;
	}
	public BankAccountType getAccountType() {
		return accountType;
	}
	public String getAccountName() {
		return accountName;
	}
	public String getPurpose() {
		return purpose;
	}
	public ApplicationStatus getCustomerApprovalStatus() {
		return customerApprovalStatus;
	}
	public ApplicationStatus getEmployeeApprovalStatus() {
		return employeeApprovalStatus;
	}
	public LocalDateTime getApplicationDateTime() {
		return applicationDateTime;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public void setAccountType(BankAccountType accountType) {
		this.accountType = accountType;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public void setCustomerApprovalStatus(ApplicationStatus customerApprovalStatus) {
		this.customerApprovalStatus = customerApprovalStatus;
	}
	public void setEmployeeApprovalStatus(ApplicationStatus employeeApprovalStatus) {
		this.employeeApprovalStatus = employeeApprovalStatus;
	}
	public void setApplicationDateTime(LocalDateTime applicationDateTime) {
		this.applicationDateTime = applicationDateTime;
	}
	
	
	
	
	

}
