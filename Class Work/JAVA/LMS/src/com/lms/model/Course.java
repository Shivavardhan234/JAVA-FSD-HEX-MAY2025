package com.lms.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Course {
	private int courseId;
	private String title;
	private BigDecimal fee;
	private BigDecimal discount;
	private LocalDate publishDate;
	
	private Track track;
	
	public Course() {};
	
	
	public Course(int courseId, String title, BigDecimal fee, BigDecimal discount, LocalDate publishDate,Track track) {
		this.courseId=courseId;
		this.title=title;
		this.fee=fee;
		this.discount=discount;
		this.publishDate=LocalDate.now();
		this.track=track;
		
	}

//----------------------------------getters and setters-----------------------------------------------------------------------
	public int getCourseId() {return courseId;}
	public String getTitle() {return title;}
	public BigDecimal getFee() {return fee;}
	public BigDecimal getDiscount() {return discount;}
	public LocalDate getPublishDate() {return publishDate;}
	public Track getTrack() {return track;}


	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}


	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}


	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}


	public void setTrack(Track track) {
		this.track = track;
	};
	
	
	

}
