package com.maverickbank.MaverickBank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.maverickbank.MaverickBank.enums.LoanStatus;
import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
public class Loan {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	@JoinColumn(nullable=false)
	private Account account;
	@ManyToOne
	@JoinColumn(name = "loan_plan_id", nullable = false)
	private LoanPlan loanPlan;
	@Column(name="loan_start_date",nullable=false)
	private LocalDate loanStartDate;
	@Column(name="loan_end_date",nullable=false)
	private LocalDate loanEndDate;
	@Column(name="status",nullable=false)
	private LoanStatus status;
	@Column(name="disbursement_date",nullable=false)
	private LocalDate disbursementDate;
	@Column(name="total_penalty",nullable=false)
	private BigDecimal totalPenalty;
	@Column(name="due_date")
	private LocalDate dueDate;
	
	
// ----------------------------------------------- Constructors -------------------------------------------------	
	public Loan() {}



	public Loan(int id, Account account, LoanPlan loanPlan, LocalDate loanStartDate, LocalDate loanEndDate,
			LoanStatus status, LocalDate disbursementDate, BigDecimal totalPenalty, LocalDate dueDate) throws InvalidInputException {
		this.setId(id);
		this.setAccount(account);
		this.setLoanPlan(loanPlan);
		this.setLoanStartDate(loanStartDate);
		this.setLoanEndDate(loanEndDate);
		this.setStatus(status);
		this.setDisbursementDate(disbursementDate);
		this.setTotalPenalty(totalPenalty);
		this.setDueDate(dueDate);
	}

// -------------------------------------- Getters & Setters -----------------------------------------------------
	

	public int getId() {return id;}
	public Account getAccount() {return account;}
	public LoanPlan getLoanPlan() {return loanPlan;}
	public LocalDate getLoanStartDate() {return loanStartDate;}
	public LocalDate getLoanEndDate() {return loanEndDate;}
	public LoanStatus getStatus() {return status;}
	public LocalDate getDisbursementDate() {return disbursementDate;}
	public BigDecimal getTotalPenalty() {return totalPenalty;}
	public LocalDate getDueDate() {return dueDate;}



	public void setId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Loan ID is Invalid. Please enter appropriate Loan ID...!!!");
		}
		this.id = id;
	}



	public void setAccount(Account account) throws InvalidInputException{
		if (account == null) {
            throw new InvalidInputException("Null account object provided. Please provide appropriate account object...!!!");
        }
		this.account = account;
	}



	public void setLoanPlan(LoanPlan loanPlan) throws InvalidInputException{
		if (loanPlan == null) {
            throw new InvalidInputException("Null loan plan object provided. Please provide appropriate loan plan object...!!!");
        }
		this.loanPlan = loanPlan;
	}



	public void setLoanStartDate(LocalDate loanStartDate) throws InvalidInputException{
		if (loanStartDate == null) {
            throw new InvalidInputException("Null loan start date provided. Please provide appropriate loan start date...!!!");
        }
		this.loanStartDate = loanStartDate;
	}



	public void setLoanEndDate(LocalDate loanEndDate) throws InvalidInputException{
		if (loanEndDate == null) {
            throw new InvalidInputException("Null loan end date provided. Please provide appropriate loan end date...!!!");
        }
		this.loanEndDate = loanEndDate;
	}



	public void setStatus(LoanStatus status) throws InvalidInputException{
		if (status == null) {
            throw new InvalidInputException("Null loan status provided. Please provide appropriate loan status...!!!");
        }
		this.status = status;
	}



	public void setDisbursementDate(LocalDate disbursementDate) throws InvalidInputException{
		if (disbursementDate == null) {
            throw new InvalidInputException("Null disbursement date provided. Please provide appropriate disburse date...!!!");
        }
		this.disbursementDate = disbursementDate;
	}



	public void setTotalPenalty(BigDecimal totalPenalty) throws InvalidInputException{
		if(totalPenalty==null || totalPenalty.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidInputException("Invalid total penalty provided. Please provide appropriate total penalty...!!!");
		}
		this.totalPenalty = totalPenalty;
	}



	public void setDueDate(LocalDate dueDate) throws InvalidInputException{
		if (dueDate == null) {
            throw new InvalidInputException("Null due date provided. Please provide appropriate due date...!!!");
        }
		this.dueDate = dueDate;
	}
	
	

	
}
