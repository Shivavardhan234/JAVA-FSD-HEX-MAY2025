package com.maverickbank.MaverickBank.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;

public interface AccountOpeningApplicationRepository extends JpaRepository<AccountOpeningApplication, Integer> {

	
	@Query("SELECT a FROM AccountOpeningApplication a WHERE a.branch.id=?1")
	List<AccountOpeningApplication> getAccountOpeningApplicationByBranchId(int id);

	
	
	@Query("SELECT a FROM AccountOpeningApplication a WHERE a.accountType.id=?1")
	List<AccountOpeningApplication> getAccountOpeningApplicationByAccountTypeId(int accountTypeId);


	@Query("SELECT a FROM AccountOpeningApplication a WHERE a.customerApprovalStatus=?1")
	List<AccountOpeningApplication> getAccountOpeningApplicationByCustomerApprovalStatus(
			ApplicationStatus customerApprovalStatus);


	@Query("SELECT a FROM AccountOpeningApplication a WHERE a.employeeApprovalStatus=?1")
	List<AccountOpeningApplication> getAccountOpeningApplicationByEmployeeApprovalStatus(
			ApplicationStatus employeeApprovalStatus);


	@Query("SELECT a FROM AccountOpeningApplication a WHERE a.applicationDateTime BETWEEN ?1 AND ?2")
	List<AccountOpeningApplication> getAccountOpeningApplicationByDate(LocalDateTime start, LocalDateTime end);

	
	
	
	
}
