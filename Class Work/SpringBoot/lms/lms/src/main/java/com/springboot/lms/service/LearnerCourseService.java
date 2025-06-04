package com.springboot.lms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.lms.model.Course;
import com.springboot.lms.model.Learner;
import com.springboot.lms.model.LearnerCourse;
import com.springboot.lms.repository.CourseRepository;
import com.springboot.lms.repository.LearnerCourseRepository;
import com.springboot.lms.repository.LearnerRepository;
import com.springboot.lms.exception.ResourceNotFoundException;

@Service
public class LearnerCourseService {
	
	LearnerRepository lr;
	CourseRepository cr;
	LearnerCourseRepository lcr;
	
	
	public LearnerCourseService(LearnerRepository lr, CourseRepository cr, LearnerCourseRepository lcr) {
		this.lr = lr;
		this.cr = cr;
		this.lcr = lcr;
	}


	public LearnerCourse enrollLearnerInCourse(int learnerId, int courseId, LearnerCourse lc) throws ResourceNotFoundException {
	
		
			Learner l = lr.findById(learnerId).orElseThrow(()-> new ResourceNotFoundException("Learner id invalid"));
			Course c=cr.findById(courseId).orElseThrow(()-> new ResourceNotFoundException("Course id invalid"));
			lc.setCourse(c);
			lc.setLearner(l);
			lc.setEnrollDate(LocalDate.now());
			
		
		
		return lcr.save(lc);
	}


	
	public List<Learner> getLearnerByCourseId(int courseId) throws ResourceNotFoundException {
		cr.findById(courseId).orElseThrow(()-> new ResourceNotFoundException("Course id invalid"));
		return lcr.getLearnerByCourseId(courseId);
	}
	
	
	
	
	

}
