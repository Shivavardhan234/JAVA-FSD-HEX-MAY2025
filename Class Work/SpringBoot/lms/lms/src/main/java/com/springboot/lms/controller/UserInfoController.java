package com.springboot.lms.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.lms.model.UserInfo;
import com.springboot.lms.service.UserInfoService;
import com.springboot.lms.util.JwtUtil;

@RestController
@RequestMapping("/api/user")
public class UserInfoController {
	
	
	@Autowired
	UserInfoService us;
	
	@PostMapping("/signup")
	public UserInfo signUp(@RequestBody UserInfo userInfo) {
		return us.signUp(userInfo);
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
	
	@GetMapping("/details")
	public Object getDetails(Principal principal) throws Exception {
		String username = principal.getName(); 
		return us.getByUsername(username);
	}
	
	

}
