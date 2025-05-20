package com.lms.model;

public class Learner {

	private int id;
	private String name;
	private String email;
	private User user;
	
	

	//------------------------------Empty one - Gives us empty objects------------------------------------------------
	public Learner() {};
	
	
	
	//---------------------------for assigning values except user----------------------------------------------------------------
	
		public Learner(int id, String name, String email) {
			super();
			this.id = id;
			this.name = name;
			this.email = email;
		}
	
	//---------------------------for assigning values----------------------------------------------------------------
	
	public Learner(int id, String name, String email, User user) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.user = user;
	}
	
	
	//-----------------------GETTERS & SETTERS-------------------------------------------------------------------------
	
	public User getUser() {return user;}
	public int getId() { return this.id; }
	public String getName() { return this.name; }
	public String getEmail() { return this.email; }
	
	public void setId(int id) { this.id=id; }
	public void setName(String name) { this.name=name; }
	public void setEmail(String email) { this.email=email; }
	

	
	
	//-------------- Overriding toString method - for fetching details of objects--------------------------------------
	
	@Override
	public String toString() {
		return "Learner [id=" + id + ", name=" + name + ", email=" + email + ", user=" + user + "]";
	}

	public void setUser(User user) {this.user = user;}
	
	
}
