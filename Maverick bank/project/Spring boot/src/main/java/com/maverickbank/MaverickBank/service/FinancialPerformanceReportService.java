package com.maverickbank.MaverickBank.service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.LoanStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.FinancialPerformanceReport;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.FinancialPerformanceReportRepository;
import com.maverickbank.MaverickBank.repository.LoanPaymentRepository;
import com.maverickbank.MaverickBank.repository.LoanRepository;
import com.maverickbank.MaverickBank.repository.TransactionRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class FinancialPerformanceReportService {
	
	
	   
	private  UserRepository userRepository;
	private AccountRepository accountRepository;
	    

	private LoanRepository loanRepository;
	private TransactionRepository transactionRepository;
	private FinancialPerformanceReportRepository financialPerformanceReportRepository;
	private LoanPaymentRepository loanPaymentRepository;
	    
	    
	    
	    
	    public FinancialPerformanceReportService(UserRepository userRepository, AccountRepository accountRepository,
				LoanRepository loanRepository, TransactionRepository transactionRepository,
				FinancialPerformanceReportRepository financialPerformanceReportRepository,
				LoanPaymentRepository loanPaymentRepository) {
			super();
			this.userRepository = userRepository;
			this.accountRepository = accountRepository;
			this.loanRepository = loanRepository;
			this.transactionRepository = transactionRepository;
			this.financialPerformanceReportRepository = financialPerformanceReportRepository;
			this.loanPaymentRepository = loanPaymentRepository;
		}

	    
//----------------------------------------------- POST ------------------------------------------------------------------

	    public FinancialPerformanceReport generateReport(Principal principal)
	            throws InvalidInputException, DeletedUserException, InvalidActionException {

	        
	        User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());

	        
	        BigDecimal totalDeposits = accountRepository.getTotalBankBalance();
	        BigDecimal totalLoanAmountIssued = loanRepository.getTotalLoanPrincipalIssued();
	        BigDecimal totalLoanRepayment = loanPaymentRepository.getTotalLoanRepayment();
	        BigDecimal totalTransactionAmount = transactionRepository.getTotalTransactionAmount();
	        int totalTransactions = transactionRepository.getTotalNumberOfTransactions();

	        int totalNumberOfLoansIssued = (int)loanRepository.count();
	        int totalNumberOfActiveLoans = loanRepository.getTotalActiveLoans(LoanStatus.ACTIVE);

	        
	        FinancialPerformanceReport report = new FinancialPerformanceReport();
	        report.setReportDate(LocalDate.now());
	        report.setTotalDeposits(totalDeposits);
	        report.setTotalLoanAmountIssued(totalLoanAmountIssued);
	        report.setTotalLoanRepayment(totalLoanRepayment);
	        report.setTotalTransactions(totalTransactions);
	        report.setTotaltransactionAmount(totalTransactionAmount);
	        report.setTotalNumberOfLoansIssued(totalNumberOfLoansIssued);
	        report.setTotalNumberOfActiveLoans(totalNumberOfActiveLoans);

	        
	        return financialPerformanceReportRepository.save(report);
	    }
	
	    
//------------------------------------------- GET ------------------------------------------------------------------------
	    public List<FinancialPerformanceReport> getAllReports(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
	    	 User currentUser = userRepository.getByUsername(principal.getName());
		        UserValidation.checkActiveStatus(currentUser.getStatus());
	        return financialPerformanceReportRepository.findAll();
	    }

	    public FinancialPerformanceReport getReportById(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
	    	 User currentUser = userRepository.getByUsername(principal.getName());
		        UserValidation.checkActiveStatus(currentUser.getStatus());
	        return financialPerformanceReportRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Report not found with id...!!!"));
	    }

	    
	    public List<FinancialPerformanceReport> getReportsByDateRange(LocalDate startDate, LocalDate endDate, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
	    	User currentUser = userRepository.getByUsername(principal.getName());
	        UserValidation.checkActiveStatus(currentUser.getStatus());
	        
	        List<FinancialPerformanceReport> financialPerformanceReport = financialPerformanceReportRepository.getReportsByDates(startDate, endDate);
	        if (financialPerformanceReport == null || financialPerformanceReport.isEmpty()) {
	            throw new ResourceNotFoundException("No Financial Performance Reports found between given dates...!!!");
	        }
	        return financialPerformanceReport;
	    }

	    


}
