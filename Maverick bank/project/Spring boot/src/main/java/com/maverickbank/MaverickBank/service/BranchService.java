package com.maverickbank.MaverickBank.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.repository.BranchRepository;
import com.maverickbank.MaverickBank.repository.EmployeeRepository;
import com.maverickbank.MaverickBank.validation.BranchValidation;

@Service
public class BranchService {
	BranchRepository br;
	EmployeeRepository er;
	
	
	public BranchService(BranchRepository br) {
		this.br=br;
	}
	
	
	
	public Branch getByName(String name) {
		return br.getByName(name);
	}



	public List<Branch> getAll() {
		
		return br.findAll();
	}



	public List<Branch> getByState(String state) {
		
		return br.getByState(state);
	}


	
	

	/**Validates and adds Branch to database
	 * @param branch
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceExistsException
	 * @throws Exception
	 */
	public Branch addBranch(Branch branch) throws InvalidInputException, ResourceExistsException , Exception{
		
		//Check whether the given branch object is null
		if(branch==null) {
			throw new InvalidInputException("Null branch provided..!!!");
		}
		//Validate the branch details
		BranchValidation.validateForNewBranch(branch);
		//Check weather any branch with given details exists
		if((br.getBranchByIfsc(branch.getIfsc())!=null) ||
				br.getByContactNumber(branch.getContactNumber())!=null||
				br.getByEmail(branch.getEmail())!=null ||
				br.getByName(branch.getBranchName())!=null) {
			throw new ResourceExistsException("Branch with the given details already exists...!!!");
			
		}
		//Set status to active and save
		
		branch.setStatus(ActiveStatus.ACTIVE);
		return br.save(branch);
	}



	public Branch getById(int id) throws ResourceNotFoundException {
		
		return br.findById(id).orElseThrow(()-> new ResourceNotFoundException("No branch record with given Id...!!!"));
	}



	public Branch deactivateBranch(int id) throws ResourceNotFoundException, InvalidInputException {
		Branch branch=br.findById(id).orElseThrow(()-> new ResourceNotFoundException("No branch record with given Id...!!!"));
		branch.setStatus(ActiveStatus.INACTIVE);
		
		return br.save(branch);
	}



	public Branch activateBranch(int id) throws InvalidInputException, ResourceNotFoundException {
		Branch branch=br.findById(id).orElseThrow(()-> new ResourceNotFoundException("No branch record with given Id...!!!"));
		branch.setStatus(ActiveStatus.ACTIVE);
		
		return br.save(branch);
	}
	

}
