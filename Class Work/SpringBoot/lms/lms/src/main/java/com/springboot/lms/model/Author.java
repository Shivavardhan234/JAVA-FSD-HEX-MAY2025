package com.springboot.lms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "author")
public class Author {

    public Author() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String contact;
    private String website;
    private String profilePic;
    @ManyToOne
    private UserInfo user;
    private boolean isActive;
	public Author(int id, String name, String contact, String website, String profilePic, UserInfo user,boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		this.contact = contact;
		this.website = website;
		this.profilePic = profilePic;
		this.user = user;
		this.isActive=isActive;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getContact() {
		return contact;
	}
	public String getWebsite() {
		return website;
	}
	public String getProfilePic() {
		return profilePic;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    
    
}