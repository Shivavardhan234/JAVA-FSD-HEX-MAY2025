package com.maverickbank.MaverickBank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.service.BranchService;

@RestController
@RequestMapping("/api/branch")
public class BranchController {
	@Autowired
	BranchService bs;
	
	
	@PostMapping("add")
	public Branch addBranch(@RequestBody  Branch branch) throws InvalidInputException, ResourceExistsException, Exception {
		return bs.addBranch(branch);
		
	}
	
	
	
	
	@GetMapping("/get/by-id/{id}")
	public Branch getById(@PathVariable int id) throws ResourceNotFoundException {
		return bs.getById(id);
	}
	
	
	@PutMapping("/deactivate/{id}")
	public Branch deactivateBranch(@PathVariable int id) throws ResourceNotFoundException, InvalidInputException {
		return bs.deactivateBranch(id);
	}
	
	
	@PutMapping("/activate/{id}")
	public Branch activateBranch(@PathVariable int id) throws ResourceNotFoundException, InvalidInputException {
		return bs.activateBranch(id);
	}
	
	
	
	@GetMapping("/get/all")
	public List<Branch> getAll(){
		return bs.getAll();
		
	}
	
	@GetMapping("/get/by-name/{name}")
	public Branch getByName(@PathVariable String name) {
		return bs.getByName(name);
		
	}
	
	
	@GetMapping("/get/by-state/{state}")
	public List<Branch> getByState(@PathVariable String state){
		return bs.getByState(state);
	}

}
