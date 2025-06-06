package com.maverickbank.MaverickBank.repository;

import java.util.List;

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

	
	@Query("SELECT e FROM Employee e WHERE e.designation=?1")
	List<Employee> getEmployeeByDesignation(String designation);
	
	@Query("SELECT e FROM Employee e WHERE e.branch=?1")
	List<Employee> getEmployeeByBranch(String branch);
	
	
	@Query("SELECT e FROM Employee e WHERE e.user.id=?1")
	Employee getEmployeeByUserId(int userId);
}
