package com.springboot.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.lms.model.Learner;
import com.springboot.lms.repository.LearnerRepository;


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

	public Learner getById(int id) {
		
		return lr.findById(id).orElseThrow(()-> new RuntimeException("Invalid Id"));
	}

	public Learner updateLearner(int id, Learner newLearner) {
		Learner learner = lr.findById(id).orElseThrow(()-> new RuntimeException("Invalid Id"));
		if (newLearner.getName()!=null) {
			learner.setName(newLearner.getName());
		}
		if (newLearner.getContact()!=null) {
			learner.setContact(newLearner.getContact());
		}
		lr.save(learner);
		return lr.save(learner);
	}
	
	
	
	
}
