package com.hospitalManagementSystem.HospitalManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospitalManagementSystem.HospitalManagementSystem.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer>{

}
