package com.lms.service;
import java.time.LocalDate;
import java.util.List;

import com.lms.dao.*;
import com.lms.dao.impl.CourseDaoImpl;
import com.lms.exception.InvalidIdException;
import com.lms.model.Course;
public class CourseService {
	
	CourseDao cd = new CourseDaoImpl();
	
	public void addCourse(Course course, int trackId) {
		int id =(int)(Math.random()*10000000);
		
		course.setCourseId(id);
		course.setPublishDate(LocalDate.now());
		cd.addCourse(course, trackId);
		
	}
	
	public List<Course> getAllCourse(){
		return cd.getAllCourses();
		
	}
	public List<Course> getCourseByTrack(int trackId) throws InvalidIdException{
		return cd.getCourseByTrack(trackId);
		
	}
	
	public Course getCourseById(int courseId) throws InvalidIdException {
		return cd.getCourseById(courseId);
		
	}

}
