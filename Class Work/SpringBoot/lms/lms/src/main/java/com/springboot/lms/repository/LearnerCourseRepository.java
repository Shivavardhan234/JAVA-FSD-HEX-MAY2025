package com.springboot.lms.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.lms.model.Learner;
import com.springboot.lms.model.LearnerCourse;

public interface LearnerCourseRepository extends JpaRepository<LearnerCourse, Integer>{
	
	
	@Query(value= "SELECT * FROM learner_course WHERE learner_id=?1 AND course_id=?2 ",nativeQuery = true)
	Optional <LearnerCourse> getUsingNativeQuery(int learnerId, int courseId);
	
	
	@Query("SELECT lc FROM LearnerCourse lc WHERE lc.learner.id=?1 AND lc.course.id=?2")
	Optional <LearnerCourse> getUsingJPQL(int learnerId, int courseId);
	
	
	@Query("SELECT lc.learner FROM LearnerCourse lc WHERE lc.course.id=?1")
	List<Learner> getLearnerByCourseId(int courseId);

}
