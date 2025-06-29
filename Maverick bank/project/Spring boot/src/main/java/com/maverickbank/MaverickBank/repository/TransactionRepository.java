package com.maverickbank.MaverickBank.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
	
	 	@Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN ?1 AND ?2")
	    List<Transaction> findByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

	    @Query("SELECT t FROM Transaction t WHERE (t.fromDetails = ?1 OR t.toDetails = ?1) AND t.transactionDate BETWEEN ?2 AND ?3")
	    List<Transaction> findByAccountNumberAndDateRange(String accountNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);

	    @Query("SELECT t FROM Transaction t WHERE t.toDetails = ?1 AND t.transactionDate BETWEEN ?2 AND ?3")
	    List<Transaction> findCredits(String accountNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);

	    @Query("SELECT t FROM Transaction t WHERE t.fromDetails = ?1 AND t.transactionDate BETWEEN ?2 AND ?3")
	    List<Transaction> findDebits(String accountNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);

	    @Query("SELECT t FROM Transaction t WHERE t.fromDetails = ?1 OR t.toDetails = ?1 ORDER BY t.transactionDate DESC, t.transactionTime DESC")
	    List<Transaction> findAllByAccountNumber(String accountNumber, Pageable pageable);

	    
	    @Query("SELECT sum(t.transactionAmount) FROM Transaction t ")
		BigDecimal getTotalTransactionAmount();

	    
	    @Query("SELECT count(t) FROM Transaction t")
		int getTotalNumberOfTransactions();

	    

	    

}
