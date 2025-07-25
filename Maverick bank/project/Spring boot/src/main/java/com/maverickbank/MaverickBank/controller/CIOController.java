package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.users.CIO;
import com.maverickbank.MaverickBank.service.CIOService;

@RestController
@RequestMapping("/api/cio")
public class CIOController {
	@Autowired
	private CIOService cioService;
	
//---------------------------------------- POST -------------------------------------------------------------------------
	@PostMapping("/add")
	@CrossOrigin(origins = "http://localhost:5173")
	public CIO  addAdmin(@RequestBody CIO admin) throws InvalidInputException, ResourceExistsException  {
		return cioService.addAdmin(admin);
	}

	
//----------------------------------------- GET -------------------------------------------------------------------------
	@GetMapping("/get/all")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<CIO> getAllCIO(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		return cioService.getAllCIO(page,size,principal);
	}
	@GetMapping("/get/by-status/{status}")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<CIO> getByStatus(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@PathVariable ActiveStatus status, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		return cioService.getByStatus(page,size,status,principal);
	}
	
	
	@GetMapping("/get/by-id/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public CIO getById(@PathVariable int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		return cioService.getById(id,principal);
	}
	
	@GetMapping("/get/by-user-id/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public CIO getByUserId(@PathVariable int id, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		return cioService.getByUserId(id,principal);
	}
	
//-------------------------------------- PUT -----------------------------------------------------------------------------
	@PutMapping("/update")
	@CrossOrigin(origins = "http://localhost:5173")
	public CIO updateCIO(@RequestBody CIO cio, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException {
		return cioService.updateCIO(cio,principal);
	}
	
}
