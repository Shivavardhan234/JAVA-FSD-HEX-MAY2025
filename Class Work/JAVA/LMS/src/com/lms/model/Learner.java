package com.lms.model;

public class Learner {

	private int id;
	private String name;
	private String email;
	
	//------------------------------Empty one - Gives us empty objects------------------------------------------------
	public Learner() {};
	
	//---------------------------for assigning values----------------------------------------------------------------
	
	public Learner( int id, String name, String email) {
		this.id=id;
		this.name=name;
		this.email=email;
		
	}
	
	
	//-----------------------GETTERS & SETTERS-------------------------------------------------------------------------
	
	public int getId() { return this.id; }
	public String getName() { return this.name; }
	public String getEmail() { return this.email; }
	
	public void setId(int id) { this.id=id; }
	public void setName(String name) { this.name=name; }
	public void setEmail(String email) { this.email=email; }
	
	
	
	//-------------- Overriding toString method - for fetching details of objects--------------------------------------
	
	@Override
	public String toString() {
		return ("[ID: "+ id + ",    Name: " + name + ",    Email: "+ email+ "]");
	}
	
	
}
