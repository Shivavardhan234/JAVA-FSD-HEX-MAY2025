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
	private BranchService branchService;
	
//------------------------------------------- POST ------------------------------------------------------------------------
	@PostMapping("/add")
	@CrossOrigin(origins = "http://localhost:5173")
	public Branch addBranch(@RequestBody  Branch branch,Principal principal) throws InvalidInputException, ResourceExistsException, Exception {
		return branchService.addBranch(branch,principal);
		
	}
	
	
	
//--------------------------------------- GET ----------------------------------------------------------------------------	
	@GetMapping("/get/by-id/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Branch getById(@PathVariable int id,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.getById(id, principal);
	}
	
	
	
	@GetMapping("/get/all")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Branch> getAll(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return branchService.getAll(page,size, principal);
		
	}
	
	@GetMapping("/get/by-name")
	@CrossOrigin(origins = "http://localhost:5173")
	public Branch getByName(@RequestParam String name,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		return branchService.getByName(name, principal);
		
	}
	@GetMapping("/get/by-ifsc")
	@CrossOrigin(origins = "http://localhost:5173")
	public Branch getByIfsc(@RequestParam String ifsc,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		return branchService.getByIfsc(ifsc, principal);
		
	}
	
	
	@GetMapping("/get/by-state")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Branch> getByState(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@RequestParam String state,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return branchService.getByState(page,size,state, principal);
	}
	@GetMapping("/get/active-by-state")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Branch> getActiveBranchesByState(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@RequestParam String state,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return branchService.getActiveBranchesByState(page,size,state, principal);
	}
	
	
	@GetMapping("/get/inactive-by-state")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Branch> getInactiveBranchesByState(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,@RequestParam String state,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return branchService.getgetInactiveBranchesByState(page,size,state, principal);
	}
	
	@GetMapping("/get/inactive")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Branch> getInactiveBranches(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return branchService.getInactiveBranches (page,size,principal);
	}
	@GetMapping("/get/active")
	@CrossOrigin(origins = "http://localhost:5173")
	public List<Branch> getActiveBranches(@RequestParam (name="page",required = false,defaultValue = "0") Integer page,
			@RequestParam(name="size",required = false, defaultValue = "100000") Integer size,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException{
		return branchService.getActiveBranches(page,size, principal);
	}
	
	
	
	//-------------------------------------- PUT ------------------------------------------------------------------------

	
	@PutMapping("/deactivate/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Branch deactivateBranch(@PathVariable int id,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.deactivateBranch(id, principal);
	}
	
	
	@PutMapping("/activate/{id}")
	@CrossOrigin(origins = "http://localhost:5173")
	public Branch activateBranch(@PathVariable int id,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.activateBranch(id, principal);
	}
	
	@PutMapping("/update/contact-number/{id}")
	public Branch updateBranchContactNumber(@PathVariable int id,@RequestParam String contactNumber,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.updateBranchContactNumber(id,contactNumber, principal);
	}
	
	@PutMapping("/update/email/{id}")
	public Branch updateBranchEmail(@PathVariable int id,@RequestParam String email,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.updateEmail(id,email, principal);
	}
	@PutMapping("/update")
	@CrossOrigin(origins = "http://localhost:5173")
	public Branch updateBranch(@RequestBody Branch branch, Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		return branchService.updateBranch(branch, principal);
	}
	
	
	
}
