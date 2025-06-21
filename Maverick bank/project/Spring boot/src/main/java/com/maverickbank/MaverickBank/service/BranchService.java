package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.BranchRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.BranchValidation;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class BranchService {
	private BranchRepository branchRepository;
	private UserRepository userRepository;
	
	
	
	public BranchService(BranchRepository branchRepository, UserRepository userRepository) {
		this.branchRepository = branchRepository;
		this.userRepository = userRepository;
	}

	
	
	
//-------------------------------------- ADD ----------------------------------------------------------------------------
	/**Validates and adds Branch to database
	 * @param branch
	 * @param principal 
	 * @return
	 * @throws InvalidInputException
	 * @throws ResourceExistsException
	 * @throws Exception
	 */
	public Branch addBranch(Branch branch, Principal principal) throws InvalidInputException, ResourceExistsException , Exception{
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		//Validate the branch details
		BranchValidation.validateForNewBranch(branch);
		
		//Check weather any branch with given details exists
		if((branchRepository.getBranchByIfsc(branch.getIfsc())!=null) ||
				branchRepository.getByContactNumber(branch.getContactNumber())!=null||
				branchRepository.getByEmail(branch.getEmail())!=null ||
				branchRepository.getByName(branch.getBranchName())!=null) {
			throw new ResourceExistsException("Branch with the given details already exists...!!!");
			
		}
		//Set status to active and save
		
		branch.setStatus(ActiveStatus.ACTIVE);
		return branchRepository.save(branch);
	}

//--------------------------------------------------- GET ---------------------------------------------------------------

	public Branch getByName(String name,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		Branch branch=branchRepository.getByName(name);
		if(branch==null) {
			throw new ResourceNotFoundException("No branch record with the given name...!!!");
		}
		
		return branch;
	}



	public List<Branch> getAll(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		List <Branch> branchList=branchRepository.findAll();
		if(branchList==null) {
			throw new ResourceNotFoundException("No branch records...!!!");
		}
				
		
		return branchList;
	}



	public List<Branch> getByState(String state,Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				
				List <Branch> branchList=branchRepository.getByState(state);
				if(branchList==null) {
					throw new ResourceNotFoundException("No branches in the given state...!!!");
				}
		
		return branchList;
	}

	public Branch getByIfsc(String ifsc, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		Branch branch=branchRepository.getBranchByIfsc(ifsc);
		if(branch==null) {
			throw new ResourceNotFoundException("No branche with the given ifsc code...!!!");
		}

			return branch;
	}
	

	
	

	


	public Branch getById(int id,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		return branchRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No branch record with given Id...!!!"));
	}

	
	public List<Branch> getInactiveBranches(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		List <Branch> branchList=branchRepository.getByStatus(ActiveStatus.INACTIVE);
		if(branchList==null) {
			throw new ResourceNotFoundException("No INACTIVE branches found...!!!");
		}

		return branchList;
	}




	public List<Branch> getActiveBranches(Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		List <Branch> branchList=branchRepository.getByStatus(ActiveStatus.ACTIVE);
		if(branchList==null) {
			throw new ResourceNotFoundException("No ACTIVE branches found...!!!");
		}

		return branchList;
	}
	
	
	public List<Branch> getActiveBranchesByState(String state, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		List <Branch> branchList=branchRepository.getByStateAndStatus( state,ActiveStatus.ACTIVE);
		if(branchList==null) {
			throw new ResourceNotFoundException("No ACTIVE branches found in the given state...!!!");
		}

		return branchList;
	}




	public List<Branch> getgetInactiveBranchesByState(String state, Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		List <Branch> branchList=branchRepository.getByStateAndStatus( state,ActiveStatus.INACTIVE);
		if(branchList==null) {
			throw new ResourceNotFoundException("No INACTIVE branches found in the given state...!!!");
		}

		return branchList;
	}
	
	
//-------------------------------------------- PUT ----------------------------------------------------------------------

	public Branch deactivateBranch(int id,Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		Branch branch=branchRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No branch record with given Id...!!!"));
		branch.setStatus(ActiveStatus.INACTIVE);
		
		return branchRepository.save(branch);
	}



	public Branch activateBranch(int id,Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		Branch branch=branchRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No branch record with given Id...!!!"));
		branch.setStatus(ActiveStatus.ACTIVE);
		
		return branchRepository.save(branch);
	}




	public Branch updateBranchContactNumber(int id, String contactNumber, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		//Check user is active
				User currentUser= userRepository.getByUsername(principal.getName());
				UserValidation.checkActiveStatus(currentUser.getStatus());
				
				BranchValidation.validateContactNumber(contactNumber);
				
				Branch branch=branchRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No branch record with given Id...!!!"));
				branch.setContactNumber(contactNumber);
		return branchRepository.save(branch);
	}




	public Branch updateEmail(int id, String email, Principal principal) throws InvalidInputException, ResourceNotFoundException, InvalidActionException, DeletedUserException {
		//Check user is active
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		
		BranchValidation.validateEmail(email);
		
		Branch branch=branchRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No branch record with given Id...!!!"));
		branch.setEmail(email);
		return branchRepository.save(branch);
	}




	public Branch updateBranch(Branch branch, Principal principal) throws InvalidInputException, InvalidActionException, DeletedUserException, ResourceNotFoundException {
		User currentUser= userRepository.getByUsername(principal.getName());
		UserValidation.checkActiveStatus(currentUser.getStatus());
		if(branch.getId()<=0) {
			throw new InvalidInputException("Invalid branch id...!!!");
		}
		BranchValidation.validateForNewBranch(branch);
		Branch currentBranch=branchRepository.findById(branch.getId()).orElseThrow(()-> new ResourceNotFoundException("No branch record with given Id...!!!"));
		currentBranch.setBranchName(branch.getBranchName());
		currentBranch.setIfsc(branch.getIfsc());
		currentBranch.setContactNumber(branch.getContactNumber());
		currentBranch.setEmail(branch.getEmail());
		currentBranch.setAddress(branch.getAddress());
		return branchRepository.save(currentBranch);
	}




	




	
	
	




	

}
