package com.mockAssessment.Ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mockAssessment.Ecom.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
