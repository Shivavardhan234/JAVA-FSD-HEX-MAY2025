package com.springboot.lms.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.lms.model.Author;
import com.springboot.lms.model.Learner;
import com.springboot.lms.model.UserInfo;
import com.springboot.lms.repository.AuthorRepository;
import com.springboot.lms.repository.LearnerRepository;
import com.springboot.lms.repository.UserInfoRepository;

@Service
public class UserInfoService {
	private UserInfoRepository ur;
	private PasswordEncoder pe;
	private LearnerRepository lr;
	private AuthorRepository ar;
	public UserInfoService(UserInfoRepository ur, PasswordEncoder pe, LearnerRepository lr,AuthorRepository ar) {
		this.ur=ur;
		this.pe=pe;
		this.lr=lr;
		this.ar=ar;
	}

	
	
	public UserInfo signUp(UserInfo userInfo) {
		String encryptedPassword = pe.encode(userInfo.getPassword());
		userInfo.setPassword(encryptedPassword);
		return ur.save(userInfo);
	}



	public Object getByUsername(String username) throws Exception {
		UserInfo user =ur.getByUsername(username);
		
		
		switch (user.getRole().toUpperCase()) {
		case "LEARNER":
			Learner learner = lr.getLearnerByUsername(username);
			return learner;
		case "AUTHOR":
			Author author = ar.getByUsername(username);
			if(!author.isActive()) {
				throw new Exception("author is inactive...!!!");
			}
			return author;
		case "EXECUTIVE":
			return null;
		default:
			return null;
	}
	}

}
