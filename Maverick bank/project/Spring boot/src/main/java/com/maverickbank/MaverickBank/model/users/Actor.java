package com.maverickbank.MaverickBank.model.users;

import java.time.LocalDate;
import java.time.Period;

import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;



/*This is an abstract class user which holds the common properties of 
 * three actors(Customer, Bank Employee, Admin)
 * */
@MappedSuperclass
public abstract class Actor {
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
	
	@Column(name="contact_number",unique = true, nullable = false)
	private String contactNumber;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String address;
	
	
	@OneToOne
	@JoinColumn(nullable=false)
	private User user;
	
	
	
	
	
	
	
	
//-----------------------------------Default constructor-------------------------------------------
	
	public Actor() {}
	
	
//------------------------- constructor only with User Type----------------------------------------
	
		public Actor(Role role) throws InvalidInputException {
			this.setUser(new User(role)); 
		}
	
	
//-----------------------------------parameterized constructor-------------------------------------
	
	public Actor(int id, String name,LocalDate dob, Gender gender, String contactNumber, String email,
			String address,User user) throws InvalidInputException {
		this.setId(id);
		this.setName( name);
		this.setDob(dob);
		this.setGender( gender);
		this.setContactNumber(contactNumber);
		this.setEmail (email);
		this.setAddress(address);
		this.setUser(user);
	}
	
	
	
	
	

	
	
	
//---------------------------------getters---------------------------------------
	public int getId() {return id;}
	public String getName() {return name;}
	public LocalDate getDob() { return dob;}
	public Gender getGender() {return gender;}
	public String getContactNumber() {return contactNumber;}
	public String getEmail() {return email;}
	public String getAddress() {return address;}
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
	
	
	
	
	
	
	
	
	
	
	
	
	

}//User class

