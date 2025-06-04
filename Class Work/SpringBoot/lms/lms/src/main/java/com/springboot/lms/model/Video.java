package com.springboot.lms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Video {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="video_title",nullable=false)
	private String videoTitle;
	@Column(name="play_time")
	private float plaayTime;
	@Column(name="video_code")
	private String videoCode;
	@ManyToOne
	private CModule module;
	public int getId() {
		return id;
	}
	public String getVideoTitle() {
		return videoTitle;
	}
	public float getPlaayTime() {
		return plaayTime;
	}
	public String getVideoCode() {
		return videoCode;
	}
	public CModule getModule() {
		return module;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}
	public void setPlaayTime(float plaayTime) {
		this.plaayTime = plaayTime;
	}
	public void setVideoCode(String videoCode) {
		this.videoCode = videoCode;
	}
	public void setModule(CModule module) {
		this.module = module;
	}
	
	

}
