package com.maverickbank.MaverickBank.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.model.LoanOpeningApplication;

public interface LoanOpeningApplicationRepository extends JpaRepository<LoanOpeningApplication, Integer> {

	
	
	
	
	@Query("SELECT l FROM LoanOpeningApplication l WHERE l.account.id = ?1 AND l.status = ?2")
	List<LoanOpeningApplication> getByAccountIdAndStatus(int accountId,ApplicationStatus status, Pageable pageable);

	
	
	@Query("SELECT l FROM LoanOpeningApplication l WHERE l.account.id=?1 AND l.loanPlan.id=?2 AND l.status=?3")
	List<LoanOpeningApplication> getOldLoanOpeningApplications(int accountId,int loanPlanId, ApplicationStatus status );


	@Query("SELECT l FROM LoanOpeningApplication l WHERE l.account.id=?1")
	List<LoanOpeningApplication> getByAccountId(int accountId, Pageable pageable);


	@Query("SELECT l FROM LoanOpeningApplication l WHERE l.loanPlan.id=?1")
	List<LoanOpeningApplication> getByLoanPlanId(int loanPlanId);


	@Query("SELECT l FROM LoanOpeningApplication l WHERE  l.status = ?1")
	List<LoanOpeningApplication> getByStatus(ApplicationStatus status, Pageable pageable);
	
	
}
