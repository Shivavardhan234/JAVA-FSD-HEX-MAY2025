package com.lms.dao;

import java.util.List;

import com.lms.exception.InvalidIdException;
import com.lms.model.Course;

public interface CourseDao {
	void addCourse(Course course,int trackId) ;
	List<Course> getAllCourses();
	List<Course> getCourseByTrack(int trackId)throws InvalidIdException;
	Course getCourseById(int courseId) throws InvalidIdException;

}
