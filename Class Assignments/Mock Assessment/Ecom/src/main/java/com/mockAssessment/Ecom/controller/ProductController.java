package com.mockAssessment.Ecom.controller;

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
import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.Product;
import com.mockAssessment.Ecom.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {
	@Autowired
	ProductService productService;
	
	
	@PostMapping("/add")
	public Product addProduct(@RequestBody Product product) {
		return productService.addProuct(product);
	}
	
	@GetMapping("/get/by-id/{id}")
	public Product getProductById(@PathVariable int id) throws ResourceNotFoundException {
		return productService.getProductById(id);
	}

	@GetMapping("/get/all")
	public List<Product> getAllProduct(){
		return productService.getAllProduct();
	}
	
	@PutMapping("/update")
	public Product updateProduct(@RequestBody Product product) throws ResourceNotFoundException, InvalidInputException {
		return productService.updateProduct(product);
		
	}
}
