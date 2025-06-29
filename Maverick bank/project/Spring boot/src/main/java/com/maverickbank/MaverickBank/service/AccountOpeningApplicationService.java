package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.CustomerAccount;
import com.maverickbank.MaverickBank.model.CustomerAccountOpeningApplication;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.BranchRepository;
import com.maverickbank.MaverickBank.repository.CustomerAccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.AccountOpeningApplicationValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;


@Service
public class AccountOpeningApplicationService {

	
	private AccountOpeningApplicationRepository   accountOpeningApplicationRepository;
	private UserRepository                        userRepository;
	private BranchService                         branchService;
	private AccountTypeService                    accountTypeService;
	private BranchRepository                      branchRepository;
	private CustomerAccountService customerAccountService;
	private AccountService accountService;
	private CustomerAccountOpeningApplicationRepository customerAccountOpeningApplicationRepository;
	Logger logger = LoggerFactory.getLogger(AccountOpeningApplicationService.class);


	











public AccountOpeningApplicationService(AccountOpeningApplicationRepository accountOpeningApplicationRepository,
			UserRepository userRepository, BranchService branchService, AccountTypeService accountTypeService,
			BranchRepository branchRepository, CustomerAccountService customerAccountService,
			AccountService accountService,
			CustomerAccountOpeningApplicationRepository customerAccountOpeningApplicationRepository) {
		super();
		this.accountOpeningApplicationRepository = accountOpeningApplicationRepository;
		this.userRepository = userRepository;
		this.branchService = branchService;
		this.accountTypeService = accountTypeService;
		this.branchRepository = branchRepository;
		this.customerAccountService = customerAccountService;
		this.accountService = accountService;
		this.customerAccountOpeningApplicationRepository = customerAccountOpeningApplicationRepository;
	}









//--------------------------------------------------- POST ---------------------------------------------------------------

	





	public AccountOpeningApplication addAccountOpeningApplication(AccountOpeningApplication accountOpeningApplication,Principal principal) throws InvalidInputException,ResourceNotFoundException, InvalidActionException, DeletedUserException {
				logger.info("In add account opening application");
				//Check user is active
		
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				logger.info("User active status validated.");
				//Validate account opening application object
				AccountOpeningApplicationValidation.validateAccountOpeningApplication(accountOpeningApplication);
				logger.info("Account opening application validated.");
				//Fetch and attach branch
				Branch branch = branchService.getByName(accountOpeningApplication.getBranch().getBranchName(), principal);
				logger.info("Successfully fetched branch details");
				//Set branch
				accountOpeningApplication.setBranch(branch);
				
				AccountType accountType = accountTypeService.getAccountTypeByName(accountOpeningApplication.getAccountType().getAccountType(), principal);
				logger.info("Successfully fetched account type details");
				//Set account type
				accountOpeningApplication.setAccountType(accountType);
				
				accountOpeningApplication.setCustomerApprovalStatus(ApplicationStatus.PENDING);
				accountOpeningApplication.setEmployeeApprovalStatus(ApplicationStatus.PENDING);
				accountOpeningApplication.setApplicationDateTime(LocalDateTime.now());
				logger.info("Saving the account opening application in Database");
		return accountOpeningApplicationRepository.save(accountOpeningApplication);
	}







	
	
	//---------------------------------------------- GET -----------------------------------------------------------------

	
	


	public List<AccountOpeningApplication> getAllAccountOpeningApplication(Integer page, Integer size, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		logger.info("In get all account opening applications");
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		logger.info("Verified user active status");
		
		Pageable pageable = PageRequest.of(page, size);
		
		return accountOpeningApplicationRepository.findAll(pageable).getContent();
	}







	public AccountOpeningApplication getAccountOpeningApplicationById(int id,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		logger.info("In get account opening application by id");
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		logger.info("User active status verified");
		
		return accountOpeningApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Account application record with the given id...!!!"));
	}







	public List<AccountOpeningApplication> getAccountOpeningApplicationByBranch(Branch branch, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		logger.info("In get account opening applications by branch");
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		logger.info("User active status verified");
		
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
		if(accountOpeningApplicationList.isEmpty()) {
			throw new ResourceNotFoundException("No Account application records with the given branch...!!!");
			
		}
		logger.info("account opening application list fetched");
		return accountOpeningApplicationList;
	}

	
	
	
	
	
	public List<AccountOpeningApplication> getAccountOpeningApplicationByAccountType(AccountType accountType,
			Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
			logger.info("In get account opening applications by account type");
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				logger.info("User active status verified");
				if(accountType.getAccountTypeId()>0) {
					accountType=accountTypeService.getAccountTypeById(accountType.getAccountTypeId(), principal);
					
				}
				else if(accountType.getAccountType()!=null) {
					accountType=accountTypeService.getAccountTypeByName(accountType.getAccountType(), principal);
					
				}else {
					throw new InvalidInputException("Invalid account type details. Provide appropriate account type details...!!!");
				}
				
				List<AccountOpeningApplication> accountOpeningApplicationList=accountOpeningApplicationRepository.getAccountOpeningApplicationByAccountTypeId(accountType.getAccountTypeId());
				if(accountOpeningApplicationList.isEmpty()) {
					throw new ResourceNotFoundException("No Account application records with the given account type...!!!");
					
				}
				logger.info("account opening applications by account type fetched");
				return accountOpeningApplicationList;
	}






	public List<AccountOpeningApplication> getAccountOpeningApplicationByCustomerApprovalStatus(
			ApplicationStatus customerApprovalStatus, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		logger.info("In get account opening applications by customer approval status");
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		logger.info("verified user active status");
		
		
		List<AccountOpeningApplication> accountOpeningApplicationList=accountOpeningApplicationRepository.getAccountOpeningApplicationByCustomerApprovalStatus(customerApprovalStatus);
		if(accountOpeningApplicationList.isEmpty()) {
			throw new ResourceNotFoundException("No Account application records with the given customer approval status...!!!");
			
		}
		logger.info("Fetched the account opening application list");
		return accountOpeningApplicationList;
	}







	public List<AccountOpeningApplication> getAccountOpeningApplicationByDate(LocalDate date, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		logger.info("In get account opening applications by Date");
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		logger.info("User active status verified");
		if(date==null) {
			throw new InvalidInputException("Invalid null date provided...!!!");
		}
		LocalDateTime start= date.atStartOfDay();
		LocalDateTime end= date.plusDays(1).atStartOfDay();
		List<AccountOpeningApplication> accountOpeningApplicationList=accountOpeningApplicationRepository.getAccountOpeningApplicationByDate(start,end);
		if(accountOpeningApplicationList.isEmpty()) {
			throw new ResourceNotFoundException("No Account application records with the given date...!!!");
			
		}
		logger.info("Fetched account opening applications by date");
		return accountOpeningApplicationList;
	}







	public List<AccountOpeningApplication> getAccountOpeningApplicationByEmployeeApprovalStatus(
			Integer page, Integer size, ApplicationStatus employeeApprovalStatus, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		logger.info("In get account opening applications by Employee approval status");
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		logger.info("User active status verified");
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<AccountOpeningApplication> accountOpeningApplicationList=accountOpeningApplicationRepository.getAccountOpeningApplicationByEmployeeApprovalStatus(employeeApprovalStatus,pageable);
		if(accountOpeningApplicationList.isEmpty()) {
			throw new ResourceNotFoundException("No Account application records with the employee approval status...!!!");
			
		}
		logger.info("Fetched account opening applications by Employee approval status");
		return accountOpeningApplicationList;
	}
	
	
	
	
	
	//----------------------------------------------- PUT ----------------------------------------------------------------
	public AccountOpeningApplication updateCustomerApprovalStatus(int id,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		logger.info("In update customer approval status of account opening application");
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		logger.info("User active status verified");
		AccountOpeningApplication accountOpeningApplication=accountOpeningApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Account application record with the given id...!!!"));
		logger.info("account opening application retrieved feom db");
		
		
		if(accountOpeningApplication.getCustomerApprovalStatus()!=ApplicationStatus.PENDING) {
			return accountOpeningApplication;
		}
		logger.info("checking is customer approval of account opening application is pending");
		List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList = customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(id);
		
		if(customerAccountOpeningApplicationList.isEmpty()) {
			throw new ResourceNotFoundException("No customer account opening applications with the given account opening application id...!!!");
		}
		logger.info("Customer account opening applications retrieved for this application");
		
		
		int approvalCount=0;
		for(CustomerAccountOpeningApplication c: customerAccountOpeningApplicationList) {
			if(c.getCustomerApproval()==ApplicationStatus.REJECTED) {
				accountOpeningApplication.setCustomerApprovalStatus(ApplicationStatus.REJECTED);
				return accountOpeningApplicationRepository.save(accountOpeningApplication);
			}
			else if(c.getCustomerApproval()==ApplicationStatus.ACCEPTED) {
				approvalCount++;
			}
			
		}
		
		if(approvalCount== customerAccountOpeningApplicationList.size()) {
			accountOpeningApplication.setCustomerApprovalStatus(ApplicationStatus.ACCEPTED);
		}else {
			accountOpeningApplication.setCustomerApprovalStatus(ApplicationStatus.PENDING);
		}
		logger.info("Updated customer approval status for account opening application ");
		
		
		return accountOpeningApplicationRepository.save(accountOpeningApplication);
	}







	/**Step 1: approve
	 * Step 2: Create accounts
	 * @param id
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 */
	public List<CustomerAccount> approveApplication(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		logger.info("In approve account opening application");
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				logger.info("User active status approved");
				
		AccountOpeningApplication accountOpeningApplication= accountOpeningApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Account application record with the given id...!!!"));
		logger.info("account opening application fetched");
		
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
		logger.info("employee approval status accepted for account opening application");
		accountOpeningApplication=accountOpeningApplicationRepository.save(accountOpeningApplication);
		logger.info("account opening application saved");
		
		List<CustomerAccountOpeningApplication> customerAccountOpeningApplicationList = customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(id);
		logger.info("customer account opening application list fetched");
		if(customerAccountOpeningApplicationList.isEmpty()) {
			throw new ResourceNotFoundException("No customer account opening applications with the given account opening application id...!!!");
		}
		logger.info("Creating a new bank account from details in account oppening application");
		Account account = new Account();
		account.setAccountType(accountOpeningApplication.getAccountType());
		account.setBranch(accountOpeningApplication.getBranch());
		account.setAccountName(accountOpeningApplication.getAccountName());
		account = accountService.createAccount(account, principal);
		logger.info("Bank account created");
		List<CustomerAccount> customerAccountList=new ArrayList<>();
		for (CustomerAccountOpeningApplication c:customerAccountOpeningApplicationList) {
			customerAccountList.add(customerAccountService.createCustomerAccount(c.getId(),account, principal));
		}
		logger.info("Customer-accounts created ");
		logger.info("Returning customer account list");
		return customerAccountList;
	}
	
	
	
	
	
	
	
	
	/**
	 * @param id
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException
	 */
	public AccountOpeningApplication rejectApplication(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		logger.info("In reject account opening application");
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				logger.info("User active status verified");
				
		AccountOpeningApplication accountOpeningApplication= accountOpeningApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Account application record with the given id...!!!"));
		logger.info("Account opening application fetched");
		
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
		logger.info("employee approval status of account opening application is rejected");
		logger.info("saving account opening applications");
		return accountOpeningApplicationRepository.save(accountOpeningApplication);
	}







	






	
}
