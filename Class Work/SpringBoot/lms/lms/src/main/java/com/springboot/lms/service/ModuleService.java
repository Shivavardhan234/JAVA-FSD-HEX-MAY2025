package com.springboot.lms.service;

import org.springframework.stereotype.Service;

import com.springboot.lms.exception.ResourceNotFoundException;
import com.springboot.lms.model.CModule;
import com.springboot.lms.model.Course;
import com.springboot.lms.repository.CModuleRepository;
import com.springboot.lms.repository.CourseRepository;

@Service
public class ModuleService {
	
	private CourseRepository cr;
	private CModuleRepository mr;
	
	
	public ModuleService(CourseRepository cr,CModuleRepository mr ) {
		this.cr=cr;
		this.mr=mr;
	}
	

	public CModule addModule(int courseId, CModule module) throws ResourceNotFoundException {
		Course course = cr.findById(courseId)
				.orElseThrow(()-> new ResourceNotFoundException("Course with given id not found"));
		module.setCourse(course);
		
		
		return mr.save(module);
	}

}
