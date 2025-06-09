package com.maverickbank.MaverickBank.validation;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.LoanPayment;

public class LoanPaymentValidation {
	
	public  static void validateId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Loan payment history ID is Invalid. Please enter appropriate Loan payment history ID...!!!");
		}
		return;
	}


	public static  void validateLoan(Loan loan)throws InvalidInputException {
		if (loan == null) {
            throw new InvalidInputException("Null loan object provided. Please provide appropriate loan object...!!!");
        }
		return;
	}


	public static  void validateDueDate(LocalDate dueDate)throws InvalidInputException {
		if (dueDate == null) {
            throw new InvalidInputException("Null due date provided. Please provide appropriate due date...!!!");
        }
		return;
	}
	
	
	public static  void validateAmountPaid(BigDecimal amountPaid)throws InvalidInputException {
		if (amountPaid==null || amountPaid.compareTo(BigDecimal.ZERO)<=0) {
            throw new InvalidInputException("Invalid amount paid. Please provide appropriate amount paid...!!!");
        }
		return;
	}
	
	public  static void validateAmountToBePaid(BigDecimal amountToBePaid)throws InvalidInputException {
		if (amountToBePaid==null || amountToBePaid.compareTo(BigDecimal.ZERO)<=0) {
            throw new InvalidInputException("Invalid amount to be paid. Please provide appropriate amount to be paid paid...!!!");
        }
		return;
	}



	public  static void validatePaymentDate(LocalDate paymentDate) throws InvalidInputException{
		if (paymentDate == null) {
            throw new InvalidInputException("Null payment date provided. Please provide appropriate payment date...!!!");
        }
		return;
	}


	public static void validatePenalty(BigDecimal penalty) throws InvalidInputException{
		if(penalty==null || penalty.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidInputException("Invalid penalty provided. Please provide appropriate penalty...!!!");
		}
		return;
	}
	
	
	public static void validateLoanPaymentObject(LoanPayment loanPayment)throws InvalidInputException{
		if(loanPayment==null) {
			throw new InvalidInputException("Invalid Loan payment object provided. Please provide appropriate Loan payment...!!!");
		}
		return;
	}
	
	
	

}
