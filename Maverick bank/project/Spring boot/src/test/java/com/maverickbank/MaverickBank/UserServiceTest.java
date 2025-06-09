package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.UserService;



@SpringBootTest
class UserServiceTest {
	
	
	 @InjectMocks
	    private UserService userService; 

	    @Mock
	    private UserRepository userRepository; 

	    @Mock
	    private PasswordEncoder passwordEncoder; 
	    
	    
	    
	    
	    
	    
	    private User sampleUser1;
	    private User sampleUser2;
	    private User sampleUser3;
	    private User sampleUser4;
	    private String encodedPassword;
	    private Principal samplePrincipal1;
	    
	    @BeforeEach
	    public void init() throws InvalidInputException {
	        // First sample user
	    	
	        sampleUser1 = new User();
	        sampleUser1.setId(1);
	        sampleUser1.setUsername("testuser1");
	        sampleUser1.setPassword("Rawpassword@1");
	        sampleUser1.setRole(Role.CUSTOMER);
	        sampleUser1.setStatus(ActiveStatus.ACTIVE);
	        // Second sample user
	        sampleUser2 = new User();
	        sampleUser2.setId(2);
	        sampleUser2.setUsername("testuser2");
	        sampleUser2.setPassword("Rawpassword@2");
	        sampleUser2.setRole(Role.ADMIN);
	        sampleUser2.setStatus(ActiveStatus.ACTIVE);
	        
	        
	     // 3rd sample user
	        sampleUser3 = new User();
	        sampleUser3.setId(3);
	        sampleUser3.setUsername("testuser3");
	        sampleUser3.setPassword("Rawpassword@3");
	        sampleUser3.setRole(Role.ACCOUNT_MANAGER);
	        sampleUser3.setStatus(ActiveStatus.INACTIVE);
	        
	        
	     // 4th sample user
	        sampleUser4 = new User();
	        sampleUser4.setId(4);
	        sampleUser4.setUsername("testuser4");
	        sampleUser4.setPassword("Rawpassword@4");
	        sampleUser4.setRole(Role.JUNIOR_OPERATIONS_MANAGER);
	        sampleUser4.setStatus(ActiveStatus.DELETED);

	        
	        
	        samplePrincipal1=mock(Principal.class);
	        
	        
	        encodedPassword = "$2a$10$encryptedPassword";
	    }



	    @Test
	    public void testAddUser() throws Exception {
	        //Test case 1
	        when(userRepository.getByUsername("testuser1")).thenReturn(null);

	        
	        when(passwordEncoder.encode("Rawpassword@1")).thenReturn(encodedPassword);

	        
	        when(userRepository.save(sampleUser1)).thenReturn(sampleUser1);
	        
	        User expectedUser1 = new User();
	        expectedUser1.setId(1);
	        expectedUser1.setUsername("testuser1");
	        expectedUser1.setPassword("$2a$10$encryptedPassword");
	        expectedUser1.setRole(Role.CUSTOMER);
	        expectedUser1.setStatus(ActiveStatus.ACTIVE);
	        
	        User actualUser1 = userService.addUser(sampleUser1);

	        
	        assertNotNull(actualUser1);
	        assertEquals("testuser1", actualUser1.getUsername());
	        assertEquals(encodedPassword, actualUser1.getPassword());
	        assertEquals(Role.CUSTOMER, actualUser1.getRole());
	        assertEquals(ActiveStatus.ACTIVE, actualUser1.getStatus());
	        
	        
	        
	        //test case 2
	        when(userRepository.getByUsername("testuser2")).thenReturn(sampleUser2);

	        
	        when(passwordEncoder.encode("Rawpassword@2")).thenReturn(encodedPassword);

	        
	        when(userRepository.save(sampleUser2)).thenReturn(sampleUser2);
	        
	        
	        
	        ResourceExistsException e =assertThrows(ResourceExistsException.class, ()->{userService.addUser(sampleUser2);}); 
	        assertEquals("Username not available..!!!", e.getMessage());
	        
	        
	        
	            
	        
	    }
	    
	    
//========================================== GET ====================================================================
	    
	    
	    
	    //------------------------Testing get all user--------------------------------
	    
	    @Test
	    public void testGetAllUser() throws Exception {
	        
	        when(samplePrincipal1.getName()).thenReturn("testuser1");

	        
	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);

	        
	        when(userRepository.findAll()).thenReturn(Arrays.asList(sampleUser1, sampleUser2));

	        
	        List<User> users = userService.getAllUser(samplePrincipal1);

	        // Verify results
	        assertNotNull(users);
	        assertEquals(2, users.size());
	        assertTrue(users.contains(sampleUser1));
	        assertTrue(users.contains(sampleUser2));
	        
	        
	        
	        
	        when(samplePrincipal1.getName()).thenReturn("testuser3");

	        
	        when(userRepository.getByUsername("testuser3")).thenReturn(sampleUser3);
	        
	        InvalidActionException e1=assertThrows(InvalidActionException.class, ()->{userService.getAllUser(samplePrincipal1);});
	        assertEquals("User is NOT ACTIVE...!!!", e1.getMessage());
	        
	        
	        
	        when(samplePrincipal1.getName()).thenReturn("testuser4");

	        
	        when(userRepository.getByUsername("testuser4")).thenReturn(sampleUser4);
	        
	        DeletedUserException e2=assertThrows(DeletedUserException.class, ()->{userService.getAllUser(samplePrincipal1);});
	        assertEquals("User DON'T EXIST...!!!", e2.getMessage());
	    }

	    
	    
	    //-------------------------- get by id -------------------------	    
	    
	    @Test
	    public void testGetUserById() throws Exception {
	        // Case 1
	        when(samplePrincipal1.getName()).thenReturn("testuser1");
	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
	        when(userRepository.findById(2)).thenReturn(Optional.of(sampleUser2));

	        User result = userService.getUserById(2, samplePrincipal1);
	        assertNotNull(result);
	        assertEquals(2, result.getId());
	        assertEquals("testuser2", result.getUsername());

	        
	        
	        
	        
	        // Case 2
	        when(userRepository.findById(99)).thenReturn(Optional.empty());

	        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
	            userService.getUserById(99, samplePrincipal1);
	        });
	        assertEquals("No user record found with the given id...!!!", e.getMessage());
	    }

	    
//================================================== UPDATE ==========================================================
	    
	    
	    // Update username
	    
	    @Test
	    public void testUpdateUsername() throws Exception {
	        // Case 1
	        when(samplePrincipal1.getName()).thenReturn("testuser1");
	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
	        when(userRepository.save(sampleUser1)).thenReturn(sampleUser1);

	        String newUsername = "updatedUser1";

	        User result = userService.updateUsername(newUsername, samplePrincipal1);

	        assertNotNull(result);
	        assertEquals(newUsername, result.getUsername());

	        // Case 2
	        String invalidUsername = "ab";
	        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
	            userService.updateUsername(invalidUsername, samplePrincipal1);
	        });
	        assertEquals("Username is Invalid. Please enter appropriate Username...!!!", e.getMessage());
	    }


}
