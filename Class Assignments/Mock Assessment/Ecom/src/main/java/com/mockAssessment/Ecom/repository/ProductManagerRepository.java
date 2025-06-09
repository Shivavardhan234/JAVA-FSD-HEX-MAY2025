package com.mockAssessment.Ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.mockAssessment.Ecom.model.ProductManager;

public interface ProductManagerRepository extends JpaRepository<ProductManager, Integer> {
	
	@Query("SELECT c FROM ProductManager c WHERE c.user.username=?1")
	public ProductManager getProductManagerByUsername(String username);

}
