package com.maverickbank.MaverickBank.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.model.LoanClosureRequest;

public interface LoanClosureRequestRepository extends JpaRepository<LoanClosureRequest, Integer> {
	
	
	@Query("SELECT l FROM LoanClosureRequest l WHERE l.loan.id=?1 AND l.requestStatus=?2")
	List<LoanClosureRequest> getByLoanIdAndRequestStatus(int loanId, ApplicationStatus status);

	
	@Query("SELECT l FROM LoanClosureRequest l WHERE l.loan.id=?1")
	List<LoanClosureRequest> findByLoanId(int loanId);

	@Query("SELECT l FROM LoanClosureRequest l WHERE  l.requestStatus=?1")
	List<LoanClosureRequest> getByRequestStatus(ApplicationStatus status, Pageable pageable);

}
