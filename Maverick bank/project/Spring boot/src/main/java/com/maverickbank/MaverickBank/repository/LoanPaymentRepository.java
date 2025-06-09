package com.maverickbank.MaverickBank.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.LoanPayment;

public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Integer> {

	
	
	@Query("SELECT l FROM LoanPayment l WHERE l.loan.id=?1")
	List<LoanPayment> getByLoan(int loanId);

	
	@Query("SELECT sum(l.amountPaid) FROM LoanPayment l")
	BigDecimal getTotalLoanRepayment();

}
