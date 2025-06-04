package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.users.CIO;

public interface CIORepository extends JpaRepository<CIO, Integer> {

	
	@Query("SELECT c FROM CIO c WHERE c.contactNumber=?1")
	CIO getByContactNumber(String contactNumber);

	
	@Query("SELECT c FROM CIO c WHERE c.email=?1")
	CIO getByEmail(String email);

}
