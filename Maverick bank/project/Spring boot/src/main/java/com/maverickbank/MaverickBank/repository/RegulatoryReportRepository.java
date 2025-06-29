package com.maverickbank.MaverickBank.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.RegulatoryReport;

public interface RegulatoryReportRepository  extends JpaRepository<RegulatoryReport, Integer>{

	
	
	@Query("SELECT r FROM RegulatoryReport r WHERE r.reportDate BETWEEN ?1 AND ?2")
	List<RegulatoryReport> getByReportDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
	

}
