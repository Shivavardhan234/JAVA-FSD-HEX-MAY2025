package com.hospitalManagementSystem.HospitalManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.ResourceNotFoundException;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Appointment;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Patient;
import com.hospitalManagementSystem.HospitalManagementSystem.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
	
	
	@Autowired
	private AppointmentService appointmentService;
	
	
	@PostMapping("/add/{patientId}/{doctorId}")
	public Appointment addAppointment(@PathVariable int patientId, @PathVariable int doctorId) throws ResourceNotFoundException, InvalidInputException {
		
		return  appointmentService.addAppointment(patientId, doctorId);
	}
	
	
	
	
	
	@GetMapping("/get/patients-by-doctorId/{doctorId}")
	public List<Patient> getAllPatientsByDoctorId(@PathVariable int doctorId) throws ResourceNotFoundException{
		
		return appointmentService.getAllPatientsByDoctorId(doctorId);
		}
	

}
