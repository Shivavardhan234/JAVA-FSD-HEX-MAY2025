package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maverickbank.MaverickBank.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

}
