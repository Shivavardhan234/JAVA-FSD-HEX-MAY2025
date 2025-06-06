package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.Employee;
import com.maverickbank.MaverickBank.repository.BranchRepository;
import com.maverickbank.MaverickBank.repository.EmployeeRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.EmployeeValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;
import com.maverickbank.MaverickBank.validation.ActorValidation;

@Service
public class EmployeeService {

	EmployeeRepository employeeRepository;
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	BranchRepository branchRepository;
	UserService userService;
	
	
	
	
	public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository,
				PasswordEncoder passwordEncoder, BranchRepository branchRepository, UserService userService) {
			super();
			this.employeeRepository = employeeRepository;
			this.userRepository = userRepository;
			this.passwordEncoder = passwordEncoder;
			this.branchRepository = branchRepository;
			this.userService = userService;
		}



//-------------------------------------------- ADD ----------------------------------------------------------------------


	/** Add Employee is a method where we are validating the details of an employee and saves into database
	 * Step 1: Validate employee using validation classes 
	 * Step 2: As contact number and email are unique check weather they exists, if exists throw resource exists exception
	 * Step 3: Fetch branch details using branch name, If not exists throw resource not found exception
	 * Step 4: set status to active as we are creating it now
	 * Step 5: Extract the user 
	 * Step 6: set the role to the user based on the designation
	 * Step 7: save the user and set the returned user object to employee object
	 * Step 8: Save employee in the db
	 * 
	 * @param employee
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceExistsException
	 * @throws ResourceNotFoundException
	 * @throws Exception
	 */
	public Employee addEmployee(Employee employee) throws InvalidInputException, ResourceExistsException,ResourceNotFoundException {
		
		//Validate the employee object
		EmployeeValidation.validateEmployee(employee);
		
		//Checking weather the phone number and email exists
		if(employeeRepository.getByContactNumber(employee.getContactNumber())!=null){
			throw new ResourceExistsException("This Contact number already exists!!!");	
		}
		
		if(employeeRepository.getByEmail(employee.getEmail())!=null){
			throw new ResourceExistsException("This Email already exists!!!");	
		}
		
		//get branch by its id
		Branch branch =branchRepository.findById(employee.getBranch().getId()).orElseThrow(()-> new ResourceNotFoundException("Branch not found...!!!"));
		employee.setBranch(branch);
		
		//Get extract user obj from employee
		User user=employee.getUser();
		
		//Add the designation and set status
		user.setRole(Role.valueOf(employee.getDesignation()));
		user.setStatus(ActiveStatus.ACTIVE);
		employee.setUser(userService.addUser(user));
		
		//Save employee
		return employeeRepository.save(employee);
	}


	
//-------------------------------------- GET ----------------------------------------------------------------------------

	/**Gets all the employees
	 * @param principal 
	 * @return
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public List<Employee> getAllEmployee(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Check customer is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		//Get all employees
		return employeeRepository.findAll();
	}

	


	/**Gets employee by username
	 * @param username
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws Exception
	 */
	public Employee getEmployeeByUsername(String username, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException{
		//Check customer is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		
		//Get the employee and store in employee object
		Employee employee=employeeRepository.getEmployeeByUsername(username);
		if(employee==null) {
			throw new ResourceNotFoundException("No employee record found with given username...!!!");
		}
		return employee;
	}
	
	
	
	
	/**Gets Employee by contact number
	 * @param contactNumber
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws Exception
	 */
	public Employee getEmployeeByContactNumber(String contactNumber, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException {
		//Check customer is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		
		Employee employee=employeeRepository.getByContactNumber(contactNumber);
		if(employee==null) {
			throw new ResourceNotFoundException("No employee record found with given contact number...!!!");
		}
		//return employee
		return employee;
	}
	
	
	
	
	
	/**Gets Employee by email
	 * @param email
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws Exception
	 */
	public Employee getEmployeeByEmail(String email, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException {
		//Check customer is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		Employee employee=employeeRepository.getByEmail(email);
		if(employee==null) {
			throw new ResourceNotFoundException("No employee record found with given email...!!!");
		}
		return employee;
	}



	/**fetches employee by employee id
	 * @param id
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 * @throws ResourceNotFoundException 
	 */
	public Employee getEmployeeById(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check customer is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		
		return employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No employee record with given id...!!!"));
	}
	
	
	public Employee getEmployeeByUserId(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check customer is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		//Fetch the employee
		Employee currentEmployee = employeeRepository.getEmployeeByUserId(id);
		//validate the employee
		if(currentEmployee==null) {
			throw new ResourceNotFoundException("No employee record with the given user id...!!!");
		}
		//return employee
		return currentEmployee;
	}



	public List<Employee> getEmployeeByDesignation(String designation, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check customer is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		//Fetch the employees
		List<Employee> employeeList=employeeRepository.getEmployeeByDesignation(designation);
		//throw exception if list is empty
		if(employeeList==null) {
			throw new ResourceNotFoundException("No employee record found with given designation...!!!");
		}
		//return list
		return employeeList;
	}



	public List<Employee> getEmployeeByBranch(String branch, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check customer is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
				
		//Fetch the employees
		List<Employee> employeeList=employeeRepository.getEmployeeByBranch(branch);
		//throw exception if list is empty
		if(employeeList==null) {
			throw new ResourceNotFoundException("No employee record found with given branch...!!!");
		}
		//return list
		return employeeList;
	}
	
	public Employee getCurrentEmployee(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Check customer is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		//Fetch and return the employee
		return employeeRepository.getEmployeeByUserId(currentUser.getId());
	}

//---------------------------------------- UPDATE -----------------------------------------------------------------------

	public Employee updateEmployeePersonalDetails(Employee employee, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		
		//Checking active Status
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				//Get old employee details
				Employee currentEmployee = employeeRepository.getEmployeeByUserId(currentUser.getId());
				
				//Validate and update
				if(employee.getName()!=null) {
					ActorValidation.validateName(employee.getName());
					currentEmployee.setName(employee.getName());
				}
				if(employee.getDob()!=null) {
					ActorValidation.validateDob(employee.getDob());
					currentEmployee.setDob(employee.getDob());
				}
				if(employee.getGender()!=null) {
					ActorValidation.validateGender(employee.getGender());
					currentEmployee.setGender(employee.getGender());
				}
				if(employee.getEmail()!=null) {
					ActorValidation.validateEmail(employee.getEmail());
					currentEmployee.setEmail(employee.getEmail());
				}
				if(employee.getContactNumber()!=null) {
					ActorValidation.validateContactNumber(employee.getContactNumber());
					currentEmployee.setContactNumber(employee.getContactNumber());
				}
				if(employee.getAddress()!=null) {
					ActorValidation.validateAddress(employee.getAddress());
					currentEmployee.setAddress(employee.getAddress());
				}
				
				
		//Return updated employee		
		return employeeRepository.save(currentEmployee);
	}



	public Employee updateEmployeeProfessionalDetails(Employee employee, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		//Get old employee details
		Employee currentEmployee = employeeRepository.getEmployeeByUserId(currentUser.getId());
		
		
		if(employee.getBranch()!=null) {
			EmployeeValidation.validateBranch(employee.getBranch());
			currentEmployee.setBranch(employee.getBranch());
			
		}
		if(employee.getDesignation()!=null) {
			EmployeeValidation.validateDesignation(employee.getDesignation());
			currentEmployee.setDesignation(employee.getDesignation());
		}
		
		//Return updated employee		
		return employeeRepository.save(currentEmployee);
	}



	



	

}
