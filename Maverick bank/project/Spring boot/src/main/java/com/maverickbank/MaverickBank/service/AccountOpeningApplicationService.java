package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.BranchRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.AccountOpeningApplicationValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;


@Service
public class AccountOpeningApplicationService {

	
	AccountOpeningApplicationRepository   accountOpeningApplicationRepository;
	UserRepository                        userRepository;
	BranchService                         branchService;
	AccountTypeService                    accountTypeService;
	BranchRepository                      branchRepository;
	
	

	


	
public AccountOpeningApplicationService(AccountOpeningApplicationRepository accountOpeningApplicationRepository,
			UserRepository userRepository, BranchService branchService, AccountTypeService accountTypeService, BranchRepository branchRepository) {
		this.accountOpeningApplicationRepository = accountOpeningApplicationRepository;
		this.userRepository = userRepository;
		this.branchService = branchService;
		this.accountTypeService = accountTypeService;
		this.branchRepository = branchRepository;
	}







//--------------------------------------------------- POST ---------------------------------------------------------------

	





	public AccountOpeningApplication addAccountOpeningApplication(AccountOpeningApplication accountOpeningApplication,Principal principal) throws InvalidInputException,ResourceNotFoundException, InvalidActionException, DeletedUserException {
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				//Validate account opening application object
				AccountOpeningApplicationValidation.validateAccountOpeningApplication(accountOpeningApplication);
				//Fetch and attach branch
				Branch branch = branchService.getByName(accountOpeningApplication.getBranch().getBranchName(), principal);
				
				//Set branch
				accountOpeningApplication.setBranch(branch);
				
				AccountType accountType = accountTypeService.getAccountTypeByName(accountOpeningApplication.getAccountType().getAccountType(), principal);
				
				//Set account type
				accountOpeningApplication.setAccountType(accountType);
				
				
				
				accountOpeningApplication.setEmployeeApprovalStatus(ApplicationStatus.PENDING);
				accountOpeningApplication.setApplicationDateTime(LocalDateTime.now());
		
		return accountOpeningApplicationRepository.save(accountOpeningApplication);
	}







	
	
	//---------------------------------------------- GET -----------------------------------------------------------------

	
	


	public List<AccountOpeningApplication> getAllAccountOpeningApplication(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		return accountOpeningApplicationRepository.findAll();
	}







	public AccountOpeningApplication getAccountOpeningApplicationById(int id,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		return accountOpeningApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Account application record with the given id...!!!"));
	}







	public List<AccountOpeningApplication> getAccountOpeningApplicationByBranch(Branch branch, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		if(branch.getId()>0) {
			branch=branchService.getById(branch.getId(), principal);
			
		}
		else if(branch.getBranchName()!=null) {
			branch=branchService.getByName(branch.getBranchName(), principal);
			
		}
		else if(branch.getIfsc()!=null) {
			branch=branchRepository.getBranchByIfsc(branch.getIfsc());
			
		}else {
			throw new InvalidInputException("Invalid branch details. Provide appropriate branch details...!!!");
		}
		
		List<AccountOpeningApplication> accountOpeningApplicationList=accountOpeningApplicationRepository.getAccountOpeningApplicationByBranchId(branch.getId());
		if(accountOpeningApplicationList==null) {
			throw new ResourceNotFoundException("No Account application records with the given branch...!!!");
			
		}
		return accountOpeningApplicationList;
	}

	
	
	
	
	
	public List<AccountOpeningApplication> getAccountOpeningApplicationByAccountType(AccountType accountType,
			Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				
				if(accountType.getAccountTypeId()>0) {
					accountType=accountTypeService.getAccountTypeById(accountType.getAccountTypeId(), principal);
					
				}
				else if(accountType.getAccountType()!=null) {
					accountType=accountTypeService.getAccountTypeByName(accountType.getAccountType(), principal);
					
				}else {
					throw new InvalidInputException("Invalid account type details. Provide appropriate account type details...!!!");
				}
				
				List<AccountOpeningApplication> accountOpeningApplicationList=accountOpeningApplicationRepository.getAccountOpeningApplicationByAccountTypeId(accountType.getAccountTypeId());
				if(accountOpeningApplicationList==null) {
					throw new ResourceNotFoundException("No Account application records with the given account type...!!!");
					
				}
				return accountOpeningApplicationList;
	}






	public List<AccountOpeningApplication> getAccountOpeningApplicationByCustomerApprovalStatus(
			ApplicationStatus customerApprovalStatus, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		List<AccountOpeningApplication> accountOpeningApplicationList=accountOpeningApplicationRepository.getAccountOpeningApplicationByCustomerApprovalStatus(customerApprovalStatus);
		if(accountOpeningApplicationList==null) {
			throw new ResourceNotFoundException("No Account application records with the given customer approval status...!!!");
			
		}
		return accountOpeningApplicationList;
	}







	public List<AccountOpeningApplication> getAccountOpeningApplicationByDate(LocalDate date, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		if(date==null) {
			throw new InvalidInputException("Invalid null date provided...!!!");
		}
		LocalDateTime start= date.atStartOfDay();
		LocalDateTime end= date.plusDays(1).atStartOfDay();
		List<AccountOpeningApplication> accountOpeningApplicationList=accountOpeningApplicationRepository.getAccountOpeningApplicationByDate(start,end);
		if(accountOpeningApplicationList==null) {
			throw new ResourceNotFoundException("No Account application records with the given date...!!!");
			
		}
		return accountOpeningApplicationList;
	}







	public List<AccountOpeningApplication> getAccountOpeningApplicationByEmployeeApprovalStatus(
			ApplicationStatus employeeApprovalStatus, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		List<AccountOpeningApplication> accountOpeningApplicationList=accountOpeningApplicationRepository.getAccountOpeningApplicationByEmployeeApprovalStatus(employeeApprovalStatus);
		if(accountOpeningApplicationList==null) {
			throw new ResourceNotFoundException("No Account application records with the employee approval status...!!!");
			
		}
		return accountOpeningApplicationList;
	}
	
	
	
	
	
	//----------------------------------------------- PUT ----------------------------------------------------------------
	public AccountOpeningApplication updateCustomerApprovalStatus(int id,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		return null;
	}







	public AccountOpeningApplication approveApplication(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				
		AccountOpeningApplication accountOpeningApplication= accountOpeningApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Account application record with the given id...!!!"));
		
		if(accountOpeningApplication.getCustomerApprovalStatus()==ApplicationStatus.REJECTED ||
				accountOpeningApplication.getCustomerApprovalStatus()==ApplicationStatus.PENDING ) {
			throw new InvalidActionException("This account opening application is not eligible to approve...!!!  ");
			
		}
		
		if(accountOpeningApplication.getEmployeeApprovalStatus()==ApplicationStatus.REJECTED) {
			throw new InvalidActionException("This account opening application is already REJECTED.Cannot perform action...!!!  ");
		}
		else if(accountOpeningApplication.getEmployeeApprovalStatus()==ApplicationStatus.ACCEPTED) {
			throw new InvalidActionException("This account opening application is already ACCEPTED...!!!  ");
			
		}
		accountOpeningApplication.setEmployeeApprovalStatus(ApplicationStatus.ACCEPTED);
		return accountOpeningApplicationRepository.save(accountOpeningApplication);
	}
	
	
	
	
	
	
	
	
	public AccountOpeningApplication rejectApplication(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				
		AccountOpeningApplication accountOpeningApplication= accountOpeningApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Account application record with the given id...!!!"));
		
		if(accountOpeningApplication.getCustomerApprovalStatus()==ApplicationStatus.REJECTED ||
				accountOpeningApplication.getCustomerApprovalStatus()==ApplicationStatus.PENDING ) {
			throw new InvalidActionException("This account opening application is not eligible to reject...!!!  ");
			
		}
		
		if(accountOpeningApplication.getEmployeeApprovalStatus()==ApplicationStatus.REJECTED) {
			throw new InvalidActionException("This account opening application is already REJECTED.Cannot perform action...!!!  ");
		}
		else if(accountOpeningApplication.getEmployeeApprovalStatus()==ApplicationStatus.ACCEPTED) {
			throw new InvalidActionException("This account opening application is already ACCEPTED...!!!  ");
			
		}
		accountOpeningApplication.setEmployeeApprovalStatus(ApplicationStatus.REJECTED);
		return accountOpeningApplicationRepository.save(accountOpeningApplication);
	}







	






	
}
