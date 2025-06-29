package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.FinancialPerformanceReport;
import com.maverickbank.MaverickBank.service.FinancialPerformanceReportService;

@RestController
@RequestMapping("/api/financial-performance-report")
public class FinancialPerformanceReportController {
	
	
	@Autowired
	private FinancialPerformanceReportService financialPerformanceReportService;
	
	
	
	
//-------------------------------------------------- POST --------------------------------------------------------------
	 @PostMapping("/generate")
	 @CrossOrigin(origins = "http://localhost:5173")
	    public FinancialPerformanceReport generateReport(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {

	        return financialPerformanceReportService.generateReport(principal);
	    }
	 
	 
//------------------------------------------------- GET ------------------------------------------------------------------

	 @GetMapping("/get/all")
	 @CrossOrigin(origins = "http://localhost:5173")
	    public List<FinancialPerformanceReport> getAllReports(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
																@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
	        return financialPerformanceReportService.getAllReports(page,size,principal);
	    }

	    @GetMapping("/get/by-id/{id}")
	    @CrossOrigin(origins = "http://localhost:5173")
	    public FinancialPerformanceReport getReportById(@PathVariable int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
	        return financialPerformanceReportService.getReportById(id, principal);
	    }

	    @GetMapping("/get/by-date-range/{startDate}/{endDate}")
	    @CrossOrigin(origins = "http://localhost:5173")
	    public List<FinancialPerformanceReport> getReportsByDateRange(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
																		@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,
																		@PathVariable LocalDate startDate,@PathVariable LocalDate endDate,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
	        return financialPerformanceReportService.getReportsByDateRange(page,size,startDate, endDate, principal);
	    }
	 
	 
	 
}
