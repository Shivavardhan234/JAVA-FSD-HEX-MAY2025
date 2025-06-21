package com.springboot.lms.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.lms.model.Author;
import com.springboot.lms.model.Course;
import com.springboot.lms.service.AuthorService;

@RestController
@RequestMapping("/api/author")
public class AuthorController {
	@Autowired
	AuthorService as;
	
	@PostMapping("/add")
	public Author addAuthor(@RequestBody Author author) {
		return as.addAuthor(author);
	}
	
	
	@PostMapping("/upload/profile-pic")
	public Author uploadProfilePic(@RequestParam MultipartFile file,Principal principal) throws IOException {
		return as.uploadProfilePic(file,principal);
	}
	
	

}
