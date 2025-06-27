package com.maverickbank.MaverickBank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "financial_performance_report")
public class FinancialPerformanceReport {


	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	    @Column(name="report_date",nullable=false)
	    private LocalDate reportDate;
	    @Column(name="total_deposits",nullable=false)
	    private BigDecimal totalDeposits; 
	    @Column(name="total_number_of_loans_issued",nullable=false)
	    private int totalNumberOfLoansIssued;    
	    @Column(name="total_number_of_active_loans",nullable=false)
	    private int totalNumberOfActiveLoans;    
	    @Column(name="total_loan_amount issued",nullable=false)
	    private BigDecimal totalLoanAmountIssued;      
	    @Column(name="total_loan_repayment",nullable=false)
	    private BigDecimal totalLoanRepayment;    
	    @Column(name="total_transactions",nullable=false)
	    private int totalTransactions;   
	    @Column(name="total_number_of_transactions",nullable=false)
	    private BigDecimal totaltransactionAmount;
	    
	    
//------------------------------------------- CONSTRUCTORS --------------------------------------------------------------    
	    
	    
	    
		public FinancialPerformanceReport() {
			super();
		}
		public FinancialPerformanceReport(int id, LocalDate reportDate, BigDecimal totalDeposits,
				int totalNumberOfLoansIssued, int totalNumberOfActiveLoans, BigDecimal totalLoanAmountIssued,
				BigDecimal totalLoanRepayment,  int totalTransactions,
				BigDecimal totaltransactionAmount) {
			super();
			this.id = id;
			this.reportDate = reportDate;
			this.totalDeposits = totalDeposits;
			this.totalNumberOfLoansIssued = totalNumberOfLoansIssued;
			this.totalNumberOfActiveLoans = totalNumberOfActiveLoans;
			this.totalLoanAmountIssued = totalLoanAmountIssued;
			this.totalLoanRepayment = totalLoanRepayment;
			this.totalTransactions = totalTransactions;
			this.totaltransactionAmount = totaltransactionAmount;
		}
		
		
		
		
		
//------------------------------------------------ GETTERS & SETTERS --------------------------------------------------
		public int getId() {
			return id;
		}
		public LocalDate getReportDate() {
			return reportDate;
		}
		public BigDecimal getTotalDeposits() {
			return totalDeposits;
		}
		public int getTotalNumberOfLoansIssued() {
			return totalNumberOfLoansIssued;
		}
		public int getTotalNumberOfActiveLoans() {
			return totalNumberOfActiveLoans;
		}
		public BigDecimal getTotalLoanAmountIssued() {
			return totalLoanAmountIssued;
		}
		public BigDecimal getTotalLoanRepayment() {
			return totalLoanRepayment;
		}
		
		public int getTotalTransactions() {
			return totalTransactions;
		}
		public BigDecimal getTotaltransactionAmount() {
			return totaltransactionAmount;
		}
		
		
		
		
		public void setId(int id) throws InvalidInputException {
			if(id<=0) {
				throw new InvalidInputException("Invalid financial perforcement record id provided. please enter appropriate id...!!! ");
			}
			this.id = id;
		}
		public void setReportDate(LocalDate reportDate) throws InvalidInputException {
	        if (reportDate == null || reportDate.isAfter(LocalDate.now())) {
	            throw new InvalidInputException("Invalid report date provided...!!!");
	        }
	        
	        this.reportDate = reportDate;
	    }

	    public void setTotalDeposits(BigDecimal totalDeposits) throws InvalidInputException {
	        if (totalDeposits == null|| (totalDeposits.compareTo(BigDecimal.ZERO) < 0)) {
	            throw new InvalidInputException("Invalid total deposits provided. please provide appropriate total deposits...!!!");
	        }
	        
	        this.totalDeposits = totalDeposits;
	    }

	    public void setTotalNumberOfLoansIssued(int totalNumberOfLoansIssued) throws InvalidInputException {
	        if (totalNumberOfLoansIssued < 0) {
	            throw new InvalidInputException("Total number of loans issued cannot be negative...!!!");
	        }
	        this.totalNumberOfLoansIssued = totalNumberOfLoansIssued;
	    }

	    public void setTotalNumberOfActiveLoans(int totalNumberOfActiveLoans) throws InvalidInputException {
	        if (totalNumberOfActiveLoans < 0) {
	            throw new InvalidInputException("Total number of active loans cannot be negative...!!!");
	        }
	        this.totalNumberOfActiveLoans = totalNumberOfActiveLoans;
	    }

	    public void setTotalLoanAmountIssued(BigDecimal totalLoanAmountIssued) throws InvalidInputException {
	        if (totalLoanAmountIssued == null || (totalLoanAmountIssued.compareTo(BigDecimal.ZERO) < 0)) {
	            throw new InvalidInputException("Invalid total loan amount. please provide appropriate loan amount...!!!");
	        }
	        
	        this.totalLoanAmountIssued = totalLoanAmountIssued;
	    }

	    public void setTotalLoanRepayment(BigDecimal totalLoanRepayment) throws InvalidInputException {
	        if (totalLoanRepayment == null||(totalLoanRepayment.compareTo(BigDecimal.ZERO) < 0)) {
	        	 throw new InvalidInputException("Invalid total loan repayment. please provide appropriate loan repayment...!!!");
	  	         }
	        
	        this.totalLoanRepayment = totalLoanRepayment;
	    }

	   
	    
	    

	    public void setTotalTransactions(int totalTransactions) throws InvalidInputException {
	        if (totalTransactions < 0) {
	        	throw new InvalidInputException("Invalid total transactions. Please provide appropriate total transactions...!!!");
	     	          }
	        
	        this.totalTransactions = totalTransactions;
	    }

	    public void setTotaltransactionAmount(BigDecimal totaltransactionAmount) throws InvalidInputException {
	        if (totaltransactionAmount == null || (totaltransactionAmount.compareTo(BigDecimal.ZERO) < 0)) {
	            throw new InvalidInputException("Invalid total transaction amount provided. Please provide appropriate total transaction amount...!!!");
	          }
	       
	        this.totaltransactionAmount = totaltransactionAmount;
	    }
		
		
		
		
	    
	    
	    
	    


}
