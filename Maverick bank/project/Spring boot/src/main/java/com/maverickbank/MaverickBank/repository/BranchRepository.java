package com.maverickbank.MaverickBank.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Integer>{
	
	@Query("SELECT b FROM Branch b WHERE b.branchName=?1")
	Branch getByName(String name);

	
	@Query("SELECT b FROM Branch b WHERE LOWER(b.address) LIKE  LOWER(CONCAT('%' ,?1,'%'))")
	List<Branch> getByState(String state, Pageable pageable);
	
	@Query("SELECT b FROM Branch b WHERE LOWER(b.email)=LOWER(?1)")
	Branch getByEmail(String email);
	
	@Query("SELECT b FROM Branch b WHERE b.contactNumber=?1")
	Branch getByContactNumber(String contactNumber);
	
	@Query("SELECT b FROM Branch b WHERE b.ifsc=?1")
	Branch getBranchByIfsc(String ifsc);

	@Query("SELECT b FROM Branch b WHERE b.status=?1")
	List<Branch> getByStatus(ActiveStatus status, Pageable pageable);
	
	@Query("SELECT b FROM Branch b WHERE b.status=?2 AND LOWER(b.address) LIKE  LOWER(CONCAT('%' ,?1,'%')) ")
	List<Branch> getByStateAndStatus(String state,ActiveStatus status, Pageable pageable);


	
	

}
