package com.maverickbank.MaverickBank.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.LoanStatus;
import com.maverickbank.MaverickBank.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
	
	
	@Query("SELECT l FROM Loan l WHERE l.status = ?1")
	List<Loan> getByStatus(LoanStatus status, Pageable pageable);
	
	@Query("SELECT l FROM Loan l WHERE l.account.id = ?1")
	List<Loan> getByAccountId(int accountId);

	@Query("SELECT l FROM Loan l WHERE l.account.id = ?1 AND  l.status = ?2")
	List<Loan> getByAccountIdAndStatus(int accountId,LoanStatus status);

	
	@Query("SELECT count(l) FROM Loan l WHERE l.status = ?1 ")
	int countByStatus(LoanStatus status);

	
	@Query("SELECT count(l) FROM Loan l WHERE l.status=?1")
	int getTotalActiveLoans(LoanStatus status);

	
	@Query("SELECT sum(l.loanPlan.principalAmount) FROM Loan l ")
	BigDecimal getTotalLoanPrincipalIssued();

	
	
	

	

	


}
