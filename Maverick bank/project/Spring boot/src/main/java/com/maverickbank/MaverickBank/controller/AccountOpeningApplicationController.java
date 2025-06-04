package com.maverickbank.MaverickBank.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.dto.AccountOpeningApplicationDto;
import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.service.AccountOpeningApplicationService;

@RestController
@RequestMapping("/api/account-opening-application")
public class AccountOpeningApplicationController {
	@Autowired
	AccountOpeningApplicationService aoas;
	
	@PostMapping("/add/{branchName}/{accountType}")
	public AccountOpeningApplicationDto addAccountOpeningApplication(@RequestBody AccountOpeningApplicationDto application, @PathVariable String branchName,@PathVariable BankAccountType accountType , Principal principal) throws InvalidInputException, Exception {
		String username=principal.getName();
		if(application.getApplication()==null) {
			throw new InvalidInputException("again null");
		}
		return aoas.addAccountOpeningApplication(application,branchName,accountType,username);
	}
	

}
