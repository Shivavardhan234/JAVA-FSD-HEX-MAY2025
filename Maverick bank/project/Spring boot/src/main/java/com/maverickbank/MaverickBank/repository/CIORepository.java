package com.maverickbank.MaverickBank.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.model.users.CIO;

public interface CIORepository extends JpaRepository<CIO, Integer> {

	
	@Query("SELECT c FROM CIO c WHERE c.contactNumber=?1")
	CIO getByContactNumber(String contactNumber);

	
	@Query("SELECT c FROM CIO c WHERE c.email=?1")
	CIO getByEmail(String email);

	@Query("SELECT c FROM CIO c WHERE c.user.id=?1")
	CIO getCioByUserId(int id);

	@Query("SELECT c FROM CIO c WHERE c.user.status=?1")
	List<CIO> getCioByStatus(ActiveStatus status, Pageable pageable);

}
