package com.hospitalManagementSystem.HospitalManagementSystem.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospitalManagementSystem.HospitalManagementSystem.util.JwtUtil;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
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
