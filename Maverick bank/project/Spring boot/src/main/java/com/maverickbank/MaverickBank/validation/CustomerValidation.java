package com.maverickbank.MaverickBank.validation;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.users.Customer;

public class CustomerValidation {
	
	//----------------------------setters-----------------------------------------------------------
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
		
		
		
		
		
		/**setCreditScore() validates the given credit score by checking 
		 * if it is less than 0 then throws InvalidInputexception
		 * else sets credit score to object
		 * @param creditScore
		 * @throws InvalidInputException
		 */
		public static void validateCreditScore(int creditScore)throws InvalidInputException {
			if(creditScore<0) {
				throw new InvalidInputException("Invalid credit score. please enter appropriate credit score value...!!!");
			}
			return;
		}
		
		public static void validateCustomerObject(Customer customer) throws InvalidInputException {
			if(customer== null) {
				throw new InvalidInputException("Invalid Customer provided.It should not be null...!!!");
			}
			return;
		}
		
		public static void fullCustomerValidation(Customer customer) throws InvalidInputException {
			validateCustomerObject(customer);
			ActorValidation.validateName(customer.getName());
			ActorValidation.validateDob(customer.getDob());
			ActorValidation.validateContactNumber(customer.getContactNumber());
			ActorValidation.validateAddress(customer.getAddress());
			ActorValidation.validateEmail(customer.getEmail());
			ActorValidation.validateGender(customer.getGender());
			ActorValidation.validateUserObject(customer.getUser());
			validateAadharNumber(customer.getAadharNumber());
			validatePanCardNumber(customer.getPanCardNumber());
		}
		
		
		public static void customerValidation1(Customer customer) throws InvalidInputException {
			validateCustomerObject(customer);
			ActorValidation.validateName(customer.getName());
			ActorValidation.validateDob(customer.getDob());
			ActorValidation.validateContactNumber(customer.getContactNumber());
			ActorValidation.validateAddress(customer.getAddress());
			ActorValidation.validateEmail(customer.getEmail());
			ActorValidation.validateGender(customer.getGender());
			ActorValidation.validateUserObject(customer.getUser());
			
		}
		
		
		public static void customerValidation2(Customer customer) throws InvalidInputException {
			validateCustomerObject(customer);
			validateAadharNumber(customer.getAadharNumber());
			validatePanCardNumber(customer.getPanCardNumber());
		}
		
	



}
