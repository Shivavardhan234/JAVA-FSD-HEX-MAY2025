package com.springboot.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.lms.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
	
	
	@Query("SELECT a FROM Author a WHERE a.user.username=?1")
	public Author getByUsername(String username);

}
