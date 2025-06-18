package com.springboot.lms.service;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.lms.model.Course;
import com.springboot.lms.repository.CourseRepository;


@Service
public class CourseService {
	CourseRepository cr;
	
	Logger logger = LoggerFactory.getLogger("CourseService");
	

	public CourseService(CourseRepository cr){
		this.cr=cr;
		
	}
	

	public Course addCourse(Course course) {
		course=cr.save(course);
		logger.info("course added");
		return course;
	}


	public List<Course> getCourseByPage(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return cr.findAll(pageable).getContent();
	}
	
	
	public List<Course> getAllCoursesForAuthor(Principal principal) {
			String username=principal.getName();
			return cr.getAllCoursesForAuthor(username);
		}

}
