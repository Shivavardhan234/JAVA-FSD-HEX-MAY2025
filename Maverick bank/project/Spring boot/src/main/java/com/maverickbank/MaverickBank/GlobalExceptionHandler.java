package com.maverickbank.MaverickBank;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.maverickbank.MaverickBank.exception.DeletedUserException;
import com.maverickbank.MaverickBank.exception.IdentityNotMatchException;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidCredentialsException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.NotEnoughFundsException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(exception = InvalidInputException.class)
	public ResponseEntity<?> invalidInputExceptionHandler(InvalidInputException e){
		Map<String ,String> map =new HashMap<>();
		map.put("Invalid Input Exception: ", e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(map);
		
	}
	@ExceptionHandler(exception = IdentityNotMatchException.class)
	public ResponseEntity<?> identityNotMatchExceptionHandler(IdentityNotMatchException e){
		Map<String ,String> map =new HashMap<>();
		map.put("Identity not match exception: ", e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(map);
		
	}
	
	@ExceptionHandler(exception = ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundExceptionHandler(ResourceNotFoundException e){
		Map<String ,String> map =new HashMap<>();
		map.put("Resource Not Found Exception: ", e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(map);
		
	}
	
	
	
	@ExceptionHandler(exception = ResourceExistsException.class)
	public ResponseEntity<?> resourceExistsExceptionHandler(ResourceExistsException e){
		Map<String ,String> map =new HashMap<>();
		map.put("Resource already exists Exception: ", e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(map);
		
	}
	
	
	@ExceptionHandler(exception = InvalidCredentialsException.class)
	public ResponseEntity<?> invalidCredentialsExceptionHandler(InvalidCredentialsException e){
		Map<String,String> map=new HashMap<>();
		map.put("Invalid Credentials Exception: ", e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(map);
	}
	
	
	
	
	@ExceptionHandler(exception = InvalidActionException.class)
	public ResponseEntity<?> invalidActionExceptionHandler(InvalidActionException e){
		Map<String,String> map=new HashMap<>();
		map.put("Invalid Action Exception: ", e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
	}
	
	@ExceptionHandler(exception = DeletedUserException.class)
	public ResponseEntity<?> deletedUserExceptionHandler(DeletedUserException e){
		Map<String,String> map=new HashMap<>();
		map.put("Deleted User Exception: ", e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
	}
	@ExceptionHandler(exception = NotEnoughFundsException.class)
	public ResponseEntity<?> notEnoughFundsExceptionHandler(NotEnoughFundsException e){
		Map<String,String> map=new HashMap<>();
		map.put("Not Enough Funds Exception: ", e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
	}
	
	
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<?> exceptionHandler(Exception e){
		Map<String ,String> map =new HashMap<>();
		map.put("Unknown Exception: ", e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		
	}
	
	

}
