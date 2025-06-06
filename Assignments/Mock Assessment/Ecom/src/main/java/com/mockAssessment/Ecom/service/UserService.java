package com.mockAssessment.Ecom.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mockAssessment.Ecom.exception.InvalidInputException;
import com.mockAssessment.Ecom.exception.ResourceExistsException;
import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.User;
import com.mockAssessment.Ecom.repository.UserRepository;
import com.mockAssessment.Ecom.validation.UserValidation;

@Service
public class UserService {
	
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder=passwordEncoder;
	}
	
	

	public User addUser(User user)throws ResourceExistsException,InvalidInputException, Exception {
		UserValidation.validateUser(user);
		if(userRepository.getByUsername(user.getUsername())!=null) {
			throw new ResourceExistsException("Username Not Available...!!!");
		}
		String encodedPassword=passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
		return userRepository.save(user);
	}

	

	public User getUserByUsername(String username) throws ResourceNotFoundException, Exception {
		User user= userRepository.getByUsername(username);
		if(user==null) {
			throw new ResourceNotFoundException("No User record with the given username...!!!");
		}
		return null;
	}



	public User getUserById(int id) throws ResourceNotFoundException, Exception {
		
		return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No User record with the given id...!!!"));
	}



	public List<User> getAllUser() {
		return userRepository.findAll();
	}



	public User updateUser(User user, String username) throws InvalidInputException, Exception {
		if(user==null) {
			throw new InvalidInputException("Provided user object is null..!!");
		}
		User previousUser= userRepository.getByUsername(username);
		if(user.getUsername()!=null && !user.getUsername().trim().isEmpty()) {
			previousUser.setUsername(user.getUsername());
		}
		if(user.getPassword()!=null && !user.getPassword().trim().isEmpty()) {
			String encodedPassword= passwordEncoder.encode(user.getPassword());
			previousUser.setPassword(encodedPassword);
		}
		if(user.getRole()!=null) {
			previousUser.setRole(user.getRole());
		}
		
		return userRepository.save(previousUser);
	}

}
