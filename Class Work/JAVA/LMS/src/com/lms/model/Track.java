package com.lms.model;

public class Track {
	private int trackId;
	private String trackName;
	
	public Track() {};
	
	
	public Track(int trackId, String trackName) {
		
		this.trackId=trackId;
		this.trackName=trackName;
	}
	
	
	
	public int getTrackId() { return this.trackId; }
	public String getTrackname() { return this.trackName;};
	
	public void setTrackId(int trackId) { this.trackId=trackId; }
	public void setTrackName(String trackName) { this.trackName=trackName; };



}
