package com.maverickbank.MaverickBank.validation;

import java.time.LocalDate;
import java.time.Period;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.Actor;

public class ActorValidation {

	
	//-------------------------------setters-----------------------------------------
	
	
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
		
		
		
		/**to set credentials and user type
		 * @param credentials
		 * @throws InvalidInputException
		 */
		public static void validateUser(User user) throws InvalidInputException{
			if (user==null) {
				throw new InvalidInputException("Provided user object is null. Please provide appropriate user object...!!!");
			}
			return;
		}
		
		
		public static void validateStatus(ActiveStatus status) throws InvalidInputException{
			if (status == null) {
	            throw new InvalidInputException("Null active status provided. Please provide appropriate active status...!!!");
	        }
			return;
		}
		
		
		public static void fullActorValidation(Actor user) throws InvalidInputException {
			validateName(user.getName());
			validateAddress(user.getAddress());
			validateContactNumber(user.getContactNumber());
			validateDob(user.getDob());
			validateEmail(user.getEmail());
			validateGender(user.getGender());
		}


}
