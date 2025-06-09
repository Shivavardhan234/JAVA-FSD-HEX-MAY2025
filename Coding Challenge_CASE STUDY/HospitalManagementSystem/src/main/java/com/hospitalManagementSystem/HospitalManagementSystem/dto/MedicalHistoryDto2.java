package com.hospitalManagementSystem.HospitalManagementSystem.dto;

import java.util.ArrayList;
import java.util.List;

import com.hospitalManagementSystem.HospitalManagementSystem.model.MedicalHistory;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Patient;


public class MedicalHistoryDto2 {

	
	private PatientDto patientDto;
	
	private List<MedicalHistoryDto> medicalHistoryDtoList;

	
	
	//---------------------------------------------------- constructor ------------------------------------------
	
	
	public MedicalHistoryDto2() {
		super();
	}
	public MedicalHistoryDto2(Patient patient, List<MedicalHistory> medicalHistoryList) {
		super();
		this.patientDto = new PatientDto(patient);
		this.medicalHistoryDtoList = convertToMedicalHistoryDtoList(medicalHistoryList);
	}
	
	
	//------------------------------------------ getters and setters -----------------------------------------

	
	public PatientDto getPatientDto() {
		return patientDto;
	}

	public List<MedicalHistoryDto> getMedicalHistoryDtoList() {
		return medicalHistoryDtoList;
	}

	public void setPatientDto(PatientDto patientDto) {
		this.patientDto = patientDto;
	}

	public void setMedicalHistoryDtoList(List<MedicalHistoryDto> medicalHistoryDtoList) {
		this.medicalHistoryDtoList = medicalHistoryDtoList;
	}
	

	
	private List<MedicalHistoryDto> convertToMedicalHistoryDtoList(List<MedicalHistory> medicalHistoryList){
		
		List<MedicalHistoryDto> medicalHistoryDtoList =new ArrayList<>();
		for(MedicalHistory m:medicalHistoryList) {
			 medicalHistoryDtoList.add(new MedicalHistoryDto(m));
		}
		return medicalHistoryDtoList;
	}
	
	
	
	
	
	
}
