package com.lms.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Enroll {

	private int enrollmentId;
	private LocalDate dateOfEnroll ;
	private BigDecimal feePaid;
	private String cuponUsed;
	
	private Learner learner;
	private Course course;
	
	public Enroll() {};
	
	
	public Enroll( int enrollmentId, LocalDate dateOfEnroll , BigDecimal feePaid, String cuponUsed, Learner learner, Course course) {
		this.enrollmentId=enrollmentId;
		this.dateOfEnroll = dateOfEnroll;
		this.feePaid= feePaid;
		this.cuponUsed =cuponUsed;
		this.learner=learner;
		this.course=course;
		
	}


	
	
	
	//-----------------------------------getters and setters----------------------------------------------------------------
	
	public int getEnrollmentId() {
		return enrollmentId;
	}


	public LocalDate getDateOfEnroll() {
		return dateOfEnroll;
	}


	public BigDecimal getFeePaid() {
		return feePaid;
	}


	public String getCuponUsed() {
		return cuponUsed;
	}


	public Learner getLearner() {
		return learner;
	}


	public Course getCourse() {
		return course;
	}


	public void setEnrollmentId(int enrollmentId) {
		this.enrollmentId = enrollmentId;
	}


	public void setDateOfEnroll(LocalDate dateOfEnroll) {
		this.dateOfEnroll = dateOfEnroll;
	}


	public void setFeePaid(BigDecimal feePaid) {
		this.feePaid = feePaid;
	}


	public void setCuponUsed(String cuponUsed) {
		this.cuponUsed = cuponUsed;
	}


	public void setLearner(Learner learner) {
		this.learner = learner;
	}


	public void setCourse(Course course) {
		this.course = course;
	}
	
	
	
}
