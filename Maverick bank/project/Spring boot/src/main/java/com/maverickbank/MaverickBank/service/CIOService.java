package com.maverickbank.MaverickBank.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.CIO;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.repository.CIORepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.CIOValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;
import com.maverickbank.MaverickBank.validation.ActorValidation;

@Service
public class CIOService {
	
	CIORepository cior;
	UserRepository ur;
	PasswordEncoder pe;
	
	

	public CIOService(CIORepository cior, UserRepository ur, PasswordEncoder pe) {
		this.cior = cior;
		this.ur = ur;
		this.pe = pe;
	}



	/** addAdmin() method validates and adds the admin to the database
	 * Step 1: Check the given CIO object is null
	 * Step 2: Validate CIO details using validation classes
	 * Step 3: Check weather username already exists or not, if exists throw resource exists exception 
	 * Step 4: As contact number and email are unique check weather they exists, if exists throw resource exists exception
	 * Step 5: Encode the password using password encoder
	 * Step 6: set status to active as we are creating it now
	 * Step 7: save the user and set the returned user object to CIO object
	 * Step 8: Save CIO in the db
	 * @param admin
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceExistsException
	 * @throws Exception
	 */
	public CIO addAdmin(CIO admin) throws InvalidInputException, ResourceExistsException , Exception{
		//Checking whether provided CIO object is null 
		if(admin==null) {
			throw new InvalidInputException("Provided CIO object is null...!!!");
		}
		//Validating CIO object
		CIOValidation.fullCioValidation(admin);
		
		//Extract user
		User user=admin.getUser();
		//Validate username
		UserValidation.validateUsername(user.getUsername());
		//Checking whether username exists in database
		if(ur.getByUsername(user.getUsername())!=null){
			throw new ResourceExistsException("This username already exists!!!");
			
		}
		//Validating for strong password
		UserValidation.validatePassword(user.getPassword());
		
		//Checking whether email and contactnumber already exists in db
		if(cior.getByContactNumber(admin.getContactNumber())!=null){
			throw new ResourceExistsException("This Contact number already exists!!!");	
		}
		
		if(cior.getByEmail(admin.getEmail())!=null){
			throw new ResourceExistsException("This Email already exists!!!");	
		}
		//set remaining properties
		user.setRole(Role.ADMIN);
		String password = pe.encode(user.getPassword());
		user.setPassword(password);
		admin.setStatus(ActiveStatus.ACTIVE);
		//Set saved user object with user id
		admin.setUser(ur.save(user));
		//Save CIO object
		return cior.save(admin);
		
	}

	
	


	/**Fetches all the CIO's
	 * @return
	 */
	public List<CIO> getAllCIO() throws Exception {
		
		return cior.findAll();
	}

}
