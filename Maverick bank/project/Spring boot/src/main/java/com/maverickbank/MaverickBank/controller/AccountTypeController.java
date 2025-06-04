package com.maverickbank.MaverickBank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.service.AccountTypeService;

@RestController
@RequestMapping("/api/account-type")
public class AccountTypeController {
	@Autowired
	AccountTypeService ats;
	
	@GetMapping("/get-all")
	public List<AccountType> getAllAccountType(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
											   @RequestParam(name="size",required = false, defaultValue = "100000") Integer size){
		return ats.getAllAccountType(page, size);
	}
	
	@GetMapping("/get-by-name/{type}")
	public AccountType getAccountTypeByName(@PathVariable BankAccountType type){
		return ats.getAccountTypeByName(type);
	}

}
