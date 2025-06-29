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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.CIO;
import com.maverickbank.MaverickBank.repository.CIORepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.CIOService;
import com.maverickbank.MaverickBank.service.UserService;

@SpringBootTest
class CIOServiceTest {

    @InjectMocks
    private CIOService cioService;

    @Mock
    private CIORepository cioRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    // Sample data
    private User sampleUserCio;
    private CIO sampleCio1;
    private CIO sampleCio2;
    private Principal samplePrincipalCio;
    private Principal samplePrincipalAdmin;

    @BeforeEach
    public void init() throws InvalidInputException {
        sampleUserCio = new User();
        sampleUserCio.setId(1);
        sampleUserCio.setUsername("cio_user");
        sampleUserCio.setPassword("CioPassword@123");
        sampleUserCio.setRole(Role.ADMIN);
        sampleUserCio.setStatus(ActiveStatus.ACTIVE);

        

        sampleCio1 = new CIO();
        sampleCio1.setId(101);
        sampleCio1.setName("Arun Kumar");
        sampleCio1.setDob(LocalDate.of(1980, 2, 10));
        sampleCio1.setGender(Gender.MALE);
        sampleCio1.setContactNumber("9876543210");
        sampleCio1.setEmail("arun.kumar@cio.com");
        sampleCio1.setAddress("CIO Office Block A");
        sampleCio1.setUser(sampleUserCio);

        sampleCio2 = new CIO();
        sampleCio2.setId(102);
        sampleCio2.setName("Priya Sharma");
        sampleCio2.setDob(LocalDate.of(1985, 6, 5));
        sampleCio2.setGender(Gender.FEMALE);
        sampleCio2.setContactNumber("8765432109");
        sampleCio2.setEmail("priya.sharma@cio.com");
        sampleCio2.setAddress("CIO Office Block B");

        samplePrincipalCio = mock(Principal.class);
        when(samplePrincipalCio.getName()).thenReturn("cio_user");

        samplePrincipalAdmin = mock(Principal.class);
        when(samplePrincipalAdmin.getName()).thenReturn("admin_user");
    }
    
    
 //====================================== POST ===============================================
    @Test
    public void testAddAdmin() throws Exception {
        // Case 1: Success
        when(cioRepository.getByContactNumber(sampleCio1.getContactNumber())).thenReturn(null);
        when(cioRepository.getByEmail(sampleCio1.getEmail())).thenReturn(null);
        when(userService.addUser(sampleUserCio)).thenReturn(sampleUserCio);
        when(cioRepository.save(sampleCio1)).thenReturn(sampleCio1);

        CIO addedCio = cioService.addAdmin(sampleCio1);

        assertEquals(101, addedCio.getId());
        assertEquals("Arun Kumar", addedCio.getName());
        assertEquals(sampleUserCio, addedCio.getUser());
        assertEquals(Role.ADMIN, addedCio.getUser().getRole());

        // Case 2: Contact number already exists
        when(cioRepository.getByContactNumber(sampleCio1.getContactNumber())).thenReturn(sampleCio1);

        ResourceExistsException e1 = assertThrows(ResourceExistsException.class, () -> {
            cioService.addAdmin(sampleCio1);
        });
        assertEquals("This Contact number already exists!!!", e1.getMessage());

        // Case 3: Email already exists
        when(cioRepository.getByContactNumber(sampleCio1.getContactNumber())).thenReturn(null);
        when(cioRepository.getByEmail(sampleCio1.getEmail())).thenReturn(sampleCio1);

        ResourceExistsException e2 = assertThrows(ResourceExistsException.class, () -> {
            cioService.addAdmin(sampleCio1);
        });
        assertEquals("This Email already exists!!!", e2.getMessage());

       
    }

  //========================================== GET ===========================================
    @Test
    public void testGetAllCIO() throws Exception {
        // Case 1: Success – list of CIOs
        when(userRepository.getByUsername("cio_user")).thenReturn(sampleUserCio);
        Page<CIO> mockPage= new PageImpl<>(Arrays.asList(sampleCio1));
        when(cioRepository.findAll(PageRequest.of(0,10))).thenReturn(mockPage);

        List<CIO> result = cioService.getAllCIO(0,10,samplePrincipalCio);

        assertEquals(Arrays.asList(sampleCio1), result);

        
    }
    
    
    
    
    @Test
    public void testGetById() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("cio_user")).thenReturn(sampleUserCio);
        when(cioRepository.findById(101)).thenReturn(Optional.of(sampleCio1));

        CIO result = cioService.getById(101, samplePrincipalCio);

        assertEquals(sampleCio1, result);

        // Case 2: CIO not found
        when(userRepository.getByUsername("cio_user")).thenReturn(sampleUserCio);
        when(cioRepository.findById(202)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            cioService.getById(202, samplePrincipalCio);
        });

        assertEquals("No CIO record with given id...!!!", e.getMessage());
    }
    
    @Test
    public void testGetByUserId() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("cio_user")).thenReturn(sampleUserCio);
        when(cioRepository.getCioByUserId(1)).thenReturn(sampleCio1);

        CIO result = cioService.getByUserId(1, samplePrincipalCio);

        assertEquals(sampleCio1, result);

        // Case 2: CIO not found
        when(userRepository.getByUsername("cio_user")).thenReturn(sampleUserCio);
        when(cioRepository.getCioByUserId(99)).thenReturn(null);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            cioService.getByUserId(99, samplePrincipalCio);
        });

        assertEquals("No CIO record with the given user id...!!!", e.getMessage());
    }
    
    @Test
    public void testGetByStatus() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("cio_user")).thenReturn(sampleUserCio);
        when(cioRepository.getCioByStatus(ActiveStatus.ACTIVE, PageRequest.of(0, 10))).thenReturn(Arrays.asList(sampleCio1));

        List<CIO> result = cioService.getByStatus(0,10,ActiveStatus.ACTIVE, samplePrincipalCio);

        assertEquals(Arrays.asList(sampleCio1), result);

        // Case 2: Empty or null list
        when(userRepository.getByUsername("cio_user")).thenReturn(sampleUserCio);
        when(cioRepository.getCioByStatus(ActiveStatus.SUSPENDED, PageRequest.of(0, 10))).thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            cioService.getByStatus(0,10,ActiveStatus.SUSPENDED, samplePrincipalCio);
        });

        assertEquals("No cio records with the given active status...!!!", e.getMessage());
    }


    //=================================== UPDATE ===========================================
    @Test
    public void testUpdateCIO() throws Exception {
        // Case 1: Success – all fields updated
        when(userRepository.getByUsername("cio_user")).thenReturn(sampleUserCio);
        when(cioRepository.getCioByUserId(1)).thenReturn(sampleCio1);
        when(cioRepository.save(sampleCio1)).thenReturn(sampleCio1);

        CIO updatedInput = new CIO();
        updatedInput.setName("Updated Name");
        updatedInput.setDob(LocalDate.of(1982, 5, 20));
        updatedInput.setGender(Gender.MALE);
       updatedInput.setEmail("updated.email@cio.com");
        updatedInput.setContactNumber("9998887776");
        updatedInput.setAddress("Updated Address Block Z");

        CIO result = cioService.updateCIO(updatedInput, samplePrincipalCio);
        

        
        assertEquals(updatedInput.getName(), result.getName());
        assertEquals(updatedInput.getDob(), result.getDob());
        assertEquals(updatedInput.getGender(), result.getGender());
        assertEquals(updatedInput.getEmail(), result.getEmail());
        assertEquals(updatedInput.getContactNumber(), result.getContactNumber());
        assertEquals(updatedInput.getAddress(), result.getAddress());

        
    }


    
    
    

    @AfterEach
    public void afterTest() {
        sampleUserCio = null;
        sampleCio1 = null;
        sampleCio2 = null;
        samplePrincipalCio = null;
        samplePrincipalAdmin = null;
    }

}
