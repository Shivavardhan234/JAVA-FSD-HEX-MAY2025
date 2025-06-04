package com.springboot.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.lms.exception.ResourceNotFoundException;
import com.springboot.lms.model.Learner;
import com.springboot.lms.service.LearnerService;

@RestController
public class LearnerController {
	@Autowired
	LearnerService ls;
	
	@PostMapping("/api/learner/add")
	public Learner addLearner(@RequestBody  Learner learner) {
		return ls.addLearner(learner);
		
	}
	
	@GetMapping("/api/learner/get-all")
	public ResponseEntity<?> getAllLeaner() {
		return ResponseEntity.status(HttpStatus.FOUND).body(ls.getAllLearner());
		
		
	}
	
	
	@DeleteMapping("/api/learner/delete/{id}")
	public void deleteLearnerById(@PathVariable int id) {
		ls.deleteById(id);
		
	}
	
	
	
	@GetMapping("/api/learner/getById/{id}")
	public Learner getById(@PathVariable int id) throws ResourceNotFoundException {
		
		return ls.getById(id);
		
	}
	
	@PutMapping("/api/learner/updateLearner/{id}") 
	public Learner updateLearner(@PathVariable int id,@RequestBody Learner learner) throws ResourceNotFoundException  {
		
		Learner newLearner = new Learner();
		
		newLearner =ls.updateLearner(id,learner);
		
		
		
		return newLearner;
	}
	
	
	

}
