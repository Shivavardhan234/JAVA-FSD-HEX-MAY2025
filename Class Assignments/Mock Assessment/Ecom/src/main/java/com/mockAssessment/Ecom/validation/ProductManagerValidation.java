package com.mockAssessment.Ecom.validation;

import com.mockAssessment.Ecom.exception.InvalidInputException;
import com.mockAssessment.Ecom.model.ProductManager;

public class ProductManagerValidation {
	
	public static void validateProductManager(ProductManager productManager) throws InvalidInputException {
		if(productManager==null) {
			throw new InvalidInputException("ProductManager object is null...!!!");
		}
		if(productManager.getName()==null|| productManager.getName().trim().isEmpty()) {
			throw new InvalidInputException("Invalid Product manager name name is provided...!!!");
		}
		if(productManager.getContact()==null|| productManager.getContact().trim().isEmpty()) {
			throw new InvalidInputException("Invalid Product manager contact number is provided...!!!");
		}
		if(productManager.getUser()==null) {
			throw new InvalidInputException("User object in provided Product manager is null...!!!");
		}
	}
	

}
