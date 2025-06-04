package com.maverickbank.MaverickBank.model.users;

import java.time.LocalDate;

import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Employee extends Actor{
	@ManyToOne
	@JoinColumn(nullable=false)
	private Branch branch;
	private String designation;
	
	
	//------------------default constructor-----------------------------------------
	public Employee() throws InvalidInputException {
		super();
	}

	
	//----------------constructor with all fields----------------------------------
	public Employee(int id, String name, LocalDate dob, Gender gender, String contactNumber, String email, String address, User user,Branch branch,String designation) throws InvalidInputException {
		super(id, name, dob, gender, contactNumber, email, address,user);
		this.setBranch(branch);
		this.setDesignation(designation);
		
		
		
	}

	
	


	


	//---------------constructor without user object-------------------------
	public Employee(int id, String name, LocalDate dob, Gender gender, String contactNumber, String email, String address) throws InvalidInputException {
		super(id, name, dob, gender, contactNumber, email, address);
	}



	
	
	
//---------------------------------------- Setters & Getters -------------------------------------------------------
	
	public Branch getBranch() {return branch;}
	public String getDesignation() {return designation;}
	
	
	
	public void setBranch(Branch branch) throws InvalidInputException {
		if (branch==null) {
			throw new InvalidInputException("Provided branch object is null. Please provide appropriate branch object...!!!");
		}
		this.branch = branch;
	}
	
	public void setDesignation(String designation) throws InvalidInputException {
		if (designation==null) {
			throw new InvalidInputException("Provided designation is null. Please provide appropriate designation...!!!");
		}
		this.designation = designation;
	}

	


	
	
	
	

}
