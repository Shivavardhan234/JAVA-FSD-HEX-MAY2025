package com.maverickbank.MaverickBank.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maverickbank.MaverickBank.dto.AccountOpeningApplicationDto;
import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.exception.IdentityNotMatchException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.CustomerAccountOpeningApplication;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.repository.AccountHolderRepository;
import com.maverickbank.MaverickBank.repository.AccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.AccountTypeRepository;
import com.maverickbank.MaverickBank.repository.BranchRepository;
import com.maverickbank.MaverickBank.repository.CustomerAccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.CustomerRepository;
import com.maverickbank.MaverickBank.validation.AccountOpeningApplicationDtoValidation;



@Service
public class AccountOpeningApplicationService {
	
	AccountOpeningApplicationRepository aoar;
	CustomerRepository cr;
	AccountTypeRepository atr;
	BranchRepository br;
	AccountHolderRepository ahr;
	CustomerAccountOpeningApplicationRepository caoar;

	public AccountOpeningApplicationService(AccountOpeningApplicationRepository aoar, CustomerRepository cr,
			AccountTypeRepository atr, BranchRepository br,AccountHolderRepository ah,CustomerAccountOpeningApplicationRepository caoar, AccountHolderRepository ahr) {
		this.aoar = aoar;
		this.cr = cr;
		this.atr = atr;
		this.br = br;
		this.ahr=ahr;
		this.caoar=caoar;
	}



	


	@Transactional
	public AccountOpeningApplicationDto addAccountOpeningApplication(AccountOpeningApplicationDto applicationDto, String branchName, BankAccountType bankAccountType, String username) throws InvalidInputException,ResourceNotFoundException, Exception {
		
		//Validate Account opening application dto object
		AccountOpeningApplicationDtoValidation.validateAccountOpeningApplicationDto(applicationDto);
		//Check whether given fields are null
		if(branchName==null) {
			throw new InvalidInputException("Invalid Branch Name provided..!!!");
		}
		if(bankAccountType==null) {
			throw new InvalidInputException("Invalid Bank Account Type provided..!!!");
		}
		//Get branch by branch name
		Branch branch=br.getByName(branchName);
		//If no branch available with given name throw exception
		if(branch==null) {
			throw new ResourceNotFoundException("Invalid Branch Name...!!!");
		}
		//Extract Account opening application
		AccountOpeningApplication application=applicationDto.getApplication();
		//Set branch for the application
		application.setBranch(branch);
		//Get account type by its name
		AccountType accountType= atr.getAccountTypeByName(bankAccountType);
		//Check whether account type exists in database
		if(accountType==null) {
			throw new ResourceNotFoundException("Invalid Bank Account type provided Name...!!!");
		}
		//Set account type to the Account opening application
		application.setAccountType(accountType);
		application.setStatus(ApplicationStatus.PENDING);
		
		/*
		 * Now here check if account is joint type, 
		 * if yes account holders should be greater than or equal to 2
		 * then fetch the customer details of all the account holders using their mail and 
		 * store in list of customers
		 * if the current Actor(customer) is not in the list of customers then account opening application cannot be created due to identity invalid
		 * if all the conditions are met then save account holders in account holder and get the ids
		 * save updated account holders in the list 
		 * add application to database
		 * now update CustomerAccountHolderApplication with account holder id, customer id, application id
		 * */
		AccountOpeningApplicationDtoValidation.validateListSize(accountType.getAccountType(), applicationDto.getAccountHolderList());
		
		List<Customer> customerList=new ArrayList<>();
		for(AccountHolder a : applicationDto.getAccountHolderList()) {
			Customer customer=cr.getByContactNumber(a.getContactNumber());
			if(customer==null) {
				throw new ResourceNotFoundException("Cannot submit application,No customer with given phone number"+a.getContactNumber());
			}
			customerList.add(customer);
		}
		Customer presentCustomer=cr.getByUsername(username);
		Boolean isPresent=false;
		for(Customer c: customerList) {
			if(c.equals(presentCustomer)) {
				isPresent=true;
			}
		}
		
		if(isPresent==false) {
			throw new IdentityNotMatchException("Customer's identity is absent in the given account holder list...!!! ");	
		}
		
		application.setApplicationDateTime(LocalDateTime.now());
		
		//Add a condition to check weather application is already available or not
		//Boolean isAlreadyAvailable=true;
		
		
		
		
		//SAVING step1: add account holders
		applicationDto.setAccountHolderList(ahr.saveAll(applicationDto.getAccountHolderList()));
		//SAVING step2: add application
		application=aoar.save(application);
		applicationDto.setApplication(application);
		//SAVING step3: Create list of CustomerAccountOpeningApplication and save
		List<CustomerAccountOpeningApplication> customerAccountOpeningApplication=new ArrayList<>();
		
		
		//Create CustomerAccountOpeningApplication object and add it to list
		for(int i=0;i<customerList.size();i++) {
			AccountHolder accountHolder=applicationDto.getAccountHolderList().get(i);
			CustomerAccountOpeningApplication customerAccountOpeningApplicationTemp=new CustomerAccountOpeningApplication();
			customerAccountOpeningApplicationTemp.setAccountHolder(accountHolder);
			customerAccountOpeningApplicationTemp.setCustomer(customerList.get(i));
			customerAccountOpeningApplicationTemp.setAccountOpeningApplication(application);
			if(customerList.get(i).equals(presentCustomer)) {
				customerAccountOpeningApplicationTemp.setCustomerApproval(ApplicationStatus.ACCEPTED);
			}
			else {
				customerAccountOpeningApplicationTemp.setCustomerApproval(ApplicationStatus.PENDING);
				
			}
			customerAccountOpeningApplication.add(customerAccountOpeningApplicationTemp);
			
		}//For loop
		
		caoar.saveAll(customerAccountOpeningApplication);
		
		return applicationDto;
	}

}
