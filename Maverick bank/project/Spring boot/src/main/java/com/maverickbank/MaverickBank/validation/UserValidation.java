package com.maverickbank.MaverickBank.validation;



import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.User;

public class UserValidation {
	/**Here we check whether id is proper or not .
	 * Checks weather the id is less than 0, if yes throws a InvalidInputException.
	 * Else sets object's id to given id.
	 * @param userId
	 * @throws InvalidInputException
	 */
	public static void validateId(int id) throws InvalidInputException  {
		if( id<=0) {
			throw new InvalidInputException("User ID is Invalid. Please enter appropriate User ID...!!!");
		}
		return;
	}
	
	
	
	
	
	
	
	/**Here we check whether username is proper or not .
	 * if username is null or length less than 5 characters or doesnot follow the given pattern  then throws a InvalidInputException.
	 * Else sets object's address to the given address.
	 * @param username
	 * @throws InvalidInputException
	 */
	public static void validateUsername(String username)throws InvalidInputException {
		if(username==null || (username.trim().length()<=4)||!username.matches("^[A-Za-z0-9_.-]+$")) {
			throw new InvalidInputException("Username is Invalid. Please enter appropriate Username...!!!");
		}
		return;
	}
	
	
	
	
	/**Here we check whether password is proper or not .
	 * if password is null or length less than 8 characters or doesnot follow the given pattern  then throws a InvalidInputException.
	 * Else sets object's password to the given password.
	 * @param password
	 * @throws InvalidInputException
	 */
	public static void validatePassword(String password)throws InvalidInputException {
		if(password==null || (password.trim().length()<8)||!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[`~!@#$%^&*()_+=\\\\/?>\\[\\]\\-{}.,<';:]).+$")) {
			throw new InvalidInputException("Password is Invalid. Please enter appropriate Password...!!!");
		}
		return;
	}
	
	
	
	
	/**Here we check whether userType is proper or not .
	 * if userType(ENUM) is null then throws a InvalidInputException.
	 * Else sets object's userType to the given userType. 
	 * @param userType
	 * @throws InvalidInputException
	 */
	public static void validateRole(Role userType)throws InvalidInputException {
		if(userType==null) {
			throw new InvalidInputException("UserType is Invalid. Please check the logic...!!!");
		}//if statement
		return;
	}

	
	public static void fullUserValidation(User user) throws InvalidInputException {
		validateUsername(user.getUsername());
		validatePassword(user.getPassword());
		validateRole(user.getRole());
	}

}
