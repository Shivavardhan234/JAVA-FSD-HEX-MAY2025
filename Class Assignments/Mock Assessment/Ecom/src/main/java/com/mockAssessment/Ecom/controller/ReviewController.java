package com.mockAssessment.Ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.Review;
import com.mockAssessment.Ecom.service.ReviewService;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
	@Autowired
	ReviewService reviewService;
	
	
	@PostMapping("/add")
	public Review addReview(@RequestBody Review review, Principal principal) throws Exception {
		String username=principal.getName();
		return reviewService.addReview(review , username);
	}
	
	@GetMapping("/get/by-id/{id}")
	public Review getReviewById(@PathVariable int id) throws ResourceNotFoundException {
		return reviewService.getReviewById(id);
		
	}

	@GetMapping("/get/all")
	public List<Review> getAllReview(){
		return reviewService.getAllReview();
	}
	
	@PutMapping("/update")
	public Review updateReview(@RequestBody Review review) {
		return reviewService.updateReview(review);
		
	}

}
