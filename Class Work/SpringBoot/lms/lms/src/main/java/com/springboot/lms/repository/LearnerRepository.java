package com.springboot.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.lms.model.Learner;

public interface LearnerRepository extends JpaRepository<Learner, Integer>{
	@Query("SELECT l FROM Learner l WHERE l.user.username=?1")
	Learner getLearnerByUsername(String username);

}
