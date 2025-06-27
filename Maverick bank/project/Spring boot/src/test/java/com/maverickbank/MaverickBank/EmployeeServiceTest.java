package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.Gender;
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
import com.maverickbank.MaverickBank.service.EmployeeService;
import com.maverickbank.MaverickBank.service.UserService;

@SpringBootTest 
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private UserService userService;

    // Sample data for tests
    private   User sampleUserEmployee;
    private   User sampleUserAdmin;
    private   Employee sampleEmployee1;
    private  Employee sampleEmployee2;
    private  Branch sampleBranch;
    private  Principal samplePrincipalEmployee;
    private  Principal samplePrincipalAdmin;

    @BeforeEach
    public void init() throws InvalidInputException {
        sampleUserEmployee = new User();
        sampleUserEmployee.setId(1);
        sampleUserEmployee.setUsername("empuser1");
        sampleUserEmployee.setPassword("EmpPassword@1");
        sampleUserEmployee.setRole(Role.LOAN_OFFICER);
        sampleUserEmployee.setStatus(ActiveStatus.ACTIVE);

        sampleUserAdmin = new User();
        sampleUserAdmin.setId(2);
        sampleUserAdmin.setUsername("adminuser");
        sampleUserAdmin.setPassword("AdminPassword@1");
        sampleUserAdmin.setRole(Role.ADMIN);
        sampleUserAdmin.setStatus(ActiveStatus.ACTIVE);

        sampleBranch = new Branch();
        sampleBranch.setId(1);
        sampleBranch.setIfsc("MVRK0000001");
        sampleBranch.setBranchName("MUMBAI_CITY");
        sampleBranch.setAddress("123 Marine Drive");
        sampleBranch.setContactNumber("9123456780");
        sampleBranch.setEmail("mumbai_city.mvrkBank@gmail.com");
        sampleBranch.setStatus(ActiveStatus.ACTIVE);

        sampleEmployee1 = new Employee();
        sampleEmployee1.setId(101);
        sampleEmployee1.setName("John Doe");
        sampleEmployee1.setDob(LocalDate.of(1985, 1, 15));
        sampleEmployee1.setGender(Gender.MALE);
        sampleEmployee1.setContactNumber("9876543210");
        sampleEmployee1.setEmail("john.doe@example.com");
        sampleEmployee1.setAddress("789 Employee St");
        sampleEmployee1.setDesignation("LOAN_OFFICER"); 
        sampleEmployee1.setUser(sampleUserEmployee);
        sampleEmployee1.setBranch(sampleBranch);

        sampleEmployee2 = new Employee();
        sampleEmployee2.setId(102);
        sampleEmployee2.setName("Jane Smith");
        sampleEmployee2.setDob(LocalDate.of(1990, 3, 20));
        sampleEmployee2.setGender(Gender.FEMALE);
        sampleEmployee2.setContactNumber("8765432109");
        sampleEmployee2.setEmail("jane.smith@example.com");
        sampleEmployee2.setAddress("456 Office Rd");
        sampleEmployee2.setDesignation("ACCOUNT_MANAGER");
      
        sampleEmployee2.setBranch(sampleBranch);

        samplePrincipalEmployee = mock(Principal.class);
        when(samplePrincipalEmployee.getName()).thenReturn("empuser1");

        samplePrincipalAdmin = mock(Principal.class);
        when(samplePrincipalAdmin.getName()).thenReturn("adminuser");
    }


    @Test
    public void testAddEmployee() throws Exception {
        // Case 1: success
        
       
        when(employeeRepository.getByContactNumber(sampleEmployee1.getContactNumber())).thenReturn(null);
        when(employeeRepository.getByEmail(sampleEmployee1.getEmail())).thenReturn(null);
        when(branchRepository.findById(sampleBranch.getId())).thenReturn(Optional.of(sampleBranch));
        when(userService.addUser(sampleUserEmployee)).thenReturn(sampleUserEmployee);
        when(employeeRepository.save(sampleEmployee1)).thenReturn(sampleEmployee1);

        
        Employee addedEmployee = employeeService.addEmployee(sampleEmployee1);

        assertEquals(101, addedEmployee.getId());
        assertEquals("John Doe", addedEmployee.getName());
        assertEquals(sampleUserEmployee, addedEmployee.getUser());
        assertEquals(Role.LOAN_OFFICER, addedEmployee.getUser().getRole());
        


        // Case 2: contact number already existing
        
        when(employeeRepository.getByContactNumber(sampleEmployee1.getContactNumber())).thenReturn(sampleEmployee1);

        // Act & Assert
        ResourceExistsException e1 = assertThrows(ResourceExistsException.class, () -> {
            employeeService.addEmployee(sampleEmployee1);
        });
        assertEquals("This Contact number already exists!!!", e1.getMessage());
        


        // Case 3: email already existing
        
        when(employeeRepository.getByContactNumber(sampleEmployee1.getContactNumber())).thenReturn(null);
        when(employeeRepository.getByEmail(sampleEmployee1.getEmail())).thenReturn(sampleEmployee1);

        
        ResourceExistsException e2 = assertThrows(ResourceExistsException.class, () -> {
            employeeService.addEmployee(sampleEmployee1);
        });
        assertEquals("This Email already exists!!!", e2.getMessage());
        


        // Case 4: if branch not available
        
        when(employeeRepository.getByContactNumber(sampleEmployee1.getContactNumber())).thenReturn(null);
        when(employeeRepository.getByEmail(sampleEmployee1.getEmail())).thenReturn(null);
        when(branchRepository.findById(sampleBranch.getId())).thenReturn(Optional.empty()); 

        
        ResourceNotFoundException e3 = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.addEmployee(sampleEmployee1);
        });
        assertEquals("Branch not found...!!!", e3.getMessage());
        

        // Case 5: invalid details
        
        Employee invalidEmployee = new Employee();
         

        InvalidInputException e4 = assertThrows(InvalidInputException.class, () -> {
            employeeService.addEmployee(invalidEmployee);
        });
        
        assertEquals("Name is Invalid. Please enter appropriate Name(Only alphabets and spaces allowed)...!!!", e4.getMessage()); 

        
    }

    
 //------------------------------------------------- GET -------------------------------------------
    @Test
    public void testGetAllEmployee() throws Exception {
        
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(sampleEmployee1, sampleEmployee2));

        List<Employee> expectedList=Arrays.asList(sampleEmployee1, sampleEmployee2);
        List<Employee> actualList = employeeService.getAllEmployee(samplePrincipalEmployee);

       
        assertEquals(expectedList, actualList);
        
    }
    
    
    
    @Test
    public void testGetEmployeeByUsername() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUsername("username1")).thenReturn(sampleEmployee1);

        Employee result = employeeService.getEmployeeByUsername("username1", samplePrincipalEmployee);

        assertEquals(101, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("9876543210", result.getContactNumber());
        assertEquals(sampleUserEmployee, result.getUser());

        // Case 2: Employee not found
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUsername("username2")).thenReturn(null);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByUsername("username2", samplePrincipalEmployee);
        });

        assertEquals("No employee record found with given username...!!!", e.getMessage());
    }
    
    
    
    @Test
    public void testGetEmployeeByContactNumber() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getByContactNumber("9876543210")).thenReturn(sampleEmployee1);

        Employee result = employeeService.getEmployeeByContactNumber("9876543210", samplePrincipalEmployee);

        assertEquals(101, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("9876543210", result.getContactNumber());
        assertEquals(sampleUserEmployee, result.getUser());

        // Case 2: Employee not found
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getByContactNumber("1231231234")).thenReturn(null);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByContactNumber("1231231234", samplePrincipalEmployee);
        });

        assertEquals("No employee record found with given contact number...!!!", e.getMessage());
    }

    
    @Test
    public void testGetEmployeeByEmail() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getByEmail("john.doe@example.com")).thenReturn(sampleEmployee1);

        Employee result = employeeService.getEmployeeByEmail("john.doe@example.com", samplePrincipalEmployee);

        assertEquals(101, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals(sampleUserEmployee, result.getUser());

        // Case 2: Employee not found
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getByEmail("not.found@example.com")).thenReturn(null);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByEmail("not.found@example.com", samplePrincipalEmployee);
        });

        assertEquals("No employee record found with given email...!!!", e.getMessage());
    }

    
    
    @Test
    public void testGetEmployeeById() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.findById(101)).thenReturn(Optional.of(sampleEmployee1));

        Employee result = employeeService.getEmployeeById(101, samplePrincipalEmployee);

        assertEquals(101, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("9876543210", result.getContactNumber());
        assertEquals(sampleUserEmployee, result.getUser());

        // Case 2: Employee not found
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(999, samplePrincipalEmployee);
        });

        assertEquals("No employee record with given id...!!!", e.getMessage());
    }

    
    @Test
    public void testGetEmployeeByUserId() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUserId(1)).thenReturn(sampleEmployee1);

        Employee result = employeeService.getEmployeeByUserId(1, samplePrincipalEmployee);

        assertEquals(101, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("9876543210", result.getContactNumber());
        assertEquals(sampleUserEmployee, result.getUser());

        // Case 2: Employee not found
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUserId(999)).thenReturn(null);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByUserId(999, samplePrincipalEmployee);
        });

        assertEquals("No employee record with the given user id...!!!", e.getMessage());
    }

    
    @Test
    public void testGetEmployeeByDesignation() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByDesignation("LOAN_OFFICER"))
            .thenReturn(Arrays.asList(sampleEmployee1));

        List<Employee> result = employeeService.getEmployeeByDesignation("LOAN_OFFICER", samplePrincipalEmployee);

        
        assertEquals(Arrays.asList(sampleEmployee1), result);

        // Case 2: Employee list is null
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByDesignation("ACCOUNT_MANAGER"))
            .thenReturn(null);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByDesignation("ACCOUNT_MANAGER", samplePrincipalEmployee);
        });

        assertEquals("No employee record found with given designation...!!!", e.getMessage());
    }
    
    
    
    @Test
    public void testGetEmployeeByBranchId() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByBranchId(1))
            .thenReturn(Arrays.asList(sampleEmployee1,sampleEmployee2));

        List<Employee> result = employeeService.getEmployeeByBranchId(1, samplePrincipalEmployee);

        assertEquals(Arrays.asList(sampleEmployee1,sampleEmployee2), result);

        // Case 2: Employee list is null
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByBranchId(99)).thenReturn(null);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByBranchId(99, samplePrincipalEmployee);
        });

        assertEquals("No employee record found with given branch id...!!!", e.getMessage());
    }
    
    
    @Test
    public void testGetEmployeeByStatus() throws Exception {
        // Case 1: Success (ACTIVE)
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByStatus(ActiveStatus.ACTIVE))
            .thenReturn(Arrays.asList(sampleEmployee1));

        List<Employee> result = employeeService.getEmployeeByStatus(ActiveStatus.ACTIVE, samplePrincipalEmployee);

        assertEquals(Arrays.asList(sampleEmployee1), result);

        // Case 2: No employee found for status
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByStatus(ActiveStatus.SUSPENDED))
            .thenReturn(null);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByStatus(ActiveStatus.SUSPENDED, samplePrincipalEmployee);
        });

        assertEquals("No employee record found with given activity status...!!!", e.getMessage());
    }

    
    
    @Test
    public void testGetCurrentEmployee() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUserId(1)).thenReturn(sampleEmployee1);

        Employee result = employeeService.getCurrentEmployee(samplePrincipalEmployee);

        assertEquals(101, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("9876543210", result.getContactNumber());
        assertEquals(sampleUserEmployee, result.getUser());
    }
    
    
    
  //=============================================== UPDATE ===========================================
    
    @Test
    public void testUpdateEmployeePersonalDetails() throws Exception {
        // Case 1: Successful update of all fields
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUserId(1)).thenReturn(sampleEmployee1);
        when(employeeRepository.save(sampleEmployee1)).thenReturn(sampleEmployee1);

        Employee updatedInput = new Employee();
        updatedInput.setName("Updated Name");
        updatedInput.setDob(LocalDate.of(1990, 5, 20));
        updatedInput.setGender(Gender.MALE);
        updatedInput.setEmail("updated.email@example.com");
        updatedInput.setContactNumber("9999999999");
        updatedInput.setAddress("New Address Lane");

        Employee result = employeeService.updateEmployeePersonalDetails(updatedInput, samplePrincipalEmployee);

        assertEquals("Updated Name", result.getName());
        assertEquals(LocalDate.of(1990, 5, 20), result.getDob());
        assertEquals(Gender.MALE, result.getGender());
        assertEquals("updated.email@example.com", result.getEmail());
        assertEquals("9999999999", result.getContactNumber());
        assertEquals("New Address Lane", result.getAddress());

        
    }
    
    @Test
    public void testUpdateEmployeeProfessionalDetails() throws Exception {
        // Case: Successful update of branch and designation
        when(userRepository.getByUsername("empuser1")).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUserId(1)).thenReturn(sampleEmployee1);
        when(employeeRepository.save(sampleEmployee1)).thenReturn(sampleEmployee1);

        Branch updatedBranch = new Branch();
        updatedBranch.setId(2);
        updatedBranch.setIfsc("MVRK0000002");
        updatedBranch.setBranchName("NEW_DELHI");
        updatedBranch.setAddress("456 Connaught Place");
        updatedBranch.setContactNumber("9988776655");
        updatedBranch.setEmail("delhi.mvrkBank@gmail.com");
        updatedBranch.setStatus(ActiveStatus.ACTIVE);

        Employee updatedInput = new Employee();
        updatedInput.setBranch(updatedBranch);
        updatedInput.setDesignation("SENIOR_OPERATIONS_MANAGER");

        Employee result = employeeService.updateEmployeeProfessionalDetails(updatedInput, samplePrincipalEmployee);

        assertEquals(updatedBranch, result.getBranch());
        assertEquals("SENIOR_OPERATIONS_MANAGER", result.getDesignation());
    }






   
    
 
    
   
    @AfterEach
    public void afterTest() {
        sampleUserEmployee = null;
        sampleUserAdmin = null;
        sampleEmployee1 = null;
        sampleEmployee2 = null;
        sampleBranch = null;
        samplePrincipalEmployee = null;
        samplePrincipalAdmin = null;
    }
    
}
