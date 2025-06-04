package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maverickbank.MaverickBank.model.LoanClosureRequest;

public interface LoanClosureRequestRepository extends JpaRepository<LoanClosureRequest, Integer> {

}
