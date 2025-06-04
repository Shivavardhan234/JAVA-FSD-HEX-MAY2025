package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maverickbank.MaverickBank.model.AccountRequest;

public interface AccountRequestRepository extends JpaRepository<AccountRequest, Integer> {

}
