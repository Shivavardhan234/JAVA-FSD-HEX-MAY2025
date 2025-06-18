package com.springboot.lms.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.lms.model.Course;
import com.springboot.lms.service.CourseService;

@RestController
@RequestMapping("/api/course")
public class CourseController {
	
	@Autowired
	private CourseService cs;
	
	@PostMapping("/add")
	public Course addCourse(@RequestBody Course course) {
		return cs.addCourse(course);
	}

	@GetMapping("/get/all-by-pagenation")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Course> getAllCourse(@RequestParam(name="page",required = false, defaultValue = "0") Integer page,
									 @RequestParam(name="size",required = false,defaultValue = "100000") Integer size){
		return cs.getCourseByPage(page,size);
		
	}
	
	@GetMapping("/get/by-author")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Course> getAllCoursesForAuthor(Principal principal){
		return cs.getAllCoursesForAuthor(principal);
	}
	
	
}
