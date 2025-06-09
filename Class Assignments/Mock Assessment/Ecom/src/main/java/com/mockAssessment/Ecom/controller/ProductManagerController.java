package com.mockAssessment.Ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mockAssessment.Ecom.exception.InvalidInputException;
import com.mockAssessment.Ecom.exception.ResourceExistsException;
import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.ProductManager;
import com.mockAssessment.Ecom.service.ProductManagerService;

@RestController
@RequestMapping("/api/product-manager")
public class ProductManagerController {
	
	@Autowired
	ProductManagerService productManagerService;
	
	@PostMapping("/add")
	public ProductManager addProductManager(@RequestBody ProductManager productManager) throws InvalidInputException, ResourceExistsException, Exception {
		return productManagerService.addProductManager(productManager);
		
	}
	
	@GetMapping("/get/by-id/{id}")
	public ProductManager getProductManagerById(@PathVariable int id) throws ResourceNotFoundException, Exception {
		return productManagerService.getProductManagerById(id);
	}

	@GetMapping("/get/all")
	public List<ProductManager> getAllProductManager(){
		return productManagerService.getAllProductManager();
		
	}
	
	@PutMapping("/update")
	public ProductManager updateProductManager(@RequestBody ProductManager productManager, Principal principal) throws InvalidInputException, Exception {
		String username=principal.getName();
		return productManagerService.updateProductManager(productManager, username);
	}
}
