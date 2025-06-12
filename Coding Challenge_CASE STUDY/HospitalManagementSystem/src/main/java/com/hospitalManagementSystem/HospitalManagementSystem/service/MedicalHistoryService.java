package com.hospitalManagementSystem.HospitalManagementSystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospitalManagementSystem.HospitalManagementSystem.dto.MedicalHistoryDto2;
import com.hospitalManagementSystem.HospitalManagementSystem.enums.Role;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.ResourceNotFoundException;
import com.hospitalManagementSystem.HospitalManagementSystem.model.MedicalHistory;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Patient;
import com.hospitalManagementSystem.HospitalManagementSystem.model.User;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.MedicalHistoryRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.PatientRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.UserRepository;

@Service
public class MedicalHistoryService {
	
	MedicalHistoryRepository medicalHistoryRepository;
	
	PasswordEncoder passwordEncoder;
	UserRepository userRepository;
	
	PatientRepository patientRepository;
	
	
	

	public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepository, PasswordEncoder passwordEncoder,
			UserRepository userRepository, PatientRepository patientRepository) {
		super();
		this.medicalHistoryRepository = medicalHistoryRepository;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.patientRepository = patientRepository;
	}





	public MedicalHistoryDto2 getMedicalHistoryByPatientId(int patientId) throws ResourceNotFoundException {
		
		//get the list
		List<MedicalHistory> medicalHistoryList= medicalHistoryRepository.getMedicalHistoryByPatientId(patientId);
		//validate the list
		if(medicalHistoryList==null||medicalHistoryList.isEmpty()) {
			throw new ResourceNotFoundException("No medical History records with the given patient  id...!!!");
		}
		
		
		//one patient, list of medical history
		MedicalHistoryDto2 medicalHistoryDto2=new MedicalHistoryDto2(medicalHistoryList.get(0).getPatient(),medicalHistoryList);
		
		return medicalHistoryDto2;
	}





	public MedicalHistoryDto2 addFirstTime(MedicalHistory medicalHistory) throws InvalidInputException {
		MedicalHistory validatedMedicalHistory=new MedicalHistory();
		//Initializing another object to validate the input object
		validatedMedicalHistory.setIllness(medicalHistory.getIllness());
		validatedMedicalHistory.setMedication(medicalHistory.getMedication());
		validatedMedicalHistory.setNumberOfYears(medicalHistory.getNumberOfYears());
		
		//this is only to check whether the obj is null or not
		validatedMedicalHistory.setPatient(medicalHistory.getPatient());
		
		
		Patient validatedPatient=new Patient();
		
		
		//same action, to verify properties
		validatedPatient.setName(medicalHistory.getPatient().getName());
		validatedPatient.setAge(medicalHistory.getPatient().getAge());
		//checking null or not
		validatedPatient.setUser(medicalHistory.getPatient().getUser());
		
		User validatedUser=new User();
		//Validate user
		validatedUser.setRole(Role.PATIENT);
		validatedUser.setUsername(medicalHistory.getPatient().getUser().getUsername());
		//Encode password using password encoder
		String encodedPassword=passwordEncoder.encode(medicalHistory.getPatient().getUser().getPassword());
		validatedUser.setPassword(encodedPassword);
		//Gives the user with id
		validatedUser=userRepository.save(validatedUser);
		//set user with id to patient
		validatedPatient.setUser(validatedUser);
		//save patient
		validatedPatient=patientRepository.save(validatedPatient);
		//set patient to medical history
		validatedMedicalHistory.setPatient(validatedPatient);
		//save medical history
		validatedMedicalHistory=medicalHistoryRepository.save(validatedMedicalHistory);
		//As customer is viewing lets not give sensitive data like id so using dto
		List<MedicalHistory> medicalHistoryList=new ArrayList<>();
		medicalHistoryList.add(validatedMedicalHistory);
		
		//return dto
		return new MedicalHistoryDto2(validatedPatient,medicalHistoryList);
	}

	
	
	
}
