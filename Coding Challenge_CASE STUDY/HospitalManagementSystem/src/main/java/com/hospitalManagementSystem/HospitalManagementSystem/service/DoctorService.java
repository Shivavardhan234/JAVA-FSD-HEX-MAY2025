package com.hospitalManagementSystem.HospitalManagementSystem.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospitalManagementSystem.HospitalManagementSystem.enums.Role;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Doctor;
import com.hospitalManagementSystem.HospitalManagementSystem.model.User;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.DoctorRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.UserRepository;

@Service
public class DoctorService {

	
	UserRepository userRepository;
	DoctorRepository doctorRepository;
	PasswordEncoder passwordEncoder;
	
	
	
	
	
	
	public DoctorService(UserRepository userRepository, DoctorRepository doctorRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.doctorRepository = doctorRepository;
		this.passwordEncoder = passwordEncoder;
	}






	public Doctor addDoctor(Doctor doctor) throws InvalidInputException {
		Doctor validatedDoctor=new Doctor();
		
		validatedDoctor.setName(doctor.getName());
		validatedDoctor.setSpeciality(doctor.getSpeciality());
		validatedDoctor.setUser(doctor.getUser());
		
		User validatedUser =new User();
		validatedUser.setUsername(doctor.getUser().getUsername());
		validatedUser.setRole(Role.DOCTOR);
		String encodedPassword = passwordEncoder.encode(doctor.getUser().getPassword());
		validatedUser.setPassword(encodedPassword);
		
		validatedUser=userRepository.save(validatedUser);
		validatedDoctor.setUser(validatedUser);
		return doctorRepository.save(validatedDoctor);
		
	}
}
