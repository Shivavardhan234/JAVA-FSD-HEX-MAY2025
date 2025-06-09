package com.hospitalManagementSystem.HospitalManagementSystem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.ResourceNotFoundException;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Patient;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.AppointmentRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.DoctorRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.repository.PatientRepository;
import com.hospitalManagementSystem.HospitalManagementSystem.service.PatientService;



@SpringBootTest
class AppointmentServiceTest {

	
	
	
	 @InjectMocks
	    private PatientService patientService;

	    @Mock
	    private AppointmentRepository appointmentRepository;
	    @Mock
	    private PatientRepository patientRepository;
	    @Mock
		private DoctorRepository doctorRepository;

	    private Patient patient1;
	    private Patient patient2;

	    @BeforeEach
	    void setUp() throws InvalidInputException {
	        patient1 = new Patient();
	        patient1.setId(1);
	        patient1.setName("John");
	        patient1.setAge(23);

	        patient2 = new Patient();
	        patient2.setId(2);
	        patient2.setName("Alice");
	        patient2.setAge(37);
	    }

	    @Test
	    void testGetAllPatientsByDoctorId_ShouldReturnPatients() throws ResourceNotFoundException {
	        // Arrange
	        List<Patient> patients = Arrays.asList(patient1, patient2);
	        Mockito.when(appointmentRepository.getAllPatientsByDoctorId(10)).thenReturn(patients);

	        // Act
	        List<Patient> result = patientService.getAllPatientsByDoctorId(10);

	        // Assert
	        Assertions.assertEquals(2, result.size());
	        Assertions.assertEquals("John", result.get(0).getName());
	        Assertions.assertEquals("Alice", result.get(1).getName());
	    }

	    @Test
	    void testGetAllPatientsByDoctorId_ShouldThrowException_WhenNoPatientsFound() {
	        // Arrange
	        Mockito.when(appointmentRepository.getAllPatientsByDoctorId(20)).thenReturn(Collections.emptyList());

	        // Act & Assert
	        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
	            patientService.getAllPatientsByDoctorId(20);
	        });
	    }

}
