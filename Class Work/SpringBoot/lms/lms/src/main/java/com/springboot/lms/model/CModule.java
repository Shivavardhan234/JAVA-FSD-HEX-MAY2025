package com.springboot.lms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="cmodule")
public class CModule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="module_title")
	private String moduleTitle;
	
	
	private int sequence;
	@ManyToOne
	private Course course;
	public int getId() {
		return id;
	}
	public String getModuleTitle() {
		return moduleTitle;
	}
	public int getSequence() {
		return sequence;
	}
	public Course getCourse() {
		return course;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	
	
	

}
