package com.maverickbank.MaverickBank.model.users;

import java.time.LocalDate;

import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;


@Entity
public class Customer extends Actor {
	@Column(name="pan_card_number")
	private String panCardNumber;
	@Column(name="aadhar_number")
	private String aadharNumber;
	
	
	//-----------------------default constructor---------------------------------------
	public Customer() throws InvalidInputException { 
		super(Role.CUSTOMER); 
		}
	
	
	
	//--------------Constructor with no user credentials---------------------
	public Customer(int id, String name,LocalDate dob, Gender gender,  String contactNumber, String email, String address, String panCardNumber,String aadharNumber,User user) throws InvalidInputException {
		super(id, name, dob, gender,  contactNumber, email, address,user);
		this.setPanCardNumber ( panCardNumber);
		this.setAadharNumber(aadharNumber);
	}




	
	
	
	
	
	
	
	
	
	
	
//---------------------------getters------------------------------------------------------------
	public String getPanCardNumber() {return panCardNumber;}
	public String getAadharNumber() {return aadharNumber;}
	
	
	
//----------------------------setters-----------------------------------------------------------
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

