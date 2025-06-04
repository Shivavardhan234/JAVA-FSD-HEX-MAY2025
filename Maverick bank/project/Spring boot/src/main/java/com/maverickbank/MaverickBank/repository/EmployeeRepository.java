package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.users.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

	@Query("SELECT e FROM Employee e WHERE e.contactNumber=?1")
	Employee getByContactNumber(String contactNumber);

	
	@Query("SELECT e FROM Employee e WHERE e.email=?1")
	Employee getByEmail(String email);

	@Query("SELECT e FROM Employee e WHERE e.user.username=?1")
	Employee getEmployeeByUsername(String username);

}
