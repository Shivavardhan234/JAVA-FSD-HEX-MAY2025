package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidCredentialsException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.EmployeeValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

/**
 * 
 */
@Service
public class UserService {
	
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	
	
	

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}


//---------------------------------- ADD ----------------------------------------------------------

	/**  addUser()==> validates and adds user to database
	 * @param user
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceExistsException
	 * @throws Exception
	 */
	public User addUser(User user) throws InvalidInputException, ResourceExistsException{
		//validating the given user object
		UserValidation.validateUser(user);
		
		
		//Checking weather this username already exists
		if(userRepository.getByUsername(user.getUsername())!=null) {
			throw new ResourceExistsException("Username not available..!!!");
		}
		
		//Encode the password
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		
		//Set password
		user.setPassword(encryptedPassword);
		
		//Set status
		user.setStatus(ActiveStatus.ACTIVE);
		
		//Save user
		return userRepository.save(user);
	}


//-------------------------------- GET ------------------------------------------------------------

	/**Fetches all the users as a list
	 * @param principal 
	 * @return
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public List<User> getAllUser(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		return userRepository.findAll();
	}


	
	
	
	/**Fetches User by its id.
	 * @param id
	 * @param principal 
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException , Exception 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public User getUserById(int id, Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException , Exception {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No user record found with the given id...!!!"));
	}
	
	
	
	
	
	

	public Object getByUsername(Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		
		
		switch (currentUser.getRole()) {
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



	
	//------------------------------------- Update -------------------------------------------------

	public User updateUsername(String username, Principal principal) throws InvalidInputException , Exception {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		//validate username
		UserValidation.validateUsername(username);
		
		//set new username to current user
		currentUser.setUsername(username);
		
		//save current user
		return userRepository.save(currentUser);
	}




	
	
	/**AIM : to update the user password
	 * Step 1:validate old & new passwords 
	 * Step 2:Extract current username from principal
	 * Step 3:Get user from database using current username
	 * Step 4:check whether old password matches original password, if no throw InvalidCredentialsException
	 * Step 5:check new password is same as previous password, if yes throw InvalidCredentialsException
	 * Step 6:encode the new password
	 * Step 7:set the new encoded password to current user
	 * Step 8: Save the user
	 * @param oldPassword
	 * @param newPassword
	 * @param principal
	 * @return
	 * @throws InvalidInputException 
	 * @throws InvalidCredentialsException 
	 */
	public User updatePassword(String oldPassword, String newPassword, Principal principal) throws InvalidInputException, InvalidCredentialsException, Exception {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		//Validate the passwords
		UserValidation.validatePassword(newPassword);
		UserValidation.validatePassword(oldPassword);
		
		
		//check whether old password matches original password
		if(!passwordEncoder.matches(oldPassword, currentUser.getPassword())){
			throw new InvalidCredentialsException("Incorrect password...!!!");
			
		}
		
		//Check whether old password is same as new password
		if(oldPassword.equals(newPassword)) {
			throw new InvalidCredentialsException("New password cannot be same as old password...!!!");
		}
		
		//Encode the password
		String newEncodedPassword = passwordEncoder.encode(newPassword);
		
		//Set new encoded password to current user
		currentUser.setPassword(newEncodedPassword);
		
		//Save user
		return userRepository.save(currentUser);
	}




	
	
	/**Update the role of given user
	 * @param id
	 * @param newRole
	 * @return
	 * @throws InvalidInputException 
	 * @throws InvalidActionException 
	 */
	public User updateUserRole(int id, Role newRole, Principal principal) throws InvalidInputException, InvalidActionException,ResourceNotFoundException, Exception {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		
		//Validate user role
		UserValidation.validateRole(newRole);
		//Check whether the given role is employee role or not
		try {
			EmployeeValidation.validateEmployeeRole(newRole);
		}
		catch(InvalidInputException e) {
			throw new InvalidInputException("Given new role is invalid. Cannot perform update role action...!!!");
		}
		//Fetch the user from database
		User user=userRepository.getById(id);
		if(user==null) {
			throw new ResourceNotFoundException(" No user record with given user id...!!!");
		}
		
		//Validate the original role that it belongs to employee role or not
		try {
			EmployeeValidation.validateEmployeeRole(user.getRole());
		}
		catch(InvalidInputException e) {
			throw new InvalidActionException("Invalid action.User role cannot be changed...!!!");
		}
		
		
		//Set the new role to user
		user.setRole(newRole);
		//Save user
		return userRepository.save(user);
	}




	
	/**AIM : to deactivate user account
	 * INPUT : password and principal (authenticated user)
	 * 
	 * Step 1: Validate password
	 * Step 2: Extract username from principal
	 * Step 3: Get user from the database using extracted username
	 * Step 4: If status is DEACTIVATED or DELETED throw illegalAction Exception
	 * Step 5: Else set status to INACTIVE
	 * Step 7: Save the user
	 * 
	 * RETURN : Updated user object
	 * @param password
	 * @param principal
	 * @return
	 * @throws InvalidInputException 
	 * @throws InvalidActionException 
	 * @throws InvalidCredentialsException 
	 */
	public User deactivateUserAccount(String password, Principal principal) throws InvalidInputException, InvalidActionException, InvalidCredentialsException, Exception {
		//Extract user and username
			String currentUsername= principal.getName();
			User currentUser=userRepository.getByUsername(currentUsername);
		//Check user Active status
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		
		//Validate password
		if(!passwordEncoder.matches(password, currentUser.getPassword())) {
			throw new InvalidCredentialsException("Incorrect password...!!!");
			
		}
		//Set status to Inactive
		currentUser.setStatus(ActiveStatus.INACTIVE);
		//Save user
		return userRepository.save(currentUser);
	}




	/**AIM : to activate user account
	 * INPUT : principal (authenticated user)
	 * 
	 * Step 1: Validate password
	 * Step 2: Extract username from principal
	 * Step 3: Get user from the database using extracted username
	 * Step 4: If status is DEACTIVATED or DELETED throw illegalAction Exception
	 * Step 5: Else set status to INACTIVE
	 * Step 7: Save the user
	 * 
	 * RETURN : Updated user object
	 * @param principal
	 * @return
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public User activateUser(Principal principal) throws InvalidActionException,DeletedUserException, InvalidInputException, Exception {
		//Extract user and username
		String currentUsername= principal.getName();
		User currentUser=userRepository.getByUsername(currentUsername);
		//Check whether account is deactivated
		if(currentUser.getStatus()==ActiveStatus.SUSPENDED) {
			throw new InvalidActionException("This user account is suspended. Can only be activated by Administrator...!!!");
		}
		//Check whether account is deleted
		if(currentUser.getStatus()==ActiveStatus.DELETED) {
			throw new DeletedUserException("User DON'T EXIST...!!!");
		}
		
		//Set activity status
		currentUser.setStatus(ActiveStatus.ACTIVE);
		
		//Save user
		return userRepository.save(currentUser);
	}




	/**AIM : To update the active status of the user
	 * 
	 * Step 1: Validate the current user is active
	 * Step 2: Get user by id
	 * Step 3: Set status
	 * Step 4: Save updated user
	 * @param id
	 * @param status
	 * @param principal
	 * @return
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public User updateUserActiveStatus(int id, ActiveStatus status, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		User user= userRepository.getById(id);
		if(user==null) {
			throw new InvalidInputException("No user with given UserId...!!!");
		}
		if(user.getStatus()==ActiveStatus.DELETED) {
			throw new InvalidActionException("This user account's activity status cannot be changed...!!!");
		}
		
		user.setStatus(status);
		return userRepository.save(user);
	}


	
//-------------------------------UPDATE but Marking as DELETE -------------------------------------
	
	/**In this method we are not actually deleting the record rather we are marking its status to DELETED
	 * once deleted cannot be made active
	 * @param password
	 * @param principal
	 * @return
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 * @throws InvalidCredentialsException 
	 */
	public User deleteUserAccount(String password, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, InvalidCredentialsException {
		
		User currentUser=userRepository.getByUsername(principal.getName());	
		//Check user Active status
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		
		//Validate password
		if(!passwordEncoder.matches(password, currentUser.getPassword())) {
			throw new InvalidCredentialsException("Incorrect password...!!!");
			
		}
		
		//Set status to Inactive
		currentUser.setStatus(ActiveStatus.DELETED);
		//Save user
		return userRepository.save(currentUser);
		
	}




	
	


	




	




	
}
