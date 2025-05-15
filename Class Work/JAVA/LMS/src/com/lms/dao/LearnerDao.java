package com.lms.dao;

import java.util.List;
import com.lms.model.*;
import com.lms.exception.*;

public interface LearnerDao {
	void addLearner ( Learner l) throws InvalidInputException;
	
	List<Learner>  getAllLearners();
	Learner getById(int id)throws InvalidIdException;
	
	Learner updateLearner(int id , Learner l)throws InvalidInputException , InvalidIdException;
	
	void deleteLearner(int id)throws InvalidIdException;
	
	
	

}
