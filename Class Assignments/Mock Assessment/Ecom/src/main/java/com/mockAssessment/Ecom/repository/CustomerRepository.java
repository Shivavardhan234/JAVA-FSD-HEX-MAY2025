package com.mockAssessment.Ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mockAssessment.Ecom.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	
	
	@Query("SELECT c FROM Customer c WHERE c.user.username=?1")
	public Customer getCustomerByUsername(String username);

}
