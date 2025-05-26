package com.springboot.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.lms.model.Learner;
import com.springboot.lms.service.LearnerService;

@RestController
public class LearnerController {
	@Autowired
	LearnerService ls;
	
	@PostMapping("/api/learner/add")
	public Learner addLeraner(@RequestBody  Learner learner) {
		return ls.addLearner(learner);
		
	}
	
	@GetMapping("/api/learner/get-all")
	public List<Learner> addLeraner() {
		return ls.getAllLearner();
		
	}
	
	
	@DeleteMapping("/api/learner/delete/{id}")
	public void deleteLearnerById(@PathVariable int id) {
		ls.deleteById(id);
		
	}
	
	
	
	@GetMapping("/api/learner/getById/{id}")
	public Learner getById(@PathVariable int id) {
		
		return ls.getById(id);
		
	}
	
	@PutMapping("/api/learner/updateLearner/{id}")
	public Learner updateLearner(@PathVariable int id,@RequestBody Learner learner) {
		return ls.updateLearner(id,learner);
		
	}

}
