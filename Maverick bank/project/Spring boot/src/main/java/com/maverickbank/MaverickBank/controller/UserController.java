package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.Util.JwtUtil;
import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidCredentialsException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
//------------------------------------ POST --------------------------------------------------------	
	/**AIM    : Add user to database
	 * INPUT  : User object
	 * RETURN : Updated User object with id
	 * @param user
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceExistsException
	 * @throws Exception
	 */
	@PostMapping("/add")
	public User addUser(@RequestBody User user) throws InvalidInputException, ResourceExistsException, Exception , Exception {
		return userService.addUser(user);
		
	}
	
	
//-------------------------------- GET ------------------------------------------------------------
	
	
	/**AIM    : Get all the users
	 * RETURN : List of all users
	 * @return
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	@GetMapping("/get/all")
	public List<User> getAllUser(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException{
		return userService.getAllUser(principal);
	}
	
	
	
	/**AIM    : Get user by its id
	 * INPUT  : User id
	 * RETURN : User object with id
	 * @param id
	 * @return
	 * @throws Exception 
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/get/by-id/{id}")
	public User getUserById(@PathVariable int id,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException, Exception  {
		return userService.getUserById(id, principal);
		
	}
	
	
	
	/**AIM : To get user Details
	 * INPUT : Principal
	 * OUTPUT : Customer/Employee/CIO object
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException 
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
//	@GetMapping("/get/details")
//	public Object getDetails(Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
//		return userService.getByUsername(principal);
//		
//	}
	
	
	
//---------------------------------- UPDATE -------------------------------------------------------	
	
	/**AIM : To update the username of current user
	 * INPUT : Username and principal
	 * @param username
	 * @param principal
	 * @return
	 * @throws InvalidInputException
	 */
	@PutMapping("/update/username/{username}")
	public User updateUsername(@PathVariable String username, Principal principal) throws InvalidInputException , Exception {
		return userService.updateUsername(username,principal);
		
	}
	
	@PutMapping("/update/password/{oldPassword}/{newPassword}")
	public User updatePassword(@PathVariable String oldPassword,@PathVariable String newPassword, Principal principal) throws InvalidInputException, InvalidCredentialsException, Exception  {
		return userService.updatePassword(oldPassword,newPassword,principal);
		
	}
	
	@PutMapping("/update/role/{id}/{newRole}")
	public User updateRole(@PathVariable int id, @PathVariable Role newRole, Principal principal) throws InvalidInputException, InvalidActionException, ResourceNotFoundException, Exception {
		
		return userService.updateUserRole(id,newRole, principal);
	}
	
	
	@PutMapping("/update/deactivate/{password}")
	public User deactivateUserAccount(@PathVariable String password,Principal principal) throws InvalidInputException, InvalidActionException, InvalidCredentialsException, Exception {
		return userService.deactivateUserAccount(password,principal);
		
	}
	
	
	@PutMapping("/update/activate")
	public User activateUser(Principal principal) throws InvalidActionException, DeletedUserException, InvalidInputException, Exception {
		return userService.activateUser(principal);
	}
	
	@PutMapping("/update/status/{id}/{status}")
	public User updateUserActiveStatus(@PathVariable int id,@PathVariable ActiveStatus status,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		return userService.updateUserActiveStatus(id,status,principal);
	}
	
	
	
//-------------------------------UPDATE but Marking as DELETE -------------------------------------
	
	@PutMapping("/update/delete/{password}")
	public User deleteUserAccount(@PathVariable String password,Principal principal) throws InvalidInputException, InvalidActionException, InvalidCredentialsException, Exception {
		return userService.deleteUserAccount(password,principal);
		
	}
	
	
	
	
	
	
//-------------------------------------- UTILS ----------------------------------------------------	
	
	@GetMapping("/token/v1")
	public String getTokenV1(Principal principal) {
		System.out.println("I am in the API method");
		
		JwtUtil jwtUtil = new JwtUtil();
		return jwtUtil.createToken(principal.getName()); 
	}
	
	@GetMapping("/token/v2")
	public String getTokenV2(Principal principal) {
		JwtUtil jwtUtil = new JwtUtil();
		String token =jwtUtil.createToken(principal.getName());
		return jwtUtil.verifyToken(token, principal.getName())?
						"Token verified":
						"Not verified";
		
	}
	
	
	
	
	
	
	

}
