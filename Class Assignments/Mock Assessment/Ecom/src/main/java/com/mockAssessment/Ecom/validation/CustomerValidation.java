package com.mockAssessment.Ecom.validation;

import com.mockAssessment.Ecom.exception.InvalidInputException;
import com.mockAssessment.Ecom.model.Customer;

public class CustomerValidation {

	public static void validateCustomer(Customer customer) throws InvalidInputException {
		if(customer==null) {
			throw new InvalidInputException("Customer object is null...!!!");
		}
		if(customer.getName()==null|| customer.getName().trim().isEmpty()) {
			throw new InvalidInputException("Invalid Customer name is provided...!!!");
		}
		if(customer.getContact()==null|| customer.getContact().trim().isEmpty()) {
			throw new InvalidInputException("Invalid Customer number is provided...!!!");
		}
		if(customer.getUser()==null) {
			throw new InvalidInputException("User object in provided customer is null...!!!");
		}
	}
	
	
}
