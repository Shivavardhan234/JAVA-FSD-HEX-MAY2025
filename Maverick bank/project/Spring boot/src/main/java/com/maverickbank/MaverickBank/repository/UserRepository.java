package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	
	@Query("SELECT u FROM User u WHERE u.username=?1")
	User getByUsername(String username);

	
}
