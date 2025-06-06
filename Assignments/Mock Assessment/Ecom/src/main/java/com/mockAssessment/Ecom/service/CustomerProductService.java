package com.mockAssessment.Ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.CustomerProduct;
import com.mockAssessment.Ecom.repository.CustomerProductRepository;

@Service
public class CustomerProductService {
	
	CustomerProductRepository customerProductRepository;
	
	

	public CustomerProductService(CustomerProductRepository customerProductRepository) {
		this.customerProductRepository = customerProductRepository;
	}



	public CustomerProduct addCustomerProduct(CustomerProduct customerProduct) {
		
		return customerProductRepository.save(customerProduct);
	}



	public CustomerProduct getcustomerProductById(int id) throws ResourceNotFoundException {
		
		return customerProductRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No Customer Product record with the given id...!!!"));
	}



	public List<CustomerProduct> getAllCustomerProducts() {
		// TODO Auto-generated method stub
		return customerProductRepository.findAll();
	}



	public CustomerProduct updateCustomerProduct(CustomerProduct customerProduct) throws ResourceNotFoundException {
		CustomerProduct previousCustomerProduct=customerProductRepository.findById(customerProduct.getId()).orElseThrow(()->new ResourceNotFoundException("No Customer Product record with the given id...!!!"));
		if(customerProduct.getCustomer()!=null) {
			previousCustomerProduct.setCustomer(customerProduct.getCustomer());
		}
		if(customerProduct.getProduct()!=null) {
			previousCustomerProduct.setProduct(customerProduct.getProduct());
		}
		if(customerProduct.getCoupon()!=null) {
			previousCustomerProduct.setCoupon(customerProduct.getCoupon());
		}
		return customerProductRepository.save(previousCustomerProduct);
	}
	
	

}
