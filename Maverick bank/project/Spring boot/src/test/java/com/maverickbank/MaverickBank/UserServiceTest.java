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

import org.junit.jupiter.api.AfterEach;
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
import com.maverickbank.MaverickBank.exception.InvalidCredentialsException;
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
	    private String encodedPassword1;
	    private Principal samplePrincipal1;
	    private Principal samplePrincipal2;
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
	        samplePrincipal2=mock(Principal.class);
	        
	        encodedPassword1 = "$2a$10$encryptedPassword1";
	    }



	    @Test
	    public void testAddUser() throws Exception {
	        //Test case 1
	        when(userRepository.getByUsername("testuser1")).thenReturn(null);

	        
	        when(passwordEncoder.encode("Rawpassword@1")).thenReturn(encodedPassword1);

	        
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
	        assertEquals(encodedPassword1, actualUser1.getPassword());
	        assertEquals(Role.CUSTOMER, actualUser1.getRole());
	        assertEquals(ActiveStatus.ACTIVE, actualUser1.getStatus());
	        
	        
	        
	        //test case 2
	        when(userRepository.getByUsername("testuser2")).thenReturn(sampleUser2);

	        
	        when(passwordEncoder.encode("Rawpassword@2")).thenReturn(encodedPassword1);

	        
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

	        User actualUpdatedUser = userService.updateUsername(newUsername, samplePrincipal1);

	        assertNotNull(actualUpdatedUser);
	        assertEquals(newUsername, actualUpdatedUser.getUsername());

	        // Case 2
	        String invalidUsername = "ab";
	        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
	            userService.updateUsername(invalidUsername, samplePrincipal1);
	        });
	        assertEquals("Username is Invalid. Please enter appropriate Username...!!!", e.getMessage());
	    }
	    
	    
	    
	    
	    //------------------------------- update password ----------------------------------
	    @Test
	    public void testUpdatePassword() throws Exception {
	    	//case1
	        when(samplePrincipal1.getName()).thenReturn("testuser1");
	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
	        when(passwordEncoder.matches("Rawpassword@1","Rawpassword@1")).thenReturn(true);
	        when(passwordEncoder.encode("NewPassword@1")).thenReturn("$2a$10$newEncodedPassword1");
	        when(userRepository.save(sampleUser1)).thenReturn(sampleUser1);

	        User actualUpdatedUser = userService.updatePassword("Rawpassword@1", "NewPassword@1", samplePrincipal1);
	        assertEquals("$2a$10$newEncodedPassword1", actualUpdatedUser.getPassword());

	        //case 2
	        when(passwordEncoder.matches("WrongOld@1","Rawpassword@1")).thenReturn(false);
	        InvalidCredentialsException e1 = assertThrows(InvalidCredentialsException.class, () -> {
	            userService.updatePassword("WrongOld@1", "NewPassword@1", samplePrincipal1);
	        });
	        assertEquals("Incorrect password...!!!", e1.getMessage());
	        
	        //case 3
	        when(passwordEncoder.matches("NewPassword@1",sampleUser1.getPassword())).thenReturn(true);
	        InvalidCredentialsException e2 = assertThrows(InvalidCredentialsException.class, () -> {
	            userService.updatePassword("NewPassword@1", "NewPassword@1", samplePrincipal1);
	        });
	        assertEquals("New password cannot be same as old password...!!!", e2.getMessage());
	    }
	    
	  //---------------------------------- Update role --------------------------------------
	    @Test
	    public void testUpdateUserRole() throws Exception {
	        // Case 1
	        when(samplePrincipal1.getName()).thenReturn("testuser1");
	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);

	       
	        when(userRepository.getById(3)).thenReturn(sampleUser3);
	        when(userRepository.save(sampleUser3)).thenReturn(sampleUser3);

	        User updatedUser = userService.updateUserRole(3, Role.JUNIOR_OPERATIONS_MANAGER, samplePrincipal1);
	        assertEquals(Role.JUNIOR_OPERATIONS_MANAGER, updatedUser.getRole());
	        
	        
	        
	        

	        // Case 2
	        InvalidInputException e1 = assertThrows(InvalidInputException.class, () -> {
	            userService.updateUserRole(3, Role.CUSTOMER, samplePrincipal1);
	        });
	        assertEquals("Given new role is invalid. Cannot perform update role action...!!!", e1.getMessage());

	        // Case 3
	        when(userRepository.getById(99)).thenReturn(null);
	        ResourceNotFoundException e2 = assertThrows(ResourceNotFoundException.class, () -> {
	            userService.updateUserRole(99, Role.ACCOUNT_MANAGER, samplePrincipal1);
	        });
	        assertEquals(" No user record with given user id...!!!", e2.getMessage());

	        // Case 4
	        when(userRepository.getById(1)).thenReturn(sampleUser1); 
	        InvalidActionException e3 = assertThrows(InvalidActionException.class, () -> {
	            userService.updateUserRole(1, Role.SENIOR_OPERATIONS_MANAGER, samplePrincipal1);
	        });
	        assertEquals("Invalid action.User role cannot be changed...!!!", e3.getMessage());
	    }
	    
	    
	    
	    
	    //----------------------------------------- deactivate user account -------------------------------------------
	    @Test
	    public void testDeactivateUserAccount() throws Exception {
	        // Case 1
	        when(samplePrincipal1.getName()).thenReturn("testuser1");
	        when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
	        when(passwordEncoder.matches("Rawpassword@1", "Rawpassword@1")).thenReturn(true);

	        
	        
	        when(userRepository.save(sampleUser1)).thenReturn(sampleUser1);

	        User updatedUser = userService.deactivateUserAccount("Rawpassword@1", samplePrincipal1);
	        assertEquals(ActiveStatus.INACTIVE, updatedUser.getStatus());

	        // Case 2
	        when(samplePrincipal2.getName()).thenReturn("testuser2");
	        when(userRepository.getByUsername("testuser2")).thenReturn(sampleUser2);
	        when(passwordEncoder.matches("WrongPassword@2", "Rawpassword@2")).thenReturn(false);
	        InvalidCredentialsException e1 = assertThrows(InvalidCredentialsException.class, () -> {
	            userService.deactivateUserAccount("WrongPassword@2", samplePrincipal2);
	        });
	        assertEquals("Incorrect password...!!!", e1.getMessage());

	        // Case 3
	        sampleUser4.setStatus(ActiveStatus.DELETED);
	        when(samplePrincipal1.getName()).thenReturn("testuser4");
	        when(userRepository.getByUsername("testuser4")).thenReturn(sampleUser4);

	        DeletedUserException e2 = assertThrows(DeletedUserException.class, () -> {
	            userService.deactivateUserAccount("Rawpassword@4", samplePrincipal1);
	        });
	        assertEquals("User DON'T EXIST...!!!", e2.getMessage());
	    }
	    
	    
	    
	    //------------------------------------- Activete account-------------------------------------------

	    
	    @Test
		public void testActivateUser() throws Exception {
		    sampleUser3.setStatus(ActiveStatus.INACTIVE); 
		    when(samplePrincipal1.getName()).thenReturn("testuser3");
		    when(userRepository.getByUsername("testuser3")).thenReturn(sampleUser3);
		    when(userRepository.save(sampleUser3)).thenReturn(sampleUser3);

		    //case 1
		    User activatedUser = userService.activateUser(samplePrincipal1);
		    assertEquals(ActiveStatus.ACTIVE, activatedUser.getStatus());

		    // Case 2:Suspended case
		    sampleUser3.setStatus(ActiveStatus.SUSPENDED);
		    when(userRepository.getByUsername("testuser3")).thenReturn(sampleUser3);
		    assertThrows(InvalidActionException.class, () ->
		        userService.activateUser(samplePrincipal1)
		    );

		    // Case 3: Deleted case
		    sampleUser3.setStatus(ActiveStatus.DELETED);
		    when(userRepository.getByUsername("testuser3")).thenReturn(sampleUser3);
		    assertThrows(DeletedUserException.class, () ->
		        userService.activateUser(samplePrincipal1)
		    );
		}

		//------------------------------- update status ---------------------------------------------------
		
		@Test
		public void testUpdateUserActiveStatus() throws Exception {
		    when(samplePrincipal1.getName()).thenReturn("testuser2");
		    when(userRepository.getByUsername("testuser2")).thenReturn(sampleUser2); 
		    when(userRepository.getById(1)).thenReturn(sampleUser1); 
		    when(userRepository.save(sampleUser1)).thenReturn(sampleUser1);

		    User updatedUser = userService.updateUserActiveStatus(1, ActiveStatus.SUSPENDED, samplePrincipal1);
		    assertEquals(ActiveStatus.SUSPENDED, updatedUser.getStatus());

		    // Deleted user
		    sampleUser4.setStatus(ActiveStatus.DELETED);
		    when(userRepository.getById(4)).thenReturn(sampleUser4);
		    assertThrows(InvalidActionException.class, () ->
		        userService.updateUserActiveStatus(4, ActiveStatus.ACTIVE, samplePrincipal1)
		    );

		    // Invalid ID
		    when(userRepository.getById(999)).thenReturn(null);
		    assertThrows(InvalidInputException.class, () ->
		        userService.updateUserActiveStatus(999, ActiveStatus.ACTIVE, samplePrincipal1)
		    );
		}

		
		
		
		
		//----------------------------------- delete ----------------------------------------------------------
		
		
		
		@Test
		public void testDeleteUserAccount() throws Exception {
		    when(samplePrincipal1.getName()).thenReturn("testuser1");
		    when(userRepository.getByUsername("testuser1")).thenReturn(sampleUser1);
		    when(passwordEncoder.matches("Rawpassword@1", "Rawpassword@1")).thenReturn(true);
		    when(userRepository.save(sampleUser1)).thenReturn(sampleUser1);

		    User deletedUser = userService.deleteUserAccount("Rawpassword@1", samplePrincipal1);
		    assertEquals(ActiveStatus.DELETED, deletedUser.getStatus());

		    // Invalid password
		    when(samplePrincipal2.getName()).thenReturn("testuser2");
		    when(userRepository.getByUsername("testuser2")).thenReturn(sampleUser2);
		    when(passwordEncoder.matches("WrongPassword@2", "Rawpassword@2")).thenReturn(false);
		    assertThrows(InvalidCredentialsException.class, () ->
		        userService.deleteUserAccount("WrongPassword@2", samplePrincipal2)
		    );
		}
	    
	    
	    
	    
	    
	    
	    @AfterEach
	    public void afterTest() {
	    	sampleUser1=null;
	    	sampleUser2=null;
	    	sampleUser3=null;
	    	sampleUser4=null;
		    encodedPassword1=null;
		    samplePrincipal1=null;
	    	
	    }
	    


}
