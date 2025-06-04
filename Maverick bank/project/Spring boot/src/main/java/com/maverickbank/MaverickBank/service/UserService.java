package com.maverickbank.MaverickBank.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class UserService {
	
	UserRepository ur;
	PasswordEncoder pe;
	
	
	

	public UserService(UserRepository ur, PasswordEncoder pe) {
		super();
		this.ur = ur;
		this.pe = pe;
	}




	/**  addUser()==> validates and adds user to database
	 * @param user
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceExistsException
	 * @throws Exception
	 */
	public User addUser(User user) throws InvalidInputException, ResourceExistsException, Exception {
		//validating the given user object
		UserValidation.fullUserValidation(user);
		
		
		//Checking weather this username already exists
		if(ur.getByUsername(user.getUsername())!=null) {
			throw new ResourceExistsException("Username not available..!!!");
		}
		
		//Encode the password
		String encryptedPassword = pe.encode(user.getPassword());
		
		//Set password
		user.setPassword(encryptedPassword);
		
		//Save user
		return ur.save(user);
	}




	/**Fetches all the users as a list
	 * @return
	 */
	public List<User> getAllUser() {
		return ur.findAll();
	}




	public Object getByUsername(String username) throws ResourceNotFoundException {
		User user=ur.getByUsername(username);
		if(user==null) {
			throw new ResourceNotFoundException("No user with the given username...!!!");
		}
		switch (user.getRole()) {
		case CUSTOMER:
			break;
		case ADMIN:
			break;
		case ACCOUNT_MANAGER:
		case JUNIOR_OPERATIONS_MANAGER:
		case SENIOR_OPERATIONS_MANAGER:
		case LOAN_OFFICER:
		case TRANSACTION_ANALYST:
			break;
		default:
				return null;
			
			
		}
		return null;
	}




	
}
