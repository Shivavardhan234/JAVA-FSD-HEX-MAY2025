package com.hospitalManagementSystem.HospitalManagementSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.hospitalManagementSystem.HospitalManagementSystem.enums.Role;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.ResourceNotFoundException;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Patient;
import com.hospitalManagementSystem.HospitalManagementSystem.model.User;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.AppointmentRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.DoctorRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.PatientRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.service.AppointmentService;


@SpringBootTest
class AppointmentServiceTest {

	
	
	
	 @InjectMocks
	    private AppointmentService appointmentService;

	    @Mock
	    private AppointmentRepository appointmentRepository;
	    @Mock
	    private PatientRepository patientRepository;
	    @Mock
		private DoctorRepository doctorRepository;

	    private Patient patient1;
	    private User user1;
	    private Patient patient2;
	    private User user2;
	    
	    
	    

	    @BeforeEach
	    void setUp() throws InvalidInputException {
	    	
	    	
	    	
	    	 user1 = new User();
	         user1.setId(1);
	         user1.setUsername("testuser1");
	         user1.setPassword("Rawpassword@1");
	         user1.setRole(Role.PATIENT);

	         user2 = new User();
	         user2.setId(2);
	         user2.setUsername("testuser2");
	         user2.setPassword("Rawpassword@2");
	         user2.setRole(Role.PATIENT);
	         
	         
	        patient1 = new Patient();
	        patient1.setId(1);
	        patient1.setName("John");
	        patient1.setAge(23);
	        patient1.setUser(user1);

	        patient2 = new Patient();
	        patient2.setId(2);
	        patient2.setName("Alice");
	        patient2.setAge(37);
	        patient2.setUser(user2);
	    }

	    @Test
	    void testGetAllPatientsByDoctorId() throws ResourceNotFoundException {
	        
//	        List<Patient> patientsForThatDoctor = Arrays.asList(patient1, patient2);
//	        when(appointmentRepository.getAllPatientsByDoctorId(1)).thenReturn(patientsForThatDoctor);
//
//	        
//	        
//	        List<Patient> expectedOutput = Arrays.asList(patient1, patient2);
//	        
//	        assertEquals(expectedOutput, appointmentService.getAllPatientsByDoctorId(1));
//	        
	        
	        
	        List<Patient> emptyList=new ArrayList<>();
	        when(appointmentRepository.getAllPatientsByDoctorId(20)).thenReturn(emptyList);

	       ResourceNotFoundException e= assertThrows(ResourceNotFoundException.class, ()->{appointmentService.getAllPatientsByDoctorId(20);});
	       assertEquals("No patient records with the given doctor id...!!!", e.getMessage());
	    }

	   

}
