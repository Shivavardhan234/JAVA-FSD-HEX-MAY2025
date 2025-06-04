package com.maverickbank.MaverickBank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="loan_payment_history")
public class LoanPaymentHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	@JoinColumn(nullable=false)
	private Loan loan;
	@Column(name="due_date",nullable=false)
	private LocalDate dueDate;
	@Column(name="payment_date",nullable=false)
	private LocalDate paymentDate;
	@Column(name="penalty",nullable=false)
	private BigDecimal penalty;
	
	
//----------------------------------- Constructors -----------------------------------------------------------------
	public LoanPaymentHistory() {}


	public LoanPaymentHistory(int id, Loan loan, LocalDate dueDate, LocalDate paymentDate, BigDecimal penalty) {
		this.id = id;
		this.loan = loan;
		this.dueDate = dueDate;
		this.paymentDate = paymentDate;
		this.penalty = penalty;
	}

// ------------------------------------- Getters & Setters ---------------------------------------------------------
	public int getId() {return id;}
	public Loan getLoan() {return loan;}
	public LocalDate getDueDate() {return dueDate;}
	public LocalDate getPaymentDate() {return paymentDate;}
	public BigDecimal getPenalty() {return penalty;}


	public void setId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Loan payment history ID is Invalid. Please enter appropriate Loan payment history ID...!!!");
		}
		this.id = id;
	}


	public void setLoan(Loan loan)throws InvalidInputException {
		if (loan == null) {
            throw new InvalidInputException("Null loan object provided. Please provide appropriate loan object...!!!");
        }
		this.loan = loan;
	}


	public void setDueDate(LocalDate dueDate)throws InvalidInputException {
		if (dueDate == null) {
            throw new InvalidInputException("Null due date provided. Please provide appropriate due date...!!!");
        }
		this.dueDate = dueDate;
	}


	public void setPaymentDate(LocalDate paymentDate) throws InvalidInputException{
		if (paymentDate == null) {
            throw new InvalidInputException("Null payment date provided. Please provide appropriate payment date...!!!");
        }
		this.paymentDate = paymentDate;
	}


	public void setPenalty(BigDecimal penalty) throws InvalidInputException{
		if(penalty==null || penalty.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidInputException("Invalid penalty provided. Please provide appropriate penalty...!!!");
		}
		this.penalty = penalty;
	}
	
	
	
}
