package com.springboot.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.lms.exception.ResourceNotFoundException;
import com.springboot.lms.model.Learner;
import com.springboot.lms.model.LearnerCourse;
import com.springboot.lms.service.LearnerCourseService;


@RestController
public class LearnerCourseController {
	
	
	@Autowired
	LearnerCourseService lcs;
	
	@PostMapping("/api/learner/enroll/course/{learnerId}/{courseId}")
	public LearnerCourse enrollLearnerInCourse(@PathVariable int learnerId, @PathVariable int courseId,@RequestBody LearnerCourse lc) throws ResourceNotFoundException {
		return lcs.enrollLearnerInCourse(learnerId, courseId, lc);
	}
	
	
	
	
	@GetMapping("/api/learner/enroll/course/{courseId}")
	public List<Learner> getLearnerByCourseId(@PathVariable int courseId) throws ResourceNotFoundException{
		return lcs.getLearnerByCourseId(courseId);
		
	}

}
