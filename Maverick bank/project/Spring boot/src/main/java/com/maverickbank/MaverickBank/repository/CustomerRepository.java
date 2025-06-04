package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.users.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	@Query("SELECT c FROM Customer c WHERE c.contactNumber=?1")
	Customer getByContactNumber(String contactNumber);
	
	
	@Query("SELECT c FROM Customer c WHERE c.email=?1")
	Customer getByEmail(String email);
	
	
	@Query("SELECT c FROM Customer c WHERE c.user.username=?1")
	Customer getByUsername(String username);
}
