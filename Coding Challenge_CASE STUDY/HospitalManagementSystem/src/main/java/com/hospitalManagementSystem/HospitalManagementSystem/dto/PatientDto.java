package com.hospitalManagementSystem.HospitalManagementSystem.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hospitalManagementSystem.HospitalManagementSystem.model.Patient;
@Component
public class PatientDto {

	private String name;
	

	int age;

//---------------------------------------------- constructor -----------------------------------------------
	public PatientDto() {
		super();
	}


	public PatientDto(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	
	
	public PatientDto(Patient patient) {
		super();
		this.name = patient.getName();
		this.age = patient.getAge();
	}

	
	
	//---------------------------------- getters and setters ------------------------------------------------

	public String getName() {
		return name;
	}


	public int getAge() {
		return age;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setAge(int age) {
		this.age = age;
	}

	
	
	public List<PatientDto> createPatientDtoList(List<Patient> patientList){
		List<PatientDto> patientDtoList=new ArrayList<>();
		for(Patient p : patientList) {
			patientDtoList.add(new PatientDto(p));
		}
		return patientDtoList;
	}
	
	
	
	
}
