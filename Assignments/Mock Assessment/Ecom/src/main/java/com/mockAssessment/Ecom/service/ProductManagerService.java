package com.mockAssessment.Ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mockAssessment.Ecom.enums.Role;
import com.mockAssessment.Ecom.exception.InvalidInputException;
import com.mockAssessment.Ecom.exception.ResourceExistsException;
import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.ProductManager;
import com.mockAssessment.Ecom.model.User;
import com.mockAssessment.Ecom.repository.ProductManagerRepository;
import com.mockAssessment.Ecom.validation.ProductManagerValidation;

@Service
public class ProductManagerService {
	UserService userService;
	ProductManagerRepository productManagerRepository;
	

	public ProductManagerService(UserService userService, ProductManagerRepository productManagerRepository) {
		super();
		this.userService = userService;
		this.productManagerRepository = productManagerRepository;
	}



	public ProductManager addProductManager(ProductManager productManager) throws InvalidInputException,ResourceExistsException, Exception {
		ProductManagerValidation.validateProductManager(productManager);
		User user=productManager.getUser();
		user.setRole(Role.PRODUCT_MANAGER);
		user=userService.addUser(user);
		productManager.setUser(user);
		return productManagerRepository.save(productManager);
	}



	public ProductManager getProductManagerById(int id) throws ResourceNotFoundException , Exception{

		return productManagerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No record with the given id...!!!"));
	}



	public List<ProductManager> getAllProductManager() {
		
		return productManagerRepository.findAll();
	}



	public ProductManager updateProductManager(ProductManager ProductManager, String username) throws InvalidInputException , Exception{
		if(ProductManager==null) {
			throw new InvalidInputException("Provided ProductManager object is null...!!!");
		}
		ProductManager previousProductManager=productManagerRepository.getProductManagerByUsername(username);
		if(ProductManager.getName()!=null &&!ProductManager.getName().trim().isEmpty()) {
			previousProductManager.setName(ProductManager.getName());
		
		}
		if(ProductManager.getContact()!=null &&!ProductManager.getContact().trim().isEmpty()) {
			previousProductManager.setContact(ProductManager.getContact());
			
		}
		if(ProductManager.getUser()!=null ) {
			previousProductManager.setUser(userService.updateUser(ProductManager.getUser(), username));
			
		}
		
		return productManagerRepository.save(previousProductManager);
	}
	
	
	
	


}
