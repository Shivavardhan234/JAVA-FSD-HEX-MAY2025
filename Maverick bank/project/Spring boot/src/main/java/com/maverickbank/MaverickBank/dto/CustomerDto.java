package com.maverickbank.MaverickBank.dto;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.model.users.Customer;

@Component
public class CustomerDto {

	private String name;
	private LocalDate dob;
	private Gender gender;
	private String contactNumber;
	private String email;
	private String address;
	public CustomerDto() {
		super();
	}


	private String panCardNumber;
	private String aadharNumber;
	
	@Autowired
	private UserDto userDto;
	
	public CustomerDto(String name, LocalDate dob, Gender gender, String contactNumber, String email, String address,
			UserDto userDto, String panCardNumber, String aadharNumber) {
		super();
		this.name = name;
		this.dob = dob;
		this.gender = gender;
		this.contactNumber = contactNumber;
		this.email = email;
		this.address = address;
		this.userDto = userDto;
		this.panCardNumber = panCardNumber;
		this.aadharNumber = aadharNumber;
	}
	
	public CustomerDto(Customer customer) {
		this.name = customer.getName();
		this.dob = customer.getDob();
		this.gender = customer.getGender();
		this.contactNumber = customer.getContactNumber();
		this.email = customer.getEmail();
		this.address = customer.getAddress();
		this.userDto = new UserDto(customer.getUser());
		this.panCardNumber = customer.getPanCardNumber();
		this.aadharNumber = customer.getAadharNumber();
	}


	public String getName() {
		return name;
	}


	public LocalDate getDob() {
		return dob;
	}


	public Gender getGender() {
		return gender;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public String getEmail() {
		return email;
	}


	public String getAddress() {
		return address;
	}


	public UserDto getUserDto() {
		return userDto;
	}


	public String getPanCardNumber() {
		return panCardNumber;
	}


	public String getAadharNumber() {
		return aadharNumber;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setDob(LocalDate dob) {
		this.dob = dob;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}


	public void setPanCardNumber(String panCardNumber) {
		this.panCardNumber = panCardNumber;
	}


	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}
	
	
	

}
