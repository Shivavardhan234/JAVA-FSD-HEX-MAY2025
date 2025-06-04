package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.Util.JwtUtil;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	UserService us;
	
	
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
	public User addUser(@RequestBody User user) throws InvalidInputException, ResourceExistsException, Exception {
		return us.addUser(user);
		
	}
	
	@GetMapping("/get-all")
	public List<User> getAllUser(){
		return us.getAllUser();
	}
	
	
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
	
	
	@GetMapping("/get/details")
	public Object getDetails(Principal principal) throws ResourceNotFoundException {
		String username=principal.getName();
		return us.getByUsername(username);
		
	}

}
