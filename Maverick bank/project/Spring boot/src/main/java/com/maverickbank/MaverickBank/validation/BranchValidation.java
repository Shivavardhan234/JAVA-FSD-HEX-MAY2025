package com.maverickbank.MaverickBank.validation;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.Branch;

public class BranchValidation {
	public static void validateId(int id) throws InvalidInputException {
		if( id<=0) {
			throw new InvalidInputException("Branch ID is Invalid. Please enter appropriate Branch ID...!!!");
		}
		return;
	}



	public static void validateIfsc(String ifsc) throws InvalidInputException {
		if(ifsc==null || ifsc.trim().isEmpty()||!ifsc.matches("^MVRK0[0-9]{6}$")) {
			throw new InvalidInputException("IFSC code is Invalid. Please enter appropriate IFSC code...!!!");
		}
		return;
	}



	public static void validateBranchName(String brachName) throws InvalidInputException {
		if(brachName==null || brachName.trim().isEmpty()||!brachName.matches("^[A-Za-z _]+$")) {
			throw new InvalidInputException("Branch Name is Invalid. Please enter appropriate Branch Name...!!!");
		}
		return;
	}



	public static void validateAddress(String address) throws InvalidInputException {
		if(address==null || address.trim().isEmpty()||!address.matches("^[A-Za-z _&]+$")) {
			throw new InvalidInputException("State Name is Invalid. Please enter appropriate State Name...!!!");
		}
		return;
	}
	

	public static void validateStatus(ActiveStatus status) throws InvalidInputException{
		if (status == null) {
            throw new InvalidInputException("Null active status provided. Please provide appropriate active status...!!!");
        }
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
	
	public static void validateBranchObject(Branch branch)throws InvalidInputException {
		if(branch==null) {
			throw new InvalidInputException("Branch is Invalid. Branch is null...!!!");
		}
		return;
	}
	
	
	
	public static void validateForNewBranch(Branch branch) throws InvalidInputException {
		validateBranchObject(branch);
		validateBranchName(branch.getBranchName());
		validateIfsc(branch.getIfsc());
		validateAddress(branch.getAddress());
		validateContactNumber(branch.getContactNumber());
		validateEmail(branch.getEmail());
		
	}


}
