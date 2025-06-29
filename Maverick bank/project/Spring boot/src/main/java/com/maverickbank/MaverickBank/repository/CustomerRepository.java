package com.maverickbank.MaverickBank.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.model.users.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c WHERE c.contactNumber=?1")
	Customer getByContactNumber(String contactNumber);
	
	
	@Query("SELECT c FROM Customer c WHERE c.email=?1")
	Customer getByEmail(String email);
	
	
	@Query("SELECT c FROM Customer c WHERE c.user.username=?1")
	Customer getByUsername(String username);
	
	@Query("SELECT c FROM Customer c WHERE c.user.id=?1")
	Customer getByUserId(int id);

	@Query("SELECT c FROM Customer c WHERE c.user.status=?1")
	List<Customer> getCustomerByStatus(ActiveStatus status, Pageable pageable);
}
