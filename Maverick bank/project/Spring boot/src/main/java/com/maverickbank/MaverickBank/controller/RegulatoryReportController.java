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
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.RegulatoryReport;
import com.maverickbank.MaverickBank.service.RegulatoryReportService;

@RestController
@RequestMapping("/api/regulatory-report")
public class RegulatoryReportController {
	
	@Autowired
	private RegulatoryReportService regulatoryReportService;
	
//---------------------------------------------- POST -------------------------------------------------------------------
	@PostMapping("/generate")
	@CrossOrigin(origins = "http://localhost:5173")
	public RegulatoryReport generateRegulatoryReport(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
	    return regulatoryReportService.generateRegulatoryReport(principal);
	}

	
	
	
	
//----------------------------------------- GET --------------------------------------------------------------------------
	
	@GetMapping("/get/all")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<RegulatoryReport> getAllReports(Principal principal) throws DeletedUserException, InvalidActionException, InvalidInputException {
	    return regulatoryReportService.getAllReports(principal);
	}
	@GetMapping("/get/by-id/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public RegulatoryReport getReportById(@PathVariable int id,Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {
	    return regulatoryReportService.getReportById(id, principal);
	}
	@GetMapping("/get/by-date-range/{startDate}/{endDate}")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<RegulatoryReport> getReportsByDateRange(@PathVariable LocalDate startDate,@PathVariable LocalDate endDate, Principal principal) throws DeletedUserException, InvalidActionException, InvalidInputException, ResourceNotFoundException {

	    return regulatoryReportService.getReportsByDateRange(startDate, endDate,principal);
	}

	
	
	
}
