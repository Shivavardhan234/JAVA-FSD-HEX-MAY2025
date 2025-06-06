package com.maverickbank.MaverickBank.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.service.BranchService;

@RestController
@RequestMapping("/api/branch")
public class BranchController {
	@Autowired
	BranchService branchService;
	
//------------------------------------------- POST ------------------------------------------------------------------------
	@PostMapping("add")
	public Branch addBranch(@RequestBody  Branch branch,Principal principal) throws InvalidInputException, ResourceExistsException, Exception {
		return branchService.addBranch(branch,principal);
		
	}
	
	
	
//--------------------------------------- GET ----------------------------------------------------------------------------	
	@GetMapping("/get/by-id/{id}")
	public Branch getById(@PathVariable int id,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.getById(id, principal);
	}
	
	
	
	@GetMapping("/get/all")
	public List<Branch> getAll(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return branchService.getAll( principal);
		
	}
	
	@GetMapping("/get/by-name/{name}")
	public Branch getByName(@PathVariable String name,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		return branchService.getByName(name, principal);
		
	}
	
	
	@GetMapping("/get/by-state/{state}")
	public List<Branch> getByState(@PathVariable String state,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return branchService.getByState(state, principal);
	}
	
	//-------------------------------------- PUT ------------------------------------------------------------------------

	
	@PutMapping("/deactivate/{id}")
	public Branch deactivateBranch(@PathVariable int id,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.deactivateBranch(id, principal);
	}
	
	
	@PutMapping("/activate/{id}")
	public Branch activateBranch(@PathVariable int id,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.activateBranch(id, principal);
	}
	
	@PutMapping("/update/contact-number/{id}/{contactNumber}")
	public Branch updateBranchContactNumber(@PathVariable int id,@PathVariable String contactNumber,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.updateBranchContactNumber(id,contactNumber, principal);
	}
	
	@PutMapping("/update/email/{id}/{email}")
	public Branch updateBranchEmail(@PathVariable int id,@PathVariable String email,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.updateEmail(id,email, principal);
	}
	
	
	
}
