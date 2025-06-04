package com.maverickbank.MaverickBank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.model.users.CIO;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.service.CIOService;

@RestController
@RequestMapping("/api/cio")
public class CIOController {
	@Autowired
	CIOService cios;
	
	@PostMapping("/add")
	public CIO  addAdmin(@RequestBody CIO admin) throws InvalidInputException,ResourceExistsException, Exception {
		return cios.addAdmin(admin);
	}

	
	@GetMapping("/get-all")
	public List<CIO> getAllCIO() throws Exception{
		return cios.getAllCIO();
	}
}
