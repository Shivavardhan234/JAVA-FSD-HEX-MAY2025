package com.springboot.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.lms.exception.ResourceNotFoundException;
import com.springboot.lms.model.LearnerCourse;
import com.springboot.lms.model.Review;
import com.springboot.lms.repository.LearnerCourseRepository;
import com.springboot.lms.repository.ReviewRepository;

@Service
public class ReviewService {
	
	ReviewRepository rr;
	LearnerCourseRepository lcr;

	public ReviewService(ReviewRepository rr,LearnerCourseRepository lcr) {
		this.rr = rr;
		this.lcr=lcr;
	}
	
	public Review addReviewByNativeQuery(int learnerId, int courseId,Review r) throws ResourceNotFoundException {
		
		
			LearnerCourse lc=lcr.getUsingJPQL(learnerId, courseId).orElseThrow(()-> new ResourceNotFoundException("NO RECORDS FOUND WITH GIVEN LEARNER ID AND COURSE ID!!!"));
			r.setLearnerCourse(lc);
		
		return rr.save(r);
		
	}

	public List<Review> getByRating(String rating) {
		
		return rr.getByRating(rating);
	}
	
	
}
