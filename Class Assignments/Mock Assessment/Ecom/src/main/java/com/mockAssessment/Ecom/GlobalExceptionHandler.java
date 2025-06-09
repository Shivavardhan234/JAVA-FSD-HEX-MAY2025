package com.mockAssessment.Ecom;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mockAssessment.Ecom.exception.InvalidInputException;
import com.mockAssessment.Ecom.exception.ResourceExistsException;
import com.mockAssessment.Ecom.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(exception= ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundExceptionHandler(ResourceNotFoundException e){
		
		Map<String,String> map =new HashMap<>();
		map.put("Resource Not Found Exception: ", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		
	}
	@ExceptionHandler(exception= ResourceExistsException.class)
	public ResponseEntity<?> resourceExistsExceptionHandler(ResourceExistsException e){
		
		Map<String,String> map =new HashMap<>();
		map.put("Resource Exists Exception: ", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(map);
		
	}
	@ExceptionHandler(exception= InvalidInputException.class)
	public ResponseEntity<?> invalidInputExceptionHandler(InvalidInputException e){
		
		Map<String,String> map =new HashMap<>();
		map.put("Invalid Input Exception: ", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(map);
		
	}
	@ExceptionHandler(exception= Exception.class)
	public ResponseEntity<?> unknownExceptionHandler(Exception e){
		
		Map<String,String> map =new HashMap<>();
		map.put("Unknown Exception: ", e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
		
	}

}
