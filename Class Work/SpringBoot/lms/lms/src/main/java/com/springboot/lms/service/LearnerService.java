package com.springboot.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.springboot.lms.model.Learner;
import com.springboot.lms.repository.LearnerRepository;

import com.springboot.lms.exception.*;
@Service
public class LearnerService {

	
	private LearnerRepository lr;
	
	public LearnerService ( LearnerRepository lr) {
		this.lr=lr;
	}

	public Learner addLearner(Learner learner) {
		lr.save(learner);
		return learner;
	}

	public List<Learner> getAllLearner() {
		
		return lr.findAll();
	}

	public void deleteById(int id) {
		lr.deleteById(id);
	}

	public Learner getById(int id) throws ResourceNotFoundException {
		
		return lr.findById(id).orElseThrow(()-> new ResourceNotFoundException("Invalid Id"));
	}

	public Learner updateLearner(int id, Learner newearner) throws ResourceNotFoundException {
		
		
		
		
		Learner learner= lr.findById(id).orElseThrow(()-> new ResourceNotFoundException("learner with given id not found!!!") );
		
		
		lr.save(learner);
		return lr.save(learner);
	}
	
	
	
	
}
