package com.springboot.lms.repository;

import java.security.Principal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.lms.model.Author;
import com.springboot.lms.model.Course;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
	
	
	@Query("SELECT a FROM Author a WHERE a.user.username=?1")
	public Author getByUsername(String username);

	


}
