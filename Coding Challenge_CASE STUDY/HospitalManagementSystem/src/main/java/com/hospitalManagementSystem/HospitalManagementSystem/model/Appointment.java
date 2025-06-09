package com.hospitalManagementSystem.HospitalManagementSystem.model;



import com.hospitalManagementSystem.HospitalManagementSystem.enums.AppointmentStatus;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Appointment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Doctor doctor;
	
	

	
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentStatus appointmentStatus;

	
	
	
	//-------------------------------------------------- constructor ------------------------------------------

	public Appointment() {
		super();
	}


	public Appointment(int id, Patient patient, Doctor doctor, 
			AppointmentStatus appointmentStatus) {
		super();
		this.id = id;
		this.patient = patient;
		this.doctor = doctor;
		this.appointmentStatus = appointmentStatus;
	}
	//------------------------------------------ getters and setters ------------------------------------------

	public int getId() {
		return id;
	}


	public Patient getPatient() {
		return patient;
	}


	public Doctor getDoctor() {
		return doctor;
	}


	

	public AppointmentStatus getAppointmentStatus() {
		return appointmentStatus;
	}

	
	
	

	public void setId(int id)throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("appointment ID is Invalid. Please enter appropriate ID...!!!");
		}
		this.id = id;
	}


	public void setPatient(Patient patient)throws InvalidInputException {
		if (patient==null) {
			throw new InvalidInputException("Provided patient object is null. Please provide appropriate patient object...!!!");
		}
		this.patient = patient;
	}


	public void setDoctor(Doctor doctor)throws InvalidInputException {
		if (doctor==null) {
			throw new InvalidInputException("Provided doctor object is null. Please provide appropriate doctor  object...!!!");
		}
		this.doctor = doctor;
	}




	public void setAppointmentStatus(AppointmentStatus appointmentStatus) throws InvalidInputException{
		if (appointmentStatus==null) {
			throw new InvalidInputException("Provided appointment status is null. Please provide appropriate status...!!!");
		}
		
		this.appointmentStatus = appointmentStatus;
	}
	
	
	
	
	
	
	
	
	
	
	

}
