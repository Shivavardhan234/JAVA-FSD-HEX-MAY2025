package com.hospitalManagementSystem.HospitalManagementSystem.dto;

import com.hospitalManagementSystem.HospitalManagementSystem.model.MedicalHistory;

public class MedicalHistoryDto {

	private String illness;
	
	int numberOfYears;
	private String medication;
	
	
	
//------------------------------------------- constructor ---------------------------------------------------
	
	public MedicalHistoryDto(String illness, int numberOfYears, String medication) {
		super();
		this.illness = illness;
		this.numberOfYears = numberOfYears;
		this.medication = medication;
	}
	
	
	
	public MedicalHistoryDto() {
		super();
	}
	
	public MedicalHistoryDto(MedicalHistory medicalHistory) {
		super();
		this.illness = medicalHistory.getIllness();
		this.numberOfYears =medicalHistory.getNumberOfYears();
		this.medication = medicalHistory.getMedication();
	}
	
	
	
	
//------------------------------------- getters and setters -------------------------------------------
	
	public String getIllness() {
		return illness;
	}
	public int getNumberOfYears() {
		return numberOfYears;
	}
	public String getMedication() {
		return medication;
	}
	public void setIllness(String illness) {
		this.illness = illness;
	}
	public void setNumberOfYears(int numberOfYears) {
		this.numberOfYears = numberOfYears;
	}
	public void setMedication(String medication) {
		this.medication = medication;
	}
	
	
	
	
	
	
	

}
