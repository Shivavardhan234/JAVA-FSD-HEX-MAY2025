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
@Table(name="loan_payment")
public class LoanPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(nullable=false)
	private Loan loan;
	@Column(name="due_date",nullable=false)
	private LocalDate dueDate;
	@Column(name="amount_to_be_paid",nullable=false)
	private BigDecimal amountToBePaid;
	@Column(name="amount_paid",nullable=false)
	private BigDecimal amountPaid;
	@Column(name="payment_date",nullable=false)
	private LocalDate paymentDate;
	@Column(name="penalty",nullable=false)
	private BigDecimal penalty;
	
	
//----------------------------------- Constructors -----------------------------------------------------------------
	public LoanPayment() {}



public LoanPayment(int id, Loan loan, LocalDate dueDate, BigDecimal amountToBePaid, BigDecimal amountPaid,
			LocalDate paymentDate, BigDecimal penalty) {
		super();
		this.id = id;
		this.loan = loan;
		this.dueDate = dueDate;
		this.amountToBePaid = amountToBePaid;
		this.amountPaid = amountPaid;
		this.paymentDate = paymentDate;
		this.penalty = penalty;
	}


	// ------------------------------------- Getters & Setters ---------------------------------------------------------
	public int getId() {return id;}
	public Loan getLoan() {return loan;}
	public LocalDate getDueDate() {return dueDate;}
	public BigDecimal getAmountPaid() {return amountPaid;}
	public BigDecimal getAmountToBePaid() {return amountToBePaid;}
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
	
	
	public void setAmountPaid(BigDecimal amountPaid)throws InvalidInputException {
		if (amountPaid==null || amountPaid.compareTo(BigDecimal.ZERO)<=0) {
            throw new InvalidInputException("Invalid amount paid. Please provide appropriate amount paid...!!!");
        }
		this.amountPaid=amountPaid;
	}
	
	public void setAmountToBePaid(BigDecimal amountToBePaid)throws InvalidInputException {
		if (amountToBePaid==null || amountToBePaid.compareTo(BigDecimal.ZERO)<=0) {
            throw new InvalidInputException("Invalid amount to be paid. Please provide appropriate amount to be paid paid...!!!");
        }
		this.amountToBePaid=amountToBePaid;
	}



	public void setPaymentDate(LocalDate paymentDate) throws InvalidInputException{
		if (paymentDate == null) {
            throw new InvalidInputException("Null payment date provided. Please provide appropriate payment date...!!!");
        }
		this.paymentDate = paymentDate;
	}


	public void setPenalty(BigDecimal penalty) throws InvalidInputException{
		if(penalty==null ) {
			throw new InvalidInputException("Invalid penalty provided. Please provide appropriate penalty...!!!");
		}
		this.penalty = penalty;
	}
	
	
	
}
