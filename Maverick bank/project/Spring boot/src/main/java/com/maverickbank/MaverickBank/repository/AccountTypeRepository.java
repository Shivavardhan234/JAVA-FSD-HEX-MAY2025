package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.model.AccountType;

public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {

	
	@Query("SELECT a FROM AccountType a Where a.accountType=?1 ")
	AccountType getAccountTypeByName(BankAccountType type);
}
