package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maverickbank.MaverickBank.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{

}
