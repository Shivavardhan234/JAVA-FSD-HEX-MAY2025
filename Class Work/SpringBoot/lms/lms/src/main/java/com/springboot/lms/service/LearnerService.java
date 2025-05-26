package com.springboot.lms.service;

import java.util.List;
import java.util.Optional;

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
		
		/*optional is a data type where the reference variable may or may not contain any value
		 * */
		
		Optional<Learner> optionalLearner = lr.findById(id);
		if(optionalLearner.isEmpty()) {
			throw new RuntimeException("Invalid id");
		}
		
		Learner learner= optionalLearner.get();
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
