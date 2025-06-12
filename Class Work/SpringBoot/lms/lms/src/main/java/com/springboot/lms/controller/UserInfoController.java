package com.springboot.lms.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/signup")
	public UserInfo signUp(@RequestBody UserInfo userInfo) {
		return us.signUp(userInfo);
	}
	
	@GetMapping("/token/v1")
	@CrossOrigin(origins = "http://localhost:5173")
	public ResponseEntity<?> getToken(Principal principal) {
		try {
			String token = jwtUtil.createToken(principal.getName());
			return ResponseEntity.status(HttpStatus.OK).body(token);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
	
	
	@GetMapping("/token/v2")
	public String getTokenV2(Principal principal) {
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
