package com.hospitalManagementSystem.HospitalManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hospitalManagementSystem.HospitalManagementSystem.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	
	@Query("SELECT u FROM User WHERE u.username=?1")
	User findByUsername(String username);

}
