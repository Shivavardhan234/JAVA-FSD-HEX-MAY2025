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
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.repository.CustomerRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.CustomerService;
import com.maverickbank.MaverickBank.service.UserService;


@SpringBootTest
class CustomerServiceTest {

	
	@InjectMocks 
	private CustomerService customerService;
	
	@Mock 
	private UserRepository userRepository;
	@Mock 
	private CustomerRepository customerRepository;
	@Mock 
	private UserService userService;
	

	

	User sampleUser1;
	User sampleUser2;
	Customer sampleCustomer1;
	Customer sampleCustomer2;
	Principal samplePrincipal1;
	Principal samplePrincipal2;
	
	
	
	@BeforeEach
    public void init() throws InvalidInputException {

        sampleUser1 = new User();
        sampleUser1.setId(1);
        sampleUser1.setUsername("testuser1");
        sampleUser1.setPassword("Rawpassword@1");
        sampleUser1.setRole(Role.CUSTOMER);
        sampleUser1.setStatus(ActiveStatus.ACTIVE);

        sampleUser2 = new User();
        sampleUser2.setId(2);
        sampleUser2.setUsername("testuser2");
        sampleUser2.setPassword("Rawpassword@2");
        sampleUser2.setRole(Role.ADMIN);
        sampleUser2.setStatus(ActiveStatus.ACTIVE);

        sampleCustomer1 = new Customer();
        sampleCustomer1.setId(101);
        sampleCustomer1.setName("Alice");
        sampleCustomer1.setDob(LocalDate.of(1995, 5, 20));
        sampleCustomer1.setGender(Gender.FEMALE);
        sampleCustomer1.setContactNumber("1234567890");
        sampleCustomer1.setEmail("alice@example.com");
        sampleCustomer1.setAddress("123 Wonderland Avenue");
        sampleCustomer1.setUser(sampleUser1);

        sampleCustomer2 = new Customer();
        sampleCustomer2.setId(102);
        sampleCustomer2.setName("Bob");
        sampleCustomer2.setDob(LocalDate.of(1990, 8, 15));
        sampleCustomer2.setGender(Gender.MALE);
        sampleCustomer2.setContactNumber("0987654321");
        sampleCustomer2.setEmail("bob@example.com");
        sampleCustomer2.setAddress("456 Builder Street");
        sampleCustomer2.setUser(sampleUser2);


        
        samplePrincipal1=mock(Principal.class);
        samplePrincipal2=mock(Principal.class);
        when(samplePrincipal1.getName()).thenReturn("testuser1");
        when(samplePrincipal2.getName()).thenReturn("testuser2");
    }

   
	
//=============================================== ADD ============================================================	
	 @Test
	    public void testSignUp() throws Exception {
	        // Case 1: Successful sign-up
	        when(customerRepository.getByContactNumber("1234567890")).thenReturn(null);
	        when(customerRepository.getByEmail("alice@example.com")).thenReturn(null);
	        when(userService.addUser(sampleUser1)).thenReturn(sampleUser1);
	        when(customerRepository.save(sampleCustomer1)).thenReturn(sampleCustomer1);

	        Customer actualCustomer = customerService.signUp(sampleCustomer1);
	        assertEquals("Alice", actualCustomer.getName());

	        // Case 2: Contact number already exists
	        when(customerRepository.getByContactNumber("1234567890")).thenReturn(sampleCustomer1);
	        ResourceExistsException e1 = assertThrows(ResourceExistsException.class, () -> {
	            customerService.signUp(sampleCustomer1);
	        });
	        assertEquals("This Contact number already exists!!!", e1.getMessage());

	        // Case 3: Email already exists
	        when(customerRepository.getByContactNumber("1234567890")).thenReturn(null);
	        when(customerRepository.getByEmail("alice@example.com")).thenReturn(sampleCustomer1);
	        ResourceExistsException e2 = assertThrows(ResourceExistsException.class, () -> {
	            customerService.signUp(sampleCustomer1);
	        });
	        assertEquals("This Email already exists!!!", e2.getMessage());
	    }
	 
	 
	 
	 
	 @Test
	    public void testAddAdditionalDetails() throws Exception {
	        // Case 1: Add both aadhar and pan
	        sampleCustomer1.setAadharNumber("123412341234");
	        sampleCustomer1.setPanCardNumber("ABCPE1234F");

	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
	        when(customerRepository.getByUserId(1)).thenReturn(sampleCustomer1);
	        when(customerRepository.save(sampleCustomer1)).thenReturn(sampleCustomer1);

	        Customer updated = customerService.addAdditionalDetails(sampleCustomer1, samplePrincipal1);
	        assertEquals("123412341234", updated.getAadharNumber());
	        assertEquals("ABCPE1234F", updated.getPanCardNumber());

	        // Case 2: Only Aadhar
	        
	        sampleCustomer1.setAadharNumber("999988887777");
	        when(customerRepository.save(sampleCustomer1)).thenReturn(sampleCustomer1);

	        Customer updatedOnlyAadhar = customerService.addAdditionalDetails(sampleCustomer1, samplePrincipal1);
	        assertEquals("999988887777", updatedOnlyAadhar.getAadharNumber());

	        // Case 3: Only Pan
	        
	        sampleCustomer1.setPanCardNumber("ZXYPV5432K");
	        when(customerRepository.save(sampleCustomer1)).thenReturn(sampleCustomer1);

	        Customer updatedOnlyPan = customerService.addAdditionalDetails(sampleCustomer1, samplePrincipal1);
	        assertEquals("ZXYPV5432K", updatedOnlyPan.getPanCardNumber());

	        // Case 4: Neither Aadhar nor Pan
	        
	        when(customerRepository.save(sampleCustomer1)).thenReturn(sampleCustomer1);

	        Customer noChange = customerService.addAdditionalDetails(sampleCustomer1, samplePrincipal1);
	        assertEquals("999988887777",noChange.getAadharNumber());
	        assertEquals("ZXYPV5432K",noChange.getPanCardNumber());
	    }
	 
	 
	//========================================= get =============================================================== 
	 
	
	    @Test
	    public void testGetAllCustomer() throws Exception {
	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
	        when(customerRepository.findAll()).thenReturn(Arrays.asList(sampleCustomer1, sampleCustomer2));

	        List<Customer> allCustomers = customerService.getAllCustomer(samplePrincipal1);
	        assertEquals(2, allCustomers.size());
	    }

	    @Test
	    public void testGetCustomerById() throws Exception {
	        // Case 1: Customer found by ID
	        when(samplePrincipal1.getName()).thenReturn("testuser1");
	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
	        when(customerRepository.findById(101)).thenReturn(Optional.of(sampleCustomer1));

	        Customer actualReturnedCustomer = customerService.getCustomerById(101, samplePrincipal1);
	        assertEquals("Alice", actualReturnedCustomer.getName());

	        // Case 2: Customer not found by ID - throws ResourceNotFoundException
	        when(customerRepository.findById(999)).thenReturn(Optional.empty());

	        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
	            customerService.getCustomerById(999, samplePrincipal1);
	        });
	        assertEquals("No customer record with given id...!!!", ex.getMessage());
	    }
	    
	    
	    
	    
	    

	    @Test
	    public void testGetCurrentCustomer() throws Exception {
	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
	        when(customerRepository.getByUserId(1)).thenReturn(sampleCustomer1);
	        Customer current = customerService.getCurrentCustomer(samplePrincipal1);
	        assertEquals("Alice", current.getName());
	    }

	    @Test
	    public void testGetCustomerByUserId() throws Exception {
	        // Case 1: Customer found by userId
	        when(samplePrincipal1.getName()).thenReturn("testuser1");
	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
	        when(customerRepository.getByUserId(1)).thenReturn(sampleCustomer1);

	        Customer foundCustomer = customerService.getCustomerByUserid(1, samplePrincipal1);
	        assertEquals("Alice", foundCustomer.getName());

	        // Case 2: Customer not found by userId - throws ResourceNotFoundException
	        when(customerRepository.getByUserId(999)).thenReturn(null);

	        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
	            customerService.getCustomerByUserid(999, samplePrincipal1);
	        });
	        assertEquals("No customer record with the given user id...!!!", ex.getMessage());
	    }
	    
	    
	    
	    
//============================================= UPDATE ===========================================================

	    @Test
	    public void testUpdateCustomer() throws Exception {
	    	
	    	//Prepare customer to pass
	        Customer updateInfo = new Customer();
	        updateInfo.setName("Alice Updated");
	        updateInfo.setEmail("alice.updated@example.com");
	        updateInfo.setContactNumber("1122334455");

	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
	        when(customerRepository.getByUserId(1)).thenReturn(sampleCustomer1);
	        when(customerRepository.save(sampleCustomer1)).thenReturn(sampleCustomer1);

	        Customer actualUpdatedCustomer1 = customerService.updateCustomer(updateInfo, samplePrincipal1);
	        assertEquals("Alice Updated", actualUpdatedCustomer1.getName());
	    }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 @AfterEach
	    public void afterTest() {
	    	sampleUser1=null;
	    	sampleUser2=null;
	    	sampleCustomer1=null;
	    	sampleCustomer2=null;
		    samplePrincipal1=null;
		    samplePrincipal2=null;
	    	
	    }



}
