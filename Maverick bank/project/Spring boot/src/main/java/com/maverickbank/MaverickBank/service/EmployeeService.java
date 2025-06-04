package com.maverickbank.MaverickBank.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.Role;
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

	EmployeeRepository er;
	UserRepository ur;
	PasswordEncoder pe;
	BranchRepository br;
	
	
	
	public EmployeeService(EmployeeRepository er, UserRepository ur, PasswordEncoder pe, BranchRepository br) {
		super();
		this.er = er;
		this.ur = ur;
		this.pe = pe;
		this.br=br;
	}
	
	
	
	
	



	/** Add Employee is a method where we are validating the details of an employee and saves into database
	 * Step 1: Check weather username already exists or not, if exists throw resource exists exception
	 * Step 2: Validate employee details using validation classes 
	 * Step 3: Fetch branch details using branch name, If not exists throw resource not found exception
	 * Step 4: As contact number and email are unique check weather they exists, if exists throw resource exists exception
	 * Step 5: Encode the password using password encoder
	 * Step 6: set status to active as we are creating it now
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
	public Employee addEmployee(Employee employee) throws InvalidInputException, ResourceExistsException,ResourceNotFoundException, Exception {
		//Check weather employee is null or not
		if(employee==null) {
			throw new InvalidInputException("Null Employee object provided...!!!");
		}
		//Validate the employee object
		EmployeeValidation.fullEmployeeValidation(employee);
		
		//Get extract user obj from employee
		User user=employee.getUser();
		
		//validate username
		UserValidation.validateUsername(user.getUsername());
		//Check weather username is available
		if(ur.getByUsername(user.getUsername())!=null){
			throw new ResourceExistsException("This username already exists!!!");
		}
		
		//Validate the password to be strong
		UserValidation.validatePassword(user.getPassword());
		
		//Checking weather the phone number and email exists
		if(er.getByContactNumber(employee.getContactNumber())!=null){
			throw new ResourceExistsException("This Contact number already exists!!!");	
		}
		
		if(er.getByEmail(employee.getEmail())!=null){
			throw new ResourceExistsException("This Email already exists!!!");	
		}
		
		
		//get branch by its id
		Branch branch =br.findById(employee.getBranch().getId()).orElseThrow(()-> new ResourceNotFoundException("Branch not found...!!!"));
		employee.setBranch(branch);
		String password = pe.encode(user.getPassword());
		user.setPassword(password);
		//Add the designation
		user.setRole(Role.valueOf(employee.getDesignation()));
		employee.setStatus(ActiveStatus.ACTIVE);
		employee.setUser(ur.save(user));
		
		
		return er.save(employee);
	}



	/**Gets all the employees
	 * @return
	 */
	public List<Employee> getAllEmployee()throws Exception {
		
		return er.findAll();
	}



	/**Gets employee by username
	 * @param username
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceNotFoundException
	 * @throws Exception
	 */
	public Employee getEmployeeByUsername(String username) throws InvalidInputException, ResourceNotFoundException, Exception {
		if(username==null) {
			throw new InvalidInputException("Username is null. Cannot fetch the details...!!!");
		}
		Employee employee=er.getEmployeeByUsername(username);
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
	 * @throws Exception
	 */
	public Employee getEmployeeByContactNumber(String contactNumber) throws InvalidInputException, ResourceNotFoundException, Exception {
		if(contactNumber==null) {
			throw new InvalidInputException("contact number is null. Cannot fetch the details...!!!");
		}
		Employee employee=er.getByContactNumber(contactNumber);
		if(employee==null) {
			throw new ResourceNotFoundException("No employee record found with given contact number...!!!");
		}
		return employee;
	}
	
	
	
	
	
	/**Gets Employee by email
	 * @param email
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceNotFoundException
	 * @throws Exception
	 */
	public Employee getEmployeeByEmail(String email) throws InvalidInputException, ResourceNotFoundException, Exception {
		if(email==null) {
			throw new InvalidInputException("email is null. Cannot fetch the details...!!!");
		}
		Employee employee=er.getByEmail(email);
		if(employee==null) {
			throw new ResourceNotFoundException("No employee record found with given email...!!!");
		}
		return employee;
	}

}
