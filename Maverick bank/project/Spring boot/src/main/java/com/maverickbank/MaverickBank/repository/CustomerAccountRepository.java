package com.maverickbank.MaverickBank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.CustomerAccount;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Integer> {

	
	@Query("SELECT c FROM CustomerAccount c WHERE c.customer.id = ?1")
    List<CustomerAccount> getByCustomerId(int id);

    @Query("SELECT c FROM CustomerAccount c WHERE c.accountHolder.id = ?1")
    List<CustomerAccount> getByAccountHolderId(int id);

    @Query("SELECT c FROM CustomerAccount c WHERE c.account.id = ?1")
    List<CustomerAccount> getByAccountId(int id);

    @Query("SELECT c FROM CustomerAccount c WHERE c.customer.id = ?1AND c.account.id=?2")
	CustomerAccount getByCustomerIdAndAccountId(int customerId, int accountId);
}
