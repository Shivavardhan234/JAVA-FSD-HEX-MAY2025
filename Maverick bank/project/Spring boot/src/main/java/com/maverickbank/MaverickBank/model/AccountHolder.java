package com.maverickbank.MaverickBank.model;

import java.time.LocalDate;
import java.time.Period;

import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="account_holder")
public class AccountHolder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private LocalDate dob;
	
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;
	
	@Column(name="contact_number", nullable = false)
	private String contactNumber;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String address;
	
	@Column(name="pan_card_number",nullable=false)
	private String panCardNumber;
	
	
	@Column(name="aadhar_number",nullable=false)
	private String aadharNumber;
	
	
	
	
	
	
	
	
//-----------------------------------Default constructor-------------------------------------------
	
	public AccountHolder() {}
	
	

	
	
//-----------------------------------parameterized constructor-------------------------------------
	
	public AccountHolder(int id, String name,LocalDate dob, Gender gender, String contactNumber, String email,
			String address,String panCardNumber, String aadharNumber) throws InvalidInputException {
		this.setId(id);
		this.setName( name);
		this.setDob(dob);
		this.setGender( gender);
		this.setContactNumber(contactNumber);
		this.setEmail (email);
		this.setAddress(address);
		this.setAadharNumber(aadharNumber);
		this.setPanCardNumber(panCardNumber);
	}
	
	
	
	
	

	
	
	
	
//---------------------------------getters---------------------------------------
	public int getId() {return id;}
	public String getName() {return name;}
	public LocalDate getDob() { return dob;}
	public Gender getGender() {return gender;}
	public String getContactNumber() {return contactNumber;}
	public String getEmail() {return email;}
	public String getAddress() {return address;}
	public String getPanCardNumber() {return panCardNumber;}
	public String getAadharNumber() {return aadharNumber;}
	
	
	
	
	
//-------------------------------setters-----------------------------------------
	
	
	/**Here we check whether id is proper or not .
	 * Checks weather the id is less than 0, if yes throws a InvalidInputException.
	 * Else sets object's id to given id.
	 * @param id
	 * @throws InvalidInputException
	 */
	public void setId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("User ID is Invalid. Please enter appropriate User ID...!!!");
		}
		this.id = id;
	}
	
	
	
	
	/**Here we check whether name is proper or having any empty spaces or any unwanted characters .
	 * if yes throws a InvalidInputException.
	 * Else sets object's name to given name.
	 * @param name
	 * @throws InvalidInputException
	 */
	public void setName(String name)throws InvalidInputException {
		if(name==null || name.trim().isEmpty()||!name.matches("^[A-Za-z ]+$")) {
			throw new InvalidInputException("Name is Invalid. Please enter appropriate Name(Only alphabets and spaces allowed)...!!!");
		}
		this.name = name;
	}
	
	public void setDob(LocalDate dob)throws InvalidInputException {
		if (dob==null|| Period.between(dob,LocalDate.now()).getYears()<18) {
			throw new InvalidInputException("Date Of Birth(DOB) is Invalid. Should be 18 years to create online banking account...!!! ");
		}
		this.dob=dob;
		
	}
	
	
	/**Here we check whether gender is proper or not .
	 * if gender(ENUM) is null then throws a InvalidInputException.
	 * Else sets object's gender to the given gender. 
	 * @param gender
	 * @throws InvalidInputException
	 */
	public void setGender(Gender gender) throws InvalidInputException{
		if(gender==null) {
			throw new InvalidInputException("Gender is Invalid. Please enter appropriate Gender...!!!");
		}//if statement
		this.gender=gender;
	}
	
	
	
	
	
	
	/**Here we check whether contactNumber is proper or not .
	 * if contactNumber is null or contains any characters other than numbers  then throws a InvalidInputException.
	 * Else sets object's contactNumber to the given number. 
	 * @param contactNumber
	 * @throws InvalidInputException
	 */
	public void setContactNumber(String contactNumber) throws InvalidInputException{
		if(contactNumber==null ||!contactNumber.matches("^[0-9]{10}$")) {
			throw new InvalidInputException("Contact number is Invalid. Please enter appropriate 10 digit Contact number...!!!");
		}
		this.contactNumber = contactNumber;
	}
	
	
	
	
	
	/**Here we check whether email is proper or not .
	 * if address is null or empty or is not proper then throws a InvalidInputException.
	 * Else sets object's email to the given email.
	 * @param email
	 * @throws InvalidInputException
	 */
	public void setEmail(String email) throws InvalidInputException{
		if(email==null || email.trim().isEmpty()||!email.matches("^[A-Za-z0-9.%+_-]+@[A-Za-z.]+\\.(com|in)$")) {
			throw new InvalidInputException("Email is Invalid. Please enter appropriate Email...!!!");
		}
		
		
		this.email = email.toLowerCase();
	}
	
	
	
	
	
	
	/**Here we check whether address is proper or not .
	 * if address is null or empty  then throws a InvalidInputException.
	 * Else sets object's address to the given address.
	 * @param address
	 * @throws InvalidInputException
	 */
	public void setAddress(String address) throws InvalidInputException{
		if(address==null || address.trim().isEmpty()||!address.matches("^[A-Za-z0-9 /,.-]+$")) {
			throw new InvalidInputException("Address is Invalid. Please enter appropriate Address...!!!");
		}
		this.address = address;
	}
	
	/**setPanCardNumber() validates the given pan card number by checking 
	 * if it is null or doesn't match the pan number pattern then throws InvalidInputexception
	 * else sets pan card number to object
	 * @param panCardNumber
	 * @throws InvalidInputException
	 */
	public void setPanCardNumber(String panCardNumber)throws InvalidInputException {
		if(panCardNumber==null || !panCardNumber.matches("^[A-Z]{3}[PCAFHT]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}$")) {
			throw new InvalidInputException("Invalid PAN card number. please enter appropriate PAN card number...!!!");
		}
		this.panCardNumber = panCardNumber;
	}
	
	
	
	/**setAadharNumber() validates the given Aadhar card number by checking 
	 * if it is null or doesn't match the Aadhar number pattern then throws InvalidInputexception
	 * else sets Aadhar card number to object
	 * @param aadharNumber
	 * @throws InvalidInputException
	 */
	public void setAadharNumber(String aadharNumber)throws InvalidInputException {
		if(aadharNumber==null || !aadharNumber.matches("^[0-9]{12}$")) {
			throw new InvalidInputException("Invalid Aadhar card number. please enter appropriate Aadhar card number...!!!");
		}
		this.aadharNumber = aadharNumber;
	}
	
	
	
	
	
	
	
	
	

}
