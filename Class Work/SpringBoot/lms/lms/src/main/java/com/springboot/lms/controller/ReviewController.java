package com.springboot.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.lms.exception.ResourceNotFoundException;
import com.springboot.lms.model.Review;
import com.springboot.lms.service.ReviewService;



@RestController
@RequestMapping("/api/review")
public class ReviewController {
	@Autowired
	ReviewService rs;
	
	
	@PostMapping("/add/{learnerId}/{courseId}")
	public Review addReview(@PathVariable int learnerId, 
							@PathVariable int courseId, 
							@RequestBody Review r ) throws ResourceNotFoundException {
		
		return rs.addReviewByNativeQuery(learnerId, courseId, r);
		
	}
	
	@GetMapping("/get/greater-than/rating")
	public List<Review > getByRating(@RequestParam String rating){
		return rs.getByRating(rating);
	}
	

}
