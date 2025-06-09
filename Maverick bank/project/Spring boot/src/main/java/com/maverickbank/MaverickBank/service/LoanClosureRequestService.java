package com.maverickbank.MaverickBank.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.LoanClosureRequest;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.LoanClosureRequestRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.validation.UserValidation;

@Service
public class LoanClosureRequestService {
	
	private LoanClosureRequestRepository  loanClosureRequestRepository;
	private UserRepository userRepository;
	private LoanService loanService;
	public LoanClosureRequestService(LoanClosureRequestRepository loanClosureRequestRepository,
			UserRepository userRepository, LoanService loanService) {
		super();
		this.loanClosureRequestRepository = loanClosureRequestRepository;
		this.userRepository = userRepository;
		this.loanService = loanService;
	}
	
	
	
//------------------------------------------------ POST -----------------------------------------------------------------
	public LoanClosureRequest createLoanClosureRequest(int loanId, String purpose, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    Loan loan = loanService.getLoanById(loanId, principal);
	    
	    if(loan.isCleared()==false) {
	    	throw new InvalidActionException("You have pending bills to pay.You cannot close the loan...!!!");
	    }

	    
	    if (!loanClosureRequestRepository.getByLoanIdAndRequestStatus(loanId, ApplicationStatus.PENDING).isEmpty()||
	    		loanClosureRequestRepository.getByLoanIdAndRequestStatus(loanId, ApplicationStatus.PENDING)==null) {
	        throw new InvalidActionException("You already have a pending closure request for this loan...!!!");
	    }

	    LoanClosureRequest request = new LoanClosureRequest();
	    request.setLoan(loan);
	    request.setPurpose(purpose);
	    request.setRequestStatus(ApplicationStatus.PENDING);

	    return loanClosureRequestRepository.save(request);
	}
	
	
//------------------------------------------ GET --------------------------------------------------------------------------

	/**
	 * @param principal
	 * @return
	 * @throws DeletedUserException
	 * @throws InvalidActionException 
	 * @throws InvalidInputException 
	 */
	public List<LoanClosureRequest> getAllRequests(Principal principal) throws DeletedUserException, InvalidInputException, InvalidActionException {
        User currentUser = userRepository.getByUsername(principal.getName());
        UserValidation.checkActiveStatus(currentUser.getStatus());

        return loanClosureRequestRepository.findAll();
    }
	
	
	

    
    /**
     * @param id
     * @param principal
     * @return
     * @throws ResourceNotFoundException
     * @throws DeletedUserException
     * @throws InvalidActionException 
     * @throws InvalidInputException 
     */
    public LoanClosureRequest getRequestById(int id, Principal principal)throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
        User currentUser = userRepository.getByUsername(principal.getName());
        UserValidation.checkActiveStatus(currentUser.getStatus());

        return loanClosureRequestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No closure request found with the provided ID...!!!"));
    }

    
    /**
     * @param loanId
     * @param principal
     * @return
     * @throws ResourceNotFoundException
     * @throws DeletedUserException
     * @throws InvalidActionException 
     * @throws InvalidInputException 
     */
    public List<LoanClosureRequest> getByLoanId(int loanId, Principal principal)
            throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
        User currentUser = userRepository.getByUsername(principal.getName());
        UserValidation.checkActiveStatus(currentUser.getStatus());

        

        List<LoanClosureRequest> loanClosureRequestList = loanClosureRequestRepository.findByLoanId(loanId);
        if (loanClosureRequestList.isEmpty()) {
            throw new ResourceNotFoundException("No closure requests found for the given loan ID...!!!");
        }
        return loanClosureRequestList;
    }



	/**
	 * @param status
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 * @throws DeletedUserException
	 */
	public List<LoanClosureRequest> getByStatus(ApplicationStatus status, Principal principal) throws ResourceNotFoundException, InvalidInputException, InvalidActionException, DeletedUserException {
		User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());


	    List<LoanClosureRequest> loanClosureRequestList = loanClosureRequestRepository.getByRequestStatus(status);
	    if (loanClosureRequestList.isEmpty()) {
	        throw new ResourceNotFoundException("No loan closure requests found for the given status...!!!");
	    }

	    return loanClosureRequestList;
	}
	
	
	/**
	 * @param loanId
	 * @param status
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidInputException
	 * @throws InvalidActionException
	 */
	public List<LoanClosureRequest> getByLoanIdAndStatus(int loanId, ApplicationStatus status, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidInputException, InvalidActionException {
	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());


	    List<LoanClosureRequest> loanClosureRequestList = loanClosureRequestRepository.getByLoanIdAndRequestStatus(loanId, status);
	    if (loanClosureRequestList.isEmpty()) {
	        throw new ResourceNotFoundException("No loan closure requests found for the given loan ID and status...!!");
	    }

	    return loanClosureRequestList;
	}



	
	
	
//---------------------------------------------- PUT ---------------------------------------------------------------------
	
	/**
	 * @param id
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidActionException
	 * @throws InvalidInputException
	 */
	public LoanClosureRequest rejectLoanClosureRequest(int id, Principal principal) throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    LoanClosureRequest loanClosureRequest = loanClosureRequestRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No loan closure request found with id...!!!"));

	    if (loanClosureRequest.getRequestStatus() == ApplicationStatus.REJECTED) {
	        throw new InvalidActionException("This closure request is already REJECTED...!!!");
	    }
	    if (loanClosureRequest.getRequestStatus() == ApplicationStatus.ACCEPTED) {
	        throw new InvalidActionException("This closure request is already ACCEPTED. Cannot reject...!!!");
	    }

	    loanClosureRequest.setRequestStatus(ApplicationStatus.REJECTED);
	    return loanClosureRequestRepository.save(loanClosureRequest);
	}
	
	
	
	
	/**
	 * @param id
	 * @param principal
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws DeletedUserException
	 * @throws InvalidActionException
	 * @throws InvalidInputException
	 */
	public LoanClosureRequest acceptLoanClosureRequest(int id, Principal principal)
	        throws ResourceNotFoundException, DeletedUserException, InvalidActionException, InvalidInputException {

	    User currentUser = userRepository.getByUsername(principal.getName());
	    UserValidation.checkActiveStatus(currentUser.getStatus());

	    LoanClosureRequest loanClosureRequest = loanClosureRequestRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No loan closure request found with id...!!!"));

	    if (loanClosureRequest.getRequestStatus() == ApplicationStatus.ACCEPTED) {
	        throw new InvalidActionException("This closure request is already ACCEPTED...!!!");
	    }
	    if (loanClosureRequest.getRequestStatus() == ApplicationStatus.REJECTED) {
	        throw new InvalidActionException("This closure request is already REJECTED. Cannot accept...!!");
	    }
	    
	    loanClosureRequest.setRequestStatus(ApplicationStatus.ACCEPTED);
	    loanClosureRequest = loanClosureRequestRepository.save(loanClosureRequest);

	    
	    loanService.closeLoan(loanClosureRequest.getLoan().getId(), principal);

	    return loanClosureRequest;
	}

	
	
	
}
