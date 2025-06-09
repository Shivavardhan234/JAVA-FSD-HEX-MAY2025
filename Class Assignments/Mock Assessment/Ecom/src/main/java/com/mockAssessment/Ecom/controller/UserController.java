package com.mockAssessment.Ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mockAssessment.Ecom.exception.InvalidInputException;
import com.mockAssessment.Ecom.exception.ResourceExistsException;
import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.User;
import com.mockAssessment.Ecom.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@PostMapping("/add")
	public User addUser(@RequestBody User user) throws ResourceExistsException, InvalidInputException, Exception {
		return userService.addUser(user);
		
	}
	
	@GetMapping("/get/by-id/{id}")
	public User getUserById(@PathVariable int id) throws ResourceNotFoundException, Exception {
		return userService.getUserById(id);	
	}
	
	
	@GetMapping("/get/by-username/{username}")
	public User getUserByUsername(@PathVariable String username) throws ResourceNotFoundException, Exception {
		return userService.getUserByUsername(username);	
	}
	
	
	
	@GetMapping("/get/all")
	public List<User> getAllUser(){
		return userService.getAllUser();
	}
	@PutMapping("/update/user")
	public User updateUser(@RequestBody User user, Principal principal) throws InvalidInputException , Exception{
		String username= principal.getName();
		
		return userService.updateUser(user, username);
		
	}
	

}
