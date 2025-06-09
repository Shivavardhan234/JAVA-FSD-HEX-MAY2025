package com.maverickbank.MaverickBank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.RequestType;
import com.maverickbank.MaverickBank.model.AccountRequest;

public interface AccountRequestRepository extends JpaRepository<AccountRequest, Integer> {
	
	
	@Query("SELECT r FROM AccountRequest r WHERE r.account.id = ?1 AND r.requestType = ?2 AND r.requestStatus = ?3")
	AccountRequest getPreviousRequest(int accountId,RequestType requestType,ApplicationStatus requestStatus);
	
	 @Query("SELECT r FROM AccountRequest r WHERE r.account.id = ?1")
	    List<AccountRequest> findByAccountId(int accountId);

	    @Query("SELECT r FROM AccountRequest r WHERE r.requestStatus = ?1")
	    List<AccountRequest> findByRequestStatus(ApplicationStatus requestStatus);


	    @Query("SELECT r FROM AccountRequest r WHERE r.account.id = ?1 AND r.requestStatus = ?2")
	    List<AccountRequest> findByAccountIdAndStatus(int accountId, ApplicationStatus requestStatus);

}
