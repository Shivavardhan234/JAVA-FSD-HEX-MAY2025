package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maverickbank.MaverickBank.model.CustomerAccount;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Integer> {

}
