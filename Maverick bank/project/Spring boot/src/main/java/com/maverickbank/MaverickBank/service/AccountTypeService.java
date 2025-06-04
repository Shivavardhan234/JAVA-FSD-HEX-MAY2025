package com.maverickbank.MaverickBank.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.repository.AccountTypeRepository;

@Service
public class AccountTypeService {
	AccountTypeRepository atr;
	
	public AccountTypeService(AccountTypeRepository atr) {
		this.atr=atr;
	}
	
	
	
	public List<AccountType> getAllAccountType(Integer page, Integer size){
		Pageable pageable= PageRequest.of(page, size);
		return atr.findAll(pageable).getContent();
	}
	
	
	
	public AccountType getAccountTypeByName(BankAccountType type) {
		return atr.getAccountTypeByName(type);
	}

}
