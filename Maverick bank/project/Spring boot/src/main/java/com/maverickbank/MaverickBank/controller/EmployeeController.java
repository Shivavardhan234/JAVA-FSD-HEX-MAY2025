package com.maverickbank.MaverickBank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.users.Employee;
import com.maverickbank.MaverickBank.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
	@Autowired
	EmployeeService es;
	
	
	@PostMapping("/add")
	public Employee addEmployee(@RequestBody Employee employee) throws InvalidInputException, ResourceExistsException,ResourceNotFoundException, Exception {
		return es.addEmployee(employee);
		
	}
	
	
	@GetMapping("/get-all")
	public List<Employee> getAllEmployee() throws Exception{
		return es.getAllEmployee();
	}
	
	@GetMapping("/get-by-username/{username}")
	public Employee getEmployeeByUsername(@PathVariable String username) throws InvalidInputException, ResourceNotFoundException, Exception {
		return es.getEmployeeByUsername(username);
	}
	
	@GetMapping("/get-by-email/{email}")
	public Employee getEmployeeByEmail(@PathVariable String email) throws InvalidInputException, ResourceNotFoundException, Exception {
		return es.getEmployeeByEmail(email);
	}
	
	@GetMapping("/get-by-contactNumber/{email}")
	public Employee getEmployeeByContactNumber(@PathVariable String contactNumber) throws InvalidInputException, ResourceNotFoundException, Exception {
		return es.getEmployeeByContactNumber(contactNumber);
	}
}
