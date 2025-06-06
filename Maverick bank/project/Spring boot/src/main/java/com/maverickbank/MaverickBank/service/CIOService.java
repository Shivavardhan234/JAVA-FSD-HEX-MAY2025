package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.CIO;
import com.maverickbank.MaverickBank.repository.CIORepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.ActorValidation;
import com.maverickbank.MaverickBank.validation.CIOValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class CIOService {
	
	CIORepository cioRepository;
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	UserService userService;
	

	public CIOService(CIORepository cioRepository, UserRepository userRepository, PasswordEncoder passwordEncoder,UserService userService) {
		this.cioRepository = cioRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}


//-------------------------------------- POST ---------------------------------------------------------------------------
	/** addAdmin() method validates and adds the admin to the database
	 * Step 1: Validate CIO using validation classes 
	 * Step 2: As contact number and email are unique check weather they exists, if exists throw resource exists exception
	 * Step 3: Extract user from CIO object and set role
	 * Step 4: set status to active as we are creating it now
	 * Step 5: save the user and set the returned user object to CIO object
	 * Step 6: Save CIO in the db
	 * @param cio
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceExistsException
	 * @throws Exception
	 */
	public CIO addAdmin(CIO cio) throws InvalidInputException, ResourceExistsException {
		
		//Validating CIO object
		CIOValidation.validateCIO(cio);
		
		//Checking whether email and contact number already exists in db
		if(cioRepository.getByContactNumber(cio.getContactNumber())!=null){
			throw new ResourceExistsException("This Contact number already exists!!!");	
		}
		
		if(cioRepository.getByEmail(cio.getEmail())!=null){
			throw new ResourceExistsException("This Email already exists!!!");	
		}
		
		//Extract user
		User user=cio.getUser();
		
		//set remaining properties
		user.setRole(Role.ADMIN);
		//Set saved user object with user id
		cio.setUser(userService.addUser(user));
		//Save CIO object
		return cioRepository.save(cio);
		
	}

	
	
//------------------------------------------------ GET -------------------------------------------------------------------

	/**Fetches all the CIO's
	 * @return
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public List<CIO> getAllCIO(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Check user is active or not
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		//Return the list
		return cioRepository.findAll();
	}


	public CIO getById(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		//Return fetched CIO object
		return cioRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No CIO record with given id...!!!"));
	}


	public CIO getByUserId(int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		CIO cio=cioRepository.getCioByUserId(id);
		if(cio==null) {
			throw new ResourceNotFoundException("No CIO record with the given user id...!!!");
		}
		return cio;
	}

//--------------------------------------------- UPDATE ------------------------------------------------------------------
	public CIO updateCIO(CIO cio, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		//Checking active Status
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		//Get old employee details
		CIO currentCio = cioRepository.getCioByUserId(currentUser.getId());
		
		//Validate and update
		if(cio.getName()!=null) {
			ActorValidation.validateName(cio.getName());
			currentCio.setName(cio.getName());
		}
		if(cio.getDob()!=null) {
			ActorValidation.validateDob(cio.getDob());
			currentCio.setDob(cio.getDob());
		}
		if(cio.getGender()!=null) {
			ActorValidation.validateGender(cio.getGender());
			currentCio.setGender(cio.getGender());
		}
		if(cio.getEmail()!=null) {
			ActorValidation.validateEmail(cio.getEmail());
			currentCio.setEmail(cio.getEmail());
		}
		if(cio.getContactNumber()!=null) {
			ActorValidation.validateContactNumber(cio.getContactNumber());
			currentCio.setContactNumber(cio.getContactNumber());
		}
		if(cio.getAddress()!=null) {
			ActorValidation.validateAddress(cio.getAddress());
			currentCio.setAddress(cio.getAddress());
		}
		
		
		//Return updated employee		
		return cioRepository.save(currentCio);
		
	}
	
	
	
	
	

}
