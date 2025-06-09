package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.LoanStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.RegulatoryReport;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.CustomerRepository;
import com.maverickbank.MaverickBank.repository.LoanRepository;
import com.maverickbank.MaverickBank.repository.RegulatoryReportRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class RegulatoryReportService {
	
	
	private RegulatoryReportRepository regulatoryReportRepository;
	private UserRepository userRepository;
	private CustomerRepository customerRepository;
	
	private AccountRepository accountRepository;
	private LoanRepository loanRepository;
	
	public RegulatoryReportService(RegulatoryReportRepository regulatoryReportRepository, UserRepository userRepository,
			CustomerRepository customerRepository, AccountRepository accountRepository, LoanRepository loanRepository) {
		super();
		this.regulatoryReportRepository = regulatoryReportRepository;
		this.userRepository = userRepository;
		this.customerRepository = customerRepository;
		this.accountRepository = accountRepository;
		this.loanRepository = loanRepository;
	}






//---------------------------------------------- POST --------------------------------------------------------------------
	
	
	
	
	
	public RegulatoryReport generateRegulatoryReport(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());


	    LocalDate reportDate = LocalDate.now();

	    int totalCustomers = (int) customerRepository.count();
	    int totalAccounts = (int) accountRepository.count();
	    int totalActiveLoans = loanRepository.countByStatus(LoanStatus.ACTIVE);
	    

	    int kycCompliantAccounts = accountRepository.countByKycCompleted(true);
	    
	    
	    
	    RegulatoryReport report = new RegulatoryReport();
	    report.setReportDate(reportDate);
	    report.setTotalCustomers(totalCustomers);
	    report.setTotalAccounts(totalAccounts);
	    report.setKycCompliantAccounts(kycCompliantAccounts);
	    report.setTotalActiveLoans(totalActiveLoans);
	    

	    
	    return regulatoryReportRepository.save(report);
	}

	
	
	
	//---------------------------------------------- GET -----------------------------------------------------------------
	public List<RegulatoryReport> getAllReports(Principal principal) throws DeletedUserException, InvalidActionException, InvalidInputException {
		 User currentUser = userRepository.getByUsername(principal.getName());
		    UserValidation.checkActiveStatus(currentUser.getStatus());
        return regulatoryReportRepository.findAll();
    }

    
    public RegulatoryReport getReportById(int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {
    	 User currentUser = userRepository.getByUsername(principal.getName());
 	    UserValidation.checkActiveStatus(currentUser.getStatus());
        return regulatoryReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Regulatory report not found with id: " + id));
    }

    
    public List<RegulatoryReport> getReportsByDateRange(LocalDate startDate, LocalDate endDate, Principal principal) throws DeletedUserException, InvalidActionException, InvalidInputException, ResourceNotFoundException {
    	 User currentUser = userRepository.getByUsername(principal.getName());
 	    UserValidation.checkActiveStatus(currentUser.getStatus());
 	   List<RegulatoryReport> regulatoryReportList=regulatoryReportRepository.getByReportDateRange(startDate, endDate);
 	   if(regulatoryReportList==null||regulatoryReportList.isEmpty()) {
 		   throw new ResourceNotFoundException("No Regulatory report found in the given date range...!!!");
 	   }
        return regulatoryReportList;
    }

}
