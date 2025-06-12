package com.hospitalManagementSystem.HospitalManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hospitalManagementSystem.HospitalManagementSystem.enums.AppointmentStatus;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Appointment;
import com.hospitalManagementSystem.HospitalManagementSystem.model.Patient;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{

	
	
	@Query("SELECT a.patient FROM Appointment a WHERE a.doctor.id=?1")
	List<Patient> getAllPatientsByDoctorId(int doctorId);

	
	@Query("SELECT a.patient FROM Appointment a WHERE a.appointmentStatus=?1")
	List<Patient> getPatientsByStatus(AppointmentStatus status);
}
