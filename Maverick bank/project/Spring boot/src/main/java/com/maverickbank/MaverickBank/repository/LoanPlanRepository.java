package com.maverickbank.MaverickBank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.LoanType;
import com.maverickbank.MaverickBank.model.LoanPlan;

public interface LoanPlanRepository extends JpaRepository<LoanPlan, Integer>{
	
	
	@Query("SELECT l FROM LoanPlan l WHERE l.loanName=?1")
	LoanPlan getLoanPlanByName(String loanName);
	
	@Query("SELECT l FROM LoanPlan l WHERE l.loanType=?1")
	List<LoanPlan> getByLoanType(LoanType loanType);

}
