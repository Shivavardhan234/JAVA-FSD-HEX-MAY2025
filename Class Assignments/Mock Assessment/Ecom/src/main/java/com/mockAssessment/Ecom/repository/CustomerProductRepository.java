package com.mockAssessment.Ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mockAssessment.Ecom.model.CustomerProduct;

public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Integer>{

	@Query("SELECT c FROM CustomerProduct c WHERE c.customer.id=?1AND c.product.id=?2")
	List<CustomerProduct> getCustomerProductByDetails(int customerId, int productId);
}
