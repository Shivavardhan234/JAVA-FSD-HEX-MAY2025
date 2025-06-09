package com.maverickbank.MaverickBank.validation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.maverickbank.MaverickBank.enums.PaymentMedium;
import com.maverickbank.MaverickBank.enums.TransactionType;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.Transaction;

public class TransactionValidation {

	
	public static void validateTransactionId(int transactionId) throws InvalidInputException {
	    if (transactionId <= 0) {
	        throw new InvalidInputException("Invalid transaction id. Transaction id should be greater than zero...!!!");
	    }
	    return;
	}

	public static void validateFromDetails(String fromDetails) throws InvalidInputException {
	    if (fromDetails == null || fromDetails.trim().isEmpty()) {
	        throw new InvalidInputException("Invalid FROM details. Please provide appropriate FROM details...!!!");
	    }
	    return;
	}
	
	
	
	public static void validateFromPaymentMedium(PaymentMedium fromPaymentMedium)throws InvalidInputException {
		if (fromPaymentMedium == null) {
	        throw new InvalidInputException("Invalid From payment medium. It should not be null...!!!");
	    }
		return;
	}

	public static void validateToDetails(String toDetails) throws InvalidInputException {
	    if (toDetails == null || toDetails.trim().isEmpty()) {
	        throw new InvalidInputException("Invalid TO details. Please provide appropriate TO details...!!!");
	    }
	    return;
	}
	
	public static void validateToPaymentMedium(PaymentMedium toPaymentMedium)throws InvalidInputException {
		if (toPaymentMedium == null) {
	        throw new InvalidInputException("Invalid TO payment medium. It should not be null...!!!");
	    }
		return;
	}

	public static void validateTransactionType(TransactionType transactionType) throws InvalidInputException {
	    if (transactionType == null) {
	        throw new InvalidInputException("Invalid Transaction type. It should not be null...!!!");
	    }
	    return;
	}

	public static void validateTransactionAmount(BigDecimal transactionAmount) throws InvalidInputException {
	    if (transactionAmount == null || transactionAmount.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new InvalidInputException("Invalid transaction amount. Please provide appropriate transaction amount...!!!");
	    }
	    return;
	}

	public static void validateTransactionDate(LocalDate transactionDate) throws InvalidInputException {
	    if (transactionDate == null) {
	        throw new InvalidInputException("Invalid Transaction date. They should not be null...!!!");
	    }
	    return;
	}
	
	





	public static void setTransactionTime(LocalTime transactionTime) throws InvalidInputException {
		if (transactionTime == null) {
	        throw new InvalidInputException("Invalid Transaction time. They should not be null...!!!");
	    }
		return;
	}
	
	public static void validateTransactionObject(Transaction transaction) throws InvalidInputException {
	    if (transaction == null) {
	        throw new InvalidInputException("Invalid Transaction. They should not be null...!!!");
	    }
	    return;
	}
	
	public static void validateTransaction(Transaction transaction) throws InvalidInputException {
	    validateTransactionObject(transaction);
	    validateFromDetails(transaction.getFromDetails());
	    validateToDetails(transaction.getToDetails());
	    validateFromPaymentMedium(transaction.getFromPaymentMedium());
	    validateToPaymentMedium(transaction.getToPaymentMedium());
	    validateTransactionAmount(transaction.getTransactionAmount());
	    return;
	}
	




	
	
	

	

}
