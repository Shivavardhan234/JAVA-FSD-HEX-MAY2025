package com.hospitalManagementSystem.HospitalManagementSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
	
	
	
	
	public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
			DoctorRepository doctorRepository) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
	}




	public List<Patient> getAllPatientsByDoctorId(int doctorId) throws ResourceNotFoundException{
		//get the list
		List<Patient> patientList = appointmentRepository.getAllPatientsByDoctorId(doctorId);
		//validate the list
		if(patientList==null||patientList.isEmpty()) {
			throw new ResourceNotFoundException("No patient records with the given doctor id...!!!");
		}
		
		
		return patientList;
	}




	public Appointment addAppointment(int patientId, int doctorId) throws ResourceNotFoundException, InvalidInputException {
		//get patient by id else throw exception
		Patient patient = patientRepository.findById(patientId).orElseThrow(()->new ResourceNotFoundException("No patient records with the given patient id...!!!"));
		//get doctor by id else throw exception
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(()->new ResourceNotFoundException("No doctor records with the given doctor id...!!!"));
		
		//create appointment
		Appointment newAppointment=new Appointment();
		newAppointment.setDoctor(doctor);
		newAppointment.setPatient(patient);
		newAppointment.setAppointmentStatus(AppointmentStatus.PENDING);
		//save and return appointment
		return appointmentRepository.save(newAppointment);
	}




	public List<Patient> getByStatus(AppointmentStatus status) throws ResourceNotFoundException {
		List<Patient> patientList=appointmentRepository.getPatientsByStatus(status);
		if(patientList==null||patientList.isEmpty()) {
			throw new ResourceNotFoundException("No  patient records fouund with the given status...!!!");
		}
		return null;
	}
	
	
	
	
	
}
