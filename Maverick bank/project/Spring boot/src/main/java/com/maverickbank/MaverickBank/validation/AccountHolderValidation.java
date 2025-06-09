package com.maverickbank.MaverickBank.validation;

import java.time.LocalDate;
import java.time.Period;

import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.AccountHolder;

public class AccountHolderValidation {
	/**Here we check whether id is proper or not .
	 * Checks weather the id is less than 0, if yes throws a InvalidInputException.
	 * Else sets object's id to given id.
	 * @param id
	 * @throws InvalidInputException
	 */
	public static void validateId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("User ID is Invalid. Please enter appropriate User ID...!!!");
		}
		return;
	}
	
	
	
	
	/**Here we check whether name is proper or having any empty spaces or any unwanted characters .
	 * if yes throws a InvalidInputException.
	 * Else sets object's name to given name.
	 * @param name
	 * @throws InvalidInputException
	 */
	public static void validateName(String name)throws InvalidInputException {
		if(name==null || name.trim().isEmpty()||!name.matches("^[A-Za-z ]+$")) {
			throw new InvalidInputException("Name is Invalid. Please enter appropriate Name(Only alphabets and spaces allowed)...!!!");
		}
		return;
	}
	
	public static void validateDob(LocalDate dob)throws InvalidInputException {
		if (dob==null|| Period.between(dob,LocalDate.now()).getYears()<18) {
			throw new InvalidInputException("Date Of Birth(DOB) is Invalid. Should be 18 years to create online banking account...!!! ");
		}
		return;
		
	}
	
	
	/**Here we check whether gender is proper or not .
	 * if gender(ENUM) is null then throws a InvalidInputException.
	 * Else sets object's gender to the given gender. 
	 * @param gender
	 * @throws InvalidInputException
	 */
	public static void validateGender(Gender gender) throws InvalidInputException{
		if(gender==null) {
			throw new InvalidInputException("Gender is Invalid. Please enter appropriate Gender...!!!");
		}//if statement
		return;
	}
	
	
	
	
	
	
	/**Here we check whether contactNumber is proper or not .
	 * if contactNumber is null or contains any characters other than numbers  then throws a InvalidInputException.
	 * Else sets object's contactNumber to the given number. 
	 * @param contactNumber
	 * @throws InvalidInputException
	 */
	public static void validateContactNumber(String contactNumber) throws InvalidInputException{
		if(contactNumber==null ||!contactNumber.matches("^[0-9]{10}$")) {
			throw new InvalidInputException("Contact number is Invalid. Please enter appropriate 10 digit Contact number...!!!");
		}
		return;
	}
	
	
	
	
	
	/**Here we check whether email is proper or not .
	 * if address is null or empty or is not proper then throws a InvalidInputException.
	 * Else sets object's email to the given email.
	 * @param email
	 * @throws InvalidInputException
	 */
	public static void validateEmail(String email) throws InvalidInputException{
		if(email==null || email.trim().isEmpty()||!email.matches("^[A-Za-z0-9.%+_-]+@[A-Za-z.]+\\.(com|in)$")) {
			throw new InvalidInputException("Email is Invalid. Please enter appropriate Email...!!!");
		}
		
		
		return;
	}
	
	
	
	
	
	
	/**Here we check whether address is proper or not .
	 * if address is null or empty  then throws a InvalidInputException.
	 * Else sets object's address to the given address.
	 * @param address
	 * @throws InvalidInputException
	 */
	public static void validateAddress(String address) throws InvalidInputException{
		if(address==null || address.trim().isEmpty()||!address.matches("^[A-Za-z0-9 /,.-]+$")) {
			throw new InvalidInputException("Address is Invalid. Please enter appropriate Address...!!!");
		}
		return;
	}
	
	/**setPanCardNumber() validates the given pan card number by checking 
	 * if it is null or doesn't match the pan number pattern then throws InvalidInputexception
	 * else sets pan card number to object
	 * @param panCardNumber
	 * @throws InvalidInputException
	 */
	public static void validatePanCardNumber(String panCardNumber)throws InvalidInputException {
		if(panCardNumber==null || !panCardNumber.matches("^[A-Z]{3}[PCAFHT]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}$")) {
			throw new InvalidInputException("Invalid PAN card number. please enter appropriate PAN card number...!!!");
		}
		return;
	}
	
	
	
	/**setAadharNumber() validates the given Aadhar card number by checking 
	 * if it is null or doesn't match the Aadhar number pattern then throws InvalidInputexception
	 * else sets Aadhar card number to object
	 * @param aadharNumber
	 * @throws InvalidInputException
	 */
	public static void validateAadharNumber(String aadharNumber)throws InvalidInputException {
		if(aadharNumber==null || !aadharNumber.matches("^[0-9]{12}$")) {
			throw new InvalidInputException("Invalid Aadhar card number. please enter appropriate Aadhar card number...!!!");
		}
		return;
	}
	
	
	public static void validateAccountHolderObject(AccountHolder accountHolder)throws InvalidInputException{
		if(accountHolder==null) {
			throw new InvalidInputException("provided account holder object is null");
			
		}
		return;
	}
	
	public static void validateAccountHolder(AccountHolder accountHolder)throws InvalidInputException{
		validateAccountHolderObject(accountHolder);
		
		validateName(accountHolder.getName());
		validateDob(accountHolder.getDob());
		validateGender(accountHolder.getGender());
		validateContactNumber(accountHolder.getContactNumber());
		validateEmail(accountHolder.getEmail());
		validateAddress(accountHolder.getAddress());
		validateAadharNumber(accountHolder.getAadharNumber());
		validatePanCardNumber(accountHolder.getPanCardNumber());
		
	}
	
	



}
