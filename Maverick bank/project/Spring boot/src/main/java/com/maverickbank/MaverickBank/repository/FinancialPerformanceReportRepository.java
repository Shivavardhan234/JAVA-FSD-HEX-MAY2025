package com.maverickbank.MaverickBank.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.FinancialPerformanceReport;

public interface FinancialPerformanceReportRepository extends JpaRepository<FinancialPerformanceReport, Integer> {

	
	
	@Query("SELECT r FROM FinancialPerformanceReport r WHERE r.reportDate BETWEEN ?1 AND ?2")
	List<FinancialPerformanceReport> getReportsByDates(LocalDate startDate, LocalDate endDate, Pageable pageable);

}
