package com.lms.service;
import com.lms.model.*;
import java.util.*;
import com.lms.dao.*;
import com.lms.exception.InvalidIdException;
import com.lms.exception.InvalidInputException;
public class LearnerService {
	
	
	public List<Learner> getAllLearners(){
		LearnerDao l= new LearnerDaoImpl();
		return l.getAllLearners();
		
	}
	
	
	public Learner getById(int id) throws InvalidIdException{
		LearnerDao l= new LearnerDaoImpl();
		return l.getById(id);
		
	}
	public void deleteById(int id) throws InvalidIdException{
		LearnerDao l= new LearnerDaoImpl();
		l.deleteLearner(id);
		return ;
		
	}
	public void updateLearner(int id,Learner l) throws InvalidIdException, InvalidInputException{
		LearnerDao dao= new LearnerDaoImpl();
		dao.updateLearner(id, l);
		return ;
		
	}
	public void addLearner(String name, String email) throws  InvalidInputException{
		LearnerDao dao= new LearnerDaoImpl();
		Learner l=new Learner();
		l.setName(name);
		l.setEmail(email);
		
		dao.addLearner(l);
		return ;
		
	}


}
