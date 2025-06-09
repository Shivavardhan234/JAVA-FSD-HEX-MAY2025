package com.maverickbank.MaverickBank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.AccountHolder;

public interface AccountHolderRepository extends JpaRepository<AccountHolder, Integer>{
	
	@Query("SELECT a FROM AccountHolder a WHERE a.contactNumber=?1")
	List<AccountHolder> getByContactNumber(String contactNumber);
	
	
	@Query("SELECT a FROM AccountHolder a WHERE a.email=?1")
	List<AccountHolder> getByEmail(String email);
	
	
	@Query("SELECT a FROM AccountHolder a WHERE a.panCardNumber=?1")
	List<AccountHolder> getByPancardNumber(String panCardNumber);
	
	@Query("SELECT c FROM AccountHolder c WHERE c.aadharNumber=?1")
	List<AccountHolder> getByAadharNumber(String aadharNumber);

}
