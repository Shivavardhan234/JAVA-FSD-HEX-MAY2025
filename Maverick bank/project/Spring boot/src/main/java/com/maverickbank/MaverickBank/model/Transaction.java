package com.maverickbank.MaverickBank.model;


	import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.maverickbank.MaverickBank.enums.PaymentMedium;
import com.maverickbank.MaverickBank.enums.TransactionType;
import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

	

	/**
	 * 
	 */
@Entity
	public class Transaction {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name="id")
		private int transactionId;
		@Column(name="from_details",nullable=false)
		private String fromDetails;
		@Enumerated(EnumType.STRING)
		@Column(name="from_payment_medium",nullable=false)
		private PaymentMedium fromPaymentMedium;
		
		@Column(name="to_details",nullable=false)
		private String toDetails;
		@Enumerated(EnumType.STRING)
		@Column(name="to_payment_medium",nullable=false)
		private PaymentMedium toPaymentMedium;
		@Enumerated(EnumType.STRING)
		@Column(name="transaction_type")
		private TransactionType transactionType;
		@Column(name="transaction_amount",nullable=false)
		private BigDecimal transactionAmount;
		@Column(name="transaction_date",nullable=false)
		private LocalDate transactionDate;
		@Column(name="transaction_time",nullable=false)
		private LocalTime transactionTime;
		@Column(name="description")
		private String transactionDescription;
		
		
	//-------------------------------constructor-------------------------------------------------
		public Transaction() {}


		
		public Transaction(int transactionId, String fromDetails,PaymentMedium fromPaymentMedium, String toDetails,PaymentMedium toPaymentMedium, TransactionType transactionType,
			BigDecimal transactionAmount, LocalDate transactionDate,LocalTime transactionTime, String transactionDescription) throws InvalidInputException {
			this.setTransactionId(transactionId);
			this.setFromDetails(fromDetails);
			this.setToDetails(toDetails);
			this.setTransactionType(transactionType);
			this.setTransactionAmount(transactionAmount);
			this.setTransactionDate(transactionDate);
			this.setTransactionTime(transactionTime);
			this.setTransactionDescription(transactionDescription); 
		}







		//------------------------------ Getters -------------------------------------------------------
		
		


		


		public LocalTime getTransactionTime() {return transactionTime;}
		public int getTransactionId() {return transactionId;}
		public String getFromDetails() {return fromDetails;}
		public PaymentMedium getFromPaymentMedium() { return fromPaymentMedium;}
		public String getToDetails() {return toDetails;}
		public PaymentMedium getToPaymentMedium() { return toPaymentMedium;}
		public TransactionType getTransactionType() {return transactionType;}
		public BigDecimal getTransactionAmount() {return transactionAmount;}
		public LocalDate getTransactionDate() {return transactionDate;}
		public String getTransactionDescription() {return transactionDescription;}


		
		
		//-------------------- Setters-------------------------------------------------------------
		public void setTransactionId(int transactionId) throws InvalidInputException {
		    if (transactionId <= 0) {
		        throw new InvalidInputException("Invalid transaction id. Transaction id should be greater than zero...!!!");
		    }
		    this.transactionId = transactionId;
		}

		public void setFromDetails(String fromDetails) throws InvalidInputException {
		    if (fromDetails == null || fromDetails.trim().isEmpty()) {
		        throw new InvalidInputException("Invalid FROM details. Please provide appropriate FROM details...!!!");
		    }
		    this.fromDetails = fromDetails.trim();
		}
		
		
		
		public void setFromPaymentMedium(PaymentMedium fromPaymentMedium)throws InvalidInputException {
			if (fromPaymentMedium == null) {
		        throw new InvalidInputException("Invalid From payment medium. It should not be null...!!!");
		    }
			this.fromPaymentMedium=fromPaymentMedium;
		}

		public void setToDetails(String toDetails) throws InvalidInputException {
		    if (toDetails == null || toDetails.trim().isEmpty()) {
		        throw new InvalidInputException("Invalid TO details. Please provide appropriate TO details...!!!");
		    }
		    this.toDetails = toDetails.trim();
		}
		
		public void setToPaymentMedium(PaymentMedium toPaymentMedium)throws InvalidInputException {
			if (toPaymentMedium == null) {
		        throw new InvalidInputException("Invalid TO payment medium. It should not be null...!!!");
		    }
			this.toPaymentMedium=toPaymentMedium;
		}

		public void setTransactionType(TransactionType transactionType) throws InvalidInputException {
		    if (transactionType == null) {
		        throw new InvalidInputException("Invalid Transaction type. It should not be null...!!!");
		    }
		    this.transactionType = transactionType;
		}

		public void setTransactionAmount(BigDecimal transactionAmount) throws InvalidInputException {
		    if (transactionAmount == null || transactionAmount.compareTo(BigDecimal.ZERO) <= 0) {
		        throw new InvalidInputException("Invalid transaction amount. Please provide appropriate transaction amount...!!!");
		    }
		    this.transactionAmount = transactionAmount;
		}

		public void setTransactionDate(LocalDate transactionDate) throws InvalidInputException {
		    if (transactionDate == null) {
		        throw new InvalidInputException("Invalid Transaction date. They should not be null...!!!");
		    }
		    this.transactionDate = transactionDate;
		}





		public void setTransactionTime(LocalTime transactionTime) throws InvalidInputException {
			if (transactionTime == null) {
		        throw new InvalidInputException("Invalid Transaction time. They should not be null...!!!");
		    }
			this.transactionTime = transactionTime;
		}



		
		
		
		/**This is optional, user may give or may not give.
		 *  Hence i didn't do any validation. Remember it!!!!!!!!!
		 * @param transactionDescription
		 */
		public void setTransactionDescription(String transactionDescription) {
		    
		    this.transactionDescription = transactionDescription.trim();
		}



		

		

}
