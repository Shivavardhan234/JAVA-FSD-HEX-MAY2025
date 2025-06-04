package com.springboot.lms.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.lms.model.Author;
import com.springboot.lms.model.UserInfo;
import com.springboot.lms.repository.AuthorRepository;
import com.springboot.lms.repository.UserInfoRepository;
@Service
public class AuthorService {
	
	AuthorRepository ar;
	UserInfoRepository uir;
	PasswordEncoder pe;
	
	

	public AuthorService(AuthorRepository ar, UserInfoRepository uir, PasswordEncoder pe) {
		super();
		this.ar = ar;
		this.uir = uir;
		this.pe = pe;
	}



	public Author addAuthor(Author author) {
		UserInfo userInfo = author.getUser();
		String encodedPassword = pe.encode(userInfo.getPassword());
		userInfo.setPassword(encodedPassword);
		userInfo.setRole("AUTHOR");
		author.setUser(uir.save(userInfo));
		author.setActive(true);
		return ar.save(author);
	}

}
