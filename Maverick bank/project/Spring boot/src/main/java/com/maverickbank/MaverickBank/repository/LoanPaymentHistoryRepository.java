package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maverickbank.MaverickBank.model.LoanPaymentHistory;

public interface LoanPaymentHistoryRepository extends JpaRepository<LoanPaymentHistory, Integer> {

}
