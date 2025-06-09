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

import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.CustomerProduct;
import com.mockAssessment.Ecom.service.CustomerProductService;

@RestController
@RequestMapping("/api/customer-product")
public class CustomerProductController {
	
	@Autowired
	CustomerProductService customerProductService;
	
	
	
	@PostMapping("/add")
	public CustomerProduct addCustomerProduct(@RequestBody CustomerProduct customerProduct) {
		return customerProductService.addCustomerProduct(customerProduct);
		
	}
	
	@GetMapping("/get/by-id/{id}")
	public CustomerProduct getCustomerProductById(@PathVariable int id) throws ResourceNotFoundException {
		return customerProductService.getcustomerProductById(id);
		
	}

	@GetMapping("/get/all")
	public List<CustomerProduct> getAllCustomerProduct(){
		return customerProductService.getAllCustomerProducts();
	}
	
	@PutMapping("/update")
	public CustomerProduct updateCustomerProduct(@RequestBody CustomerProduct customerproduct) throws ResourceNotFoundException {
		return customerProductService.updateCustomerProduct(customerproduct);
	}

}
