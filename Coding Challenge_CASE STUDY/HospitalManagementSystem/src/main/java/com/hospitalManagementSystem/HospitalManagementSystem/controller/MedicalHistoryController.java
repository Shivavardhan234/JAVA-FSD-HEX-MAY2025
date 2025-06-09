package com.hospitalManagementSystem.HospitalManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospitalManagementSystem.HospitalManagementSystem.dto.MedicalHistoryDto2;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.ResourceNotFoundException;
import com.hospitalManagementSystem.HospitalManagementSystem.model.MedicalHistory;
import com.hospitalManagementSystem.HospitalManagementSystem.service.MedicalHistoryService;

@RestController
@RequestMapping("/api/medical-history")
public class MedicalHistoryController {
	
	
	@Autowired
	MedicalHistoryService medicalHistoryService;
	
	
	
	@PostMapping("/add/first-time")
	public MedicalHistoryDto2 addFirstTime(@RequestBody MedicalHistory medicalHistory) throws InvalidInputException{
		
		return medicalHistoryService.addFirstTime(medicalHistory);
	}
	
	
	//------------------------------------------ GET ----------------------------------------------------
	
	
	@GetMapping("/get/by-patient-id")
	 MedicalHistoryDto2 getMedicalHistoryByPatientId(int patientId) throws ResourceNotFoundException{
		
		 return medicalHistoryService.getMedicalHistoryByPatientId(patientId);
		 
	 }

}
