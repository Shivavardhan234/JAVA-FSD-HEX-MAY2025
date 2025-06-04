package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maverickbank.MaverickBank.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

}
