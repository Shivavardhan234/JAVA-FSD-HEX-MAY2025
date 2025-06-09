package com.mockAssessment.Ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mockAssessment.Ecom.exception.InvalidInputException;
import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.Product;
import com.mockAssessment.Ecom.repository.ProductRepository;

@Service
public class ProductService {
	
	ProductRepository productRepository;
	
	

	public ProductService(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}



	public Product addProuct(Product product) {
		
		return productRepository.save(product);
	}



	public Product getProductById(int id) throws ResourceNotFoundException {
		
		return productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No product with the given id...!!!"));
	}



	public List<Product> getAllProduct() {
		
		return productRepository.findAll();
	}



	public Product updateProduct(Product product) throws ResourceNotFoundException, InvalidInputException {
		if(product==null) {
			throw new InvalidInputException("Provided product object is null...!!!");
		}
		
		Product previousProduct=productRepository.findById(product.getId()).orElseThrow(()-> new ResourceNotFoundException("No product with the given id...!!!"));
		
		if(product.getTitle()!=null &&!product.getTitle().trim().isEmpty()) {
			previousProduct.setTitle(product.getTitle());
			
		}
		if(product.getPrice()!=null ) {
			previousProduct.setPrice(product.getPrice());
			
		}
		
		
		return productRepository.save(product);
	}
	
	

}
