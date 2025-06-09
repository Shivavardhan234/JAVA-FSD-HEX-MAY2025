package com.hospitalManagementSystem.HospitalManagementSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospitalManagementSystem.HospitalManagementSystem.dto.PatientDto;
import com.hospitalManagementSystem.HospitalManagementSystem.enums.AppointmentStatus;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.ResourceNotFoundException;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Appointment;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Doctor;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Patient;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.AppointmentRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.DoctorRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.PatientRepository;

@Service
public class AppointmentService {

	
	private AppointmentRepository appointmentRepository;
	private PatientRepository patientRepository;
	private DoctorRepository doctorRepository;
	private PatientDto patientDto;
	
	
	public List<Patient> getAllPatientsByDoctorId(int doctorId) throws ResourceNotFoundException{
		List<Patient> patientList = appointmentRepository.getAllPatientsByDoctorId(doctorId);
		if(patientList==null||patientList.isEmpty()) {
			throw new ResourceNotFoundException("No patient records with the given doctor id...!!!");
		}
		
		
		return patientList;
	}




	public Appointment addAppointment(int patientId, int doctorId) throws ResourceNotFoundException, InvalidInputException {
		Patient patient = patientRepository.findById(patientId).orElseThrow(()->new ResourceNotFoundException("No patient records with the given patient id...!!!"));
		
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(()->new ResourceNotFoundException("No doctor records with the given doctor id...!!!"));
		
		
		Appointment newAppointment=new Appointment();
		newAppointment.setDoctor(doctor);
		newAppointment.setPatient(patient);
		newAppointment.setAppointmentStatus(AppointmentStatus.PENDING);
		return appointmentRepository.save(newAppointment);
	}
	
	
	
	
	
}
