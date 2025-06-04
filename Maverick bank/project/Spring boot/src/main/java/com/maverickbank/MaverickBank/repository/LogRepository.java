package com.maverickbank.MaverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maverickbank.MaverickBank.model.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {

}
