package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
    User sampleUserEmployee;
    User sampleUserAdmin;
    Employee sampleEmployee1;
    Employee sampleEmployee2;
    Branch sampleBranch;
    Principal samplePrincipalEmployee;
    Principal samplePrincipalAdmin;

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
    public void testGetEmployeeByUsername_Success() throws Exception {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUsername("empuser1")).thenReturn(sampleEmployee1);

        // Act
        Employee foundEmployee = employeeService.getEmployeeByUsername("empuser1", samplePrincipalEmployee);

        // Assert
        assertNotNull(foundEmployee);
        assertEquals("John Doe", foundEmployee.getName());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).getEmployeeByUsername("empuser1");
    }

    @Test
    public void testGetEmployeeByUsername_NotFound() {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUsername("nonexistentuser")).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByUsername("nonexistentuser", samplePrincipalEmployee);
        });
        assertEquals("No employee record found with given username...!!!", exception.getMessage());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).getEmployeeByUsername("nonexistentuser");
    }
    

    @Test
    public void testGetEmployeeByContactNumber_Success() throws Exception {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.getByContactNumber("9876543210")).thenReturn(sampleEmployee1);

        // Act
        Employee foundEmployee = employeeService.getEmployeeByContactNumber("9876543210", samplePrincipalEmployee);

        // Assert
        assertNotNull(foundEmployee);
        assertEquals("John Doe", foundEmployee.getName());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).getByContactNumber("9876543210");
    }

    @Test
    public void testGetEmployeeByContactNumber_NotFound() {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.getByContactNumber("0000000000")).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByContactNumber("0000000000", samplePrincipalEmployee);
        });
        assertEquals("No employee record found with given contact number...!!!", exception.getMessage());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).getByContactNumber("0000000000");
    }



    @Test
    public void testGetEmployeeByEmail_Success() throws Exception {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.getByEmail("john.doe@example.com")).thenReturn(sampleEmployee1);

        // Act
        Employee foundEmployee = employeeService.getEmployeeByEmail("john.doe@example.com", samplePrincipalEmployee);

        // Assert
        assertNotNull(foundEmployee);
        assertEquals("John Doe", foundEmployee.getName());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).getByEmail("john.doe@example.com");
    }

    @Test
    public void testGetEmployeeByEmail_NotFound() {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.getByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByEmail("nonexistent@example.com", samplePrincipalEmployee);
        });
        assertEquals("No employee record found with given email...!!!", exception.getMessage());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).getByEmail("nonexistent@example.com");
    }

 

    @Test
    public void testGetEmployeeById_Success() throws Exception {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.findById(101)).thenReturn(Optional.of(sampleEmployee1));

        // Act
        Employee foundEmployee = employeeService.getEmployeeById(101, samplePrincipalEmployee);

        // Assert
        assertNotNull(foundEmployee);
        assertEquals("John Doe", foundEmployee.getName());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).findById(101);
    }

    @Test
    public void testGetEmployeeById_NotFound() {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(999, samplePrincipalEmployee);
        });
        assertEquals("No employee record with given id...!!!", exception.getMessage());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).findById(999);
    }

  

    @Test
    public void testGetEmployeeByUserId_Success() throws Exception {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUserId(sampleUserEmployee.getId())).thenReturn(sampleEmployee1);

        // Act
        Employee foundEmployee = employeeService.getEmployeeByUserId(sampleUserEmployee.getId(), samplePrincipalEmployee);

        // Assert
        assertNotNull(foundEmployee);
        assertEquals("John Doe", foundEmployee.getName());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).getEmployeeByUserId(sampleUserEmployee.getId());
    }

    @Test
    public void testGetEmployeeByUserId_NotFound() {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUserId(999)).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByUserId(999, samplePrincipalEmployee);
        });
        assertEquals("No employee record with the given user id...!!!", exception.getMessage());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).getEmployeeByUserId(999);
    }

 
    @Test
    public void testGetEmployeeByDesignation_Success() throws Exception {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalAdmin.getName())).thenReturn(sampleUserAdmin);
        when(employeeRepository.getEmployeeByDesignation("LOAN_OFFICER")).thenReturn(Arrays.asList(sampleEmployee1));

        // Act
        List<Employee> employees = employeeService.getEmployeeByDesignation("LOAN_OFFICER", samplePrincipalAdmin);

        // Assert
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getName());
        verify(userRepository).getByUsername(samplePrincipalAdmin.getName());
        verify(employeeRepository).getEmployeeByDesignation("LOAN_OFFICER");
    }

    @Test
    public void testGetEmployeeByDesignation_NotFound() {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalAdmin.getName())).thenReturn(sampleUserAdmin);
        when(employeeRepository.getEmployeeByDesignation("NON_EXISTENT")).thenReturn(null); // Or an empty list

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByDesignation("NON_EXISTENT", samplePrincipalAdmin);
        });
        assertEquals("No employee record found with given designation...!!!", exception.getMessage());
        verify(userRepository).getByUsername(samplePrincipalAdmin.getName());
        verify(employeeRepository).getEmployeeByDesignation("NON_EXISTENT");
    }

   
    @Test
    public void testGetEmployeeByBranch_Success() throws Exception {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalAdmin.getName())).thenReturn(sampleUserAdmin);
        when(employeeRepository.getEmployeeByBranch("MUMBAI_CITY")).thenReturn(Arrays.asList(sampleEmployee1, sampleEmployee2));

        // Act
        List<Employee> employees = employeeService.getEmployeeByBranch("MUMBAI_CITY", samplePrincipalAdmin);

        // Assert
        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertEquals("John Doe", employees.get(0).getName());
        assertEquals("Jane Smith", employees.get(1).getName());
        verify(userRepository).getByUsername(samplePrincipalAdmin.getName());
        verify(employeeRepository).getEmployeeByBranch("MUMBAI_CITY");
    }

    @Test
    public void testGetEmployeeByBranch_NotFound() {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalAdmin.getName())).thenReturn(sampleUserAdmin);
        when(employeeRepository.getEmployeeByBranch("NON_EXISTENT_BRANCH")).thenReturn(null); // Or an empty list

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByBranch("NON_EXISTENT_BRANCH", samplePrincipalAdmin);
        });
        assertEquals("No employee record found with given branch...!!!", exception.getMessage());
        verify(userRepository).getByUsername(samplePrincipalAdmin.getName());
        verify(employeeRepository).getEmployeeByBranch("NON_EXISTENT_BRANCH");
    }



    @Test
    public void testGetCurrentEmployee_Success() throws Exception {
        // Arrange
        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUserId(sampleUserEmployee.getId())).thenReturn(sampleEmployee1);

        // Act
        Employee currentEmployee = employeeService.getCurrentEmployee(samplePrincipalEmployee);

        // Assert
        assertNotNull(currentEmployee);
        assertEquals("John Doe", currentEmployee.getName());
        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).getEmployeeByUserId(sampleUserEmployee.getId());
    }



    @Test
    public void testUpdateEmployeePersonalDetails_Success() throws Exception {
        // Arrange
        Employee updateInfo = new Employee();
        updateInfo.setName("Johnathan Doe");
        updateInfo.setEmail("johnathan.doe.updated@example.com");
        updateInfo.setDob(LocalDate.of(1986, 2, 20)); // New DOB
        updateInfo.setContactNumber("9988776655"); // New Contact

        when(userRepository.getByUsername(samplePrincipalEmployee.getName())).thenReturn(sampleUserEmployee);
        when(employeeRepository.getEmployeeByUserId(sampleUserEmployee.getId())).thenReturn(sampleEmployee1);
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee1); // Return the modified sampleEmployee1

        // Act
        Employee updatedEmployee = employeeService.updateEmployeePersonalDetails(updateInfo, samplePrincipalEmployee);

        // Assert
        assertNotNull(updatedEmployee);
        assertEquals("Johnathan Doe", updatedEmployee.getName());
        assertEquals("johnathan.doe.updated@example.com", updatedEmployee.getEmail());
        assertEquals(LocalDate.of(1986, 2, 20), updatedEmployee.getDob());
        assertEquals("9988776655", updatedEmployee.getContactNumber());
        // Verify that original fields remain if not updated
        assertEquals(Gender.MALE, updatedEmployee.getGender()); // Gender wasn't updated

        verify(userRepository).getByUsername(samplePrincipalEmployee.getName());
        verify(employeeRepository).getEmployeeByUserId(sampleUserEmployee.getId());
        verify(employeeRepository).save(sampleEmployee1); // Verify save was called with the modified object
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
