package com.maverickbank.MaverickBank.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.model.AccountHolder;


@Component
public class AccountHolderDto {
	private String name;
	private LocalDate dob;
	private Gender gender;
	private String contactNumber;
	private String email;
	private String address;
	private String panCardNumber;
	private String aadharNumber;
	
	
	
	public AccountHolderDto() {
		super();
	}

	public AccountHolderDto(String name, LocalDate dob, Gender gender, String contactNumber, String email, String address,
			 String panCardNumber, String aadharNumber) {
		super();
		this.name = name;
		this.dob = dob;
		this.gender = gender;
		this.contactNumber = contactNumber;
		this.email = email;
		this.address = address;
		this.panCardNumber = panCardNumber;
		this.aadharNumber = aadharNumber;
	}
	
	public AccountHolderDto(AccountHolder accountHolder){
		this.name = accountHolder.getName();
		this.dob = accountHolder.getDob();
		this.gender = accountHolder.getGender();
		this.contactNumber = accountHolder.getContactNumber();
		this.email = accountHolder.getEmail();
		this.address = accountHolder.getAddress();
		this.panCardNumber = accountHolder.getPanCardNumber();
		this.aadharNumber = accountHolder.getAadharNumber();
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


	public void setPanCardNumber(String panCardNumber) {
		this.panCardNumber = panCardNumber;
	}


	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}
	
	

}
