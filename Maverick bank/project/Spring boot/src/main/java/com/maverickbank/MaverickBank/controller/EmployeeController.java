package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.users.Employee;
import com.maverickbank.MaverickBank.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

//--------------------------------------------- POST --------------------------------------------------------------------
	
	@PostMapping("/add")
	@CrossOrigin(origins = "http://localhost:5173")
	public Employee addEmployee(@RequestBody Employee employee) throws InvalidInputException, ResourceExistsException,ResourceNotFoundException{
		return employeeService.addEmployee(employee);
		
	}
	
//-------------------------------------------- GET -----------------------------------------------------------------------
	@GetMapping("/get/all")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Employee> getAllEmployee(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		return employeeService.getAllEmployee(page,size,principal);
	}
	
	@GetMapping("/get/by-branch-id/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Employee> getEmployeeByBranchId(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@PathVariable int id, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
		return employeeService.getEmployeeByBranchId(page,size,id,principal);
	}
	
	@GetMapping("/get/by-status/{status}")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Employee> getEmployeeByStatus(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@PathVariable ActiveStatus status, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
		return employeeService.getEmployeeByStatus(page,size,status,principal);
	}
	
	@GetMapping("/get/by-id/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Employee getEmployeeById(@PathVariable int id, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
		return employeeService.getEmployeeById(id,principal);
	}
	
	@GetMapping("/get/by-user-id/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Employee getEmployeeByUserId(@PathVariable int id, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
		return employeeService.getEmployeeByUserId(id,principal);
	}
	
	
	@GetMapping("/get/by-designation")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Employee> getEmployeeByDesignation(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,
			@RequestParam String designation, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
		return employeeService.getEmployeeByDesignation(page,size,designation,principal);
	}
	
	@GetMapping("/get/by-branch")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Employee> getEmployeeByBranch(@RequestParam String branch, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
		return employeeService.getEmployeeByBranch(branch,principal);
	}
	
	
	@GetMapping("/get/by-username")
	@CrossOrigin(origins = "http://localhost:5173")
	public Employee getEmployeeByUsername(@RequestParam String username, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
		return employeeService.getEmployeeByUsername(username,principal);
	}
	
	@GetMapping("/get/by-email")
	@CrossOrigin(origins = "http://localhost:5173")
	public Employee getEmployeeByEmail(@RequestParam String email, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException {
		return employeeService.getEmployeeByEmail(email,principal);
	}
	
	@GetMapping("/get/by-contactNumber")
	@CrossOrigin(origins = "http://localhost:5173")
	public Employee getEmployeeByContactNumber(@RequestParam String contactNumber, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException {
		return employeeService.getEmployeeByContactNumber(contactNumber,principal);
	}
	
	
	@GetMapping("/get/current-employee")
	public Employee getCurrentEmployee( Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
		return employeeService.getCurrentEmployee(principal);
	}
	
	
	
	
	
	//-------------------------------------------- UPDATE EMPLOYEE -------------------------------------------------------
	
	@PutMapping("/update/personal-details")
	@CrossOrigin(origins = "http://localhost:5173")
	public Employee updateEmployeePersonalDetails(Employee employee, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		return employeeService.updateEmployeePersonalDetails(employee,principal);
	}
	
	@PutMapping("/update/professional-details")
	@CrossOrigin(origins = "http://localhost:5173")
	public Employee updateEmployeeProfessionalDetails(Employee employee, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		return employeeService.updateEmployeeProfessionalDetails(employee,principal);
	}
}
