package com.hospitalManagementSystem.HospitalManagementSystem.model;

import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="medical_history")
public class MedicalHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String illness;
	
	
	@Column(name="number_od_years",nullable=false)
	int numberOfYears;
	@Column(nullable = false)
	private String medication;
	
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Patient patient;


	
	//-------------------------------------------- constructor ----------------------------------------------
	public MedicalHistory() {
		super();
	}


	public MedicalHistory(int id, String illness, int numberOfYears, String medication, Patient patient) throws InvalidInputException {
		super();
		this.setId(id);
		this.setIllness(illness);
		this.setNumberOfYears(numberOfYears);
		this.setMedication(medication);
		this.setPatient(patient);
	}

	
//------------------------------------------- getters and setters -------------------------------------------

	public int getId() {
		return id;
	}


	public String getIllness() {
		return illness;
	}


	public int getNumberOfYears() {
		return numberOfYears;
	}


	public String getMedication() {
		return medication;
	}


	public Patient getPatient() {
		return patient;
	}


	public void setId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("medical history ID is Invalid. Please enter appropriate ID...!!!");
		}
		this.id = id;
	}
	
	
	
	
	
	public void setIllness(String illness)throws InvalidInputException {
		if(illness==null || illness.trim().isEmpty()) {
			throw new InvalidInputException("Provided illness is invalid. please enter appropriate illness...!!!");
		}
		this.illness = illness;
	}
	
	
	
	
	public void setNumberOfYears(int numberOfYears) throws InvalidInputException {
		if( numberOfYears<=0||numberOfYears>150) {
			throw new InvalidInputException("Invalid number of years. It should not be less than 0 or more than 150...!!!");
		}
		this.numberOfYears=numberOfYears;;
	}
	
	
	
	
	public void setMedication(String medication)throws InvalidInputException {
		if(medication==null || medication.trim().isEmpty()) {
			throw new InvalidInputException("medication is Invalid. Please enter appropriate medication...!!!");
		}
		this.medication=medication;
	}
	

	
	
	
	public void setPatient(Patient patient) throws InvalidInputException{
		if (patient==null) {
			throw new InvalidInputException("Provided patient object is null. Please provide appropriate patient object...!!!");
		}
		this.patient =patient;
	}
	
	
	
	
	
	
	
	
	

}
