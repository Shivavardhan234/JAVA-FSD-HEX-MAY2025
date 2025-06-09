package com.maverickbank.MaverickBank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.CustomerAccountOpeningApplication;

public interface CustomerAccountOpeningApplicationRepository extends JpaRepository<CustomerAccountOpeningApplication, Integer> {

	
	
	@Query("SELECT c FROM CustomerAccountOpeningApplication c WHERE c.customer.id=?1")
	List<CustomerAccountOpeningApplication> getByCustomerId(int id);
	
	@Query("SELECT c FROM CustomerAccountOpeningApplication c WHERE c.accountHolder.id = ?1")
    List<CustomerAccountOpeningApplication> getByAccountHolderId(int id);

    @Query("SELECT c FROM CustomerAccountOpeningApplication c WHERE c.accountOpeningApplication.id = ?1")
    List<CustomerAccountOpeningApplication> getByAccountOpeningApplicationId(int id);

}
