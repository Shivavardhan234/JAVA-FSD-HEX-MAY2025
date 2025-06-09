package com.hospitalManagementSystem.HospitalManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospitalManagementSystem.HospitalManagementSystem.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

}
