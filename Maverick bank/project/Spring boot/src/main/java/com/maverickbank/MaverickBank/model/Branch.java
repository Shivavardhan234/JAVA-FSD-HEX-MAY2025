package com.maverickbank.MaverickBank.model;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Branch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false)
	private String ifsc;
	@Column(name="branch_name",nullable=false)
	private String branchName;
	@Column(nullable=false)
	private String address;
	@Column(name="contact_number",unique=true, nullable = false)
	private String contactNumber;
	
	@Column(nullable = false,unique=true)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private ActiveStatus status;
	
	
	
	
	// -------------------------------------- Constructors -------------------------------------------------------
	public Branch() {}



	public Branch(int id, String ifsc, String branchName, String address,String contactNumber,String email) throws InvalidInputException {
		this.setId(id);
		this.setIfsc(ifsc);
		this.setBranchName(branchName);
		this.setAddress(address);
		this.setContactNumber(contactNumber);
		this.setEmail(email);
	}


	//------------------------------------- Getters & Setters ----------------------------------------------------
	public int getId() {return id;}
	public String getIfsc() {return ifsc;}
	public String getBranchName() {return branchName;}
	public String getContactNumber() {return contactNumber;}
	public String getEmail() {return email;}
	public String getAddress() {return address;}
	public ActiveStatus getStatus() {return status;}


	public void setId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Branch ID is Invalid. Please enter appropriate Branch ID...!!!");
		}
		this.id = id;
	}



	public void setIfsc(String ifsc) throws InvalidInputException {
		if(ifsc==null || ifsc.trim().isEmpty()||!ifsc.matches("^MVRK0[0-9]{6}$")) {
			throw new InvalidInputException("IFSC code is Invalid. Please enter appropriate IFSC code...!!!");
		}
		this.ifsc = ifsc;
	}



	public void setBranchName(String branchName) throws InvalidInputException {
		if(branchName==null || branchName.trim().isEmpty()||!branchName.matches("^[A-Za-z _]+$")) {
			throw new InvalidInputException("Branch Name is Invalid. Please enter appropriate Branch Name...!!!");
		}
		this.branchName = branchName;
		
		
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
	



	public void setAddress(String address) throws InvalidInputException {
		if(address==null || address.trim().isEmpty()) {
			throw new InvalidInputException("Address is Invalid. Please enter appropriate Address...!!!");
		}
		this.address = address;
	}
	

	public void setStatus(ActiveStatus status) throws InvalidInputException{
		if (status == null) {
            throw new InvalidInputException("Null active status provided. Please provide appropriate active status...!!!");
        }
		this.status = status;
	}

	

}
