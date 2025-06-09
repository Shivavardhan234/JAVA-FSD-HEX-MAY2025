package com.hospitalManagementSystem.HospitalManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hospitalManagementSystem.HospitalManagementSystem.model.MedicalHistory;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Integer> {

	
	
	@Query("SELECT m FROM MedicalHistory m WHERE m.patient.id=?1")
	List<MedicalHistory> getMedicalHistoryByPatientId(int patientId);

}
