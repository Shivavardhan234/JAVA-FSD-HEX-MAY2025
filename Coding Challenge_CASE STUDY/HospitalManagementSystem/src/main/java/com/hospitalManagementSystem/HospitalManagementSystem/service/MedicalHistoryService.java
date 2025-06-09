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
	

	public MedicalHistoryDto2 getMedicalHistoryByPatientId(int patientId) throws ResourceNotFoundException {
		
		
		List<MedicalHistory> medicalHistoryList= medicalHistoryRepository.getMedicalHistoryByPatientId(patientId);
		if(medicalHistoryList==null||medicalHistoryList.isEmpty()) {
			throw new ResourceNotFoundException("No medical History records with the given patient  id...!!!");
		}
		
		
		
		MedicalHistoryDto2 medicalHistoryDto2=new MedicalHistoryDto2(medicalHistoryList.get(0).getPatient(),medicalHistoryList);
		
		return medicalHistoryDto2;
	}





	public MedicalHistoryDto2 addFirstTime(MedicalHistory medicalHistory) throws InvalidInputException {
		MedicalHistory validatedMedicalHistory=new MedicalHistory();
		
		validatedMedicalHistory.setIllness(medicalHistory.getIllness());
		validatedMedicalHistory.setMedication(medicalHistory.getMedication());
		validatedMedicalHistory.setNumberOfYears(medicalHistory.getNumberOfYears());
		validatedMedicalHistory.setPatient(medicalHistory.getPatient());
		
		
		Patient validatedPatient=new Patient();
		
		validatedPatient.setName(medicalHistory.getPatient().getName());
		validatedPatient.setAge(medicalHistory.getPatient().getAge());
		validatedPatient.setUser(medicalHistory.getPatient().getUser());
		
		User validatedUser=new User();
		
		validatedUser.setRole(Role.PATIENT);
		validatedUser.setUsername(medicalHistory.getPatient().getUser().getUsername());
		String encodedPassword=passwordEncoder.encode(medicalHistory.getPatient().getUser().getPassword());
		validatedUser.setPassword(encodedPassword);
		
		validatedUser=userRepository.save(validatedUser);
		validatedPatient.setUser(validatedUser);
		
		validatedPatient=patientRepository.save(validatedPatient);
		
		validatedMedicalHistory.setPatient(validatedPatient);
		
		validatedMedicalHistory=medicalHistoryRepository.save(validatedMedicalHistory);
		
		List<MedicalHistory> medicalHistoryList=new ArrayList<>();
		medicalHistoryList.add(validatedMedicalHistory);
		
		
		return new MedicalHistoryDto2(validatedPatient,medicalHistoryList);
	}

	
	
	
}
