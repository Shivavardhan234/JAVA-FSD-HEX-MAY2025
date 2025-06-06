package com.mockAssessment.Ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mockAssessment.Ecom.exception.ResourceNotFoundException;
import com.mockAssessment.Ecom.model.Customer;
import com.mockAssessment.Ecom.model.Review;
import com.mockAssessment.Ecom.repository.CustomerProductRepository;
import com.mockAssessment.Ecom.repository.CustomerRepository;
import com.mockAssessment.Ecom.repository.ReviewRepository;

@Service
public class ReviewService {
	ReviewRepository reviewRepository;
	CustomerRepository customerRepository;
	CustomerProductRepository customerProductRepository;

	public ReviewService(ReviewRepository reviewRepository, CustomerRepository customerRepository,CustomerProductRepository customerProductRepository) {
		this.reviewRepository = reviewRepository;
		this.customerRepository = customerRepository;
		this. customerProductRepository= customerProductRepository;
	}


	public Review addReview(Review review, String username) throws Exception {
		Customer customer=customerRepository.getCustomerByUsername(username);
		
		if( customerProductRepository.getCustomerProductByDetails(customer.getId(), review.getProduct().getId())==null) {
			throw new Exception("You are not eligible to review this product...!!!");
		}
		review.setCustomer(customer);
		return reviewRepository.save(review);
	}


	public Review getReviewById(int id) throws ResourceNotFoundException {
		
		return reviewRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No review record with the given id...!!!"));
	}


	public List<Review> getAllReview() {
		return reviewRepository.findAll();
	}


	public Review updateReview(Review review) {
		Review previousReview = reviewRepository.getById(review.getId());
		if(review.getCustomer()!=null) {
			previousReview.setCustomer(review.getCustomer());
		}
		if(review.getProduct()!=null) {
			previousReview.setProduct(review.getProduct());
		}
		if(review.getRating()!=previousReview.getRating()) {
			previousReview.setRating(review.getRating());
		}
		
		return null;
	}

}
