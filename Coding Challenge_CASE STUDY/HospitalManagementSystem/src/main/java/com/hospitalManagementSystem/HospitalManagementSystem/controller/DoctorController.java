package com.hospitalManagementSystem.HospitalManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Doctor;
import com.hospitalManagementSystem.HospitalManagementSystem.service.DoctorService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
	@Autowired
	DoctorService doctorService;
	
	
	@PostMapping("/add")
	Doctor addDoctor(@RequestBody Doctor doctor) throws InvalidInputException {
		return doctorService.addDoctor(doctor);
	}
	
	

}
