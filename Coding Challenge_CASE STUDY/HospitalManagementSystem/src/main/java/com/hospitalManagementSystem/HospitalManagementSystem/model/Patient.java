package com.hospitalManagementSystem.HospitalManagementSystem.model;

import java.time.LocalDate;

import com.hospitalManagementSystem.HospitalManagementSystem.enums.Role;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;


@Entity
public class Patient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	
	@Column(nullable=false)
	int age;
	
	
	
	@OneToOne
	@JoinColumn(nullable=false)
	private User user;
	
	
	
	
	
	
	
	
//-----------------------------------Default constructor-------------------------------------------
	
	
		public Patient() throws InvalidInputException {
			this.setUser(new User(Role.PATIENT)); 
		}
	
	
//-----------------------------------parameterized constructor-------------------------------------
	
	public Patient(int id, String name,LocalDate dob, int age, String contactNumber, String email,
			String address,User user) throws InvalidInputException {
		this.setId(id);
		this.setName( name);
		this.setUser(user);
		this.setAge(age);
	}
	
	
	
	
	

	
	
	
//---------------------------------getters---------------------------------------
	public int getId() {return id;}
	public String getName() {return name;}
	public int getAge() {return age;}
	public User getUser() {return user;}
	
	
	
	
	
	
	
//-------------------------------setters-----------------------------------------
	
	
	/**Here we check whether id is proper or not .
	 * Checks weather the id is less than 0, if yes throws a InvalidInputException.
	 * Else sets object's id to given id.
	 * @param id
	 * @throws InvalidInputException
	 */
	public void setId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Patient ID is Invalid. Please enter appropriate ID...!!!");
		}
		this.id = id;
	}
	
	
	
	
	/**Here we check whether name is proper or not
	 * if no throws a InvalidInputException.
	 * Else sets object's name to given name.
	 * @param name
	 * @throws InvalidInputException
	 */
	public void setName(String name)throws InvalidInputException {
		if(name==null || name.trim().isEmpty()) {
			throw new InvalidInputException("Name is Invalid. Please enter appropriate Name(Only alphabets and spaces allowed)...!!!");
		}
		this.name = name;
	}
	
	
	
	/**
	 * @param age
	 * @throws InvalidInputException
	 */
	public void setAge(int age) throws InvalidInputException {
		if( age<=0||age>150) {
			throw new InvalidInputException("Invalid patient age. It should not be less than 0 or more than 150...!!!");
		}
		this.age=age;;
	}
	
	
	
	

	
	
	/**to set credentials and user type
	 * @param credentials
	 * @throws InvalidInputException
	 */
	public void setUser(User user) throws InvalidInputException{
		if (user==null) {
			throw new InvalidInputException("Provided user object is null. Please provide appropriate user object...!!!");
		}
		this.user=user;
	}
	


}
