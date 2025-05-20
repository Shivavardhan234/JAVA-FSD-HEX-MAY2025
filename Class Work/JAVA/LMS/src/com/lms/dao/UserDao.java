package com.lms.dao;

import java.util.List;

import com.lms.exception.InvalidIdException;
import com.lms.exception.InvalidInputException;
import com.lms.model.User;

public interface UserDao {
	int addUser(User user)throws  InvalidInputException;
	
	List<User> getAllUser();
	User getUserById(int id)throws InvalidIdException;
	
	User updateUser(int id, User user)throws InvalidIdException, InvalidInputException;
	
	void deleteUser(int id) throws InvalidIdException;
	
	

}
