package com.maverickbank.MaverickBank.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.AccountStatus;
import com.maverickbank.MaverickBank.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{
	
	
	
	@Query("SELECT a FROM Account a WHERE a.accountNumber=?1 ")
	Account getByAccountNumber(String accountNumber);
	

	@Query("SELECT a FROM Account a WHERE a.branch.id = ?1")
	List<Account> getByBranchId(int id, Pageable pageable);


	@Query("SELECT a FROM Account a WHERE a.accountStatus = ?1")
	List<Account> getByAccountStatus(AccountStatus accountStatus, Pageable pageable);
	
	@Query("SELECT a FROM Account a WHERE a.accountType.id = ?1")
	List<Account> getByAccountTypeId(int id, Pageable pageable);

	@Query("SELECT count(a) FROM Account a WHERE a.kycCompliant=?1")
	int countByKycCompleted(boolean b);


	@Query("SELECT SUM(a.balance) FROM Account a")
	BigDecimal getTotalBankBalance();

}
