package com.hospitalManagementSystem.HospitalManagementSystem;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.ResourceExistsException;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(exception = ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundexception(ResourceNotFoundException e){
		Map<String,String> map=new HashMap<>();
		map.put("Resource Not Found Exception :", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		
	}
	
	
	
	@ExceptionHandler(exception=ResourceExistsException.class)
	ResponseEntity<?> resourceExistsExceptionHandler(ResourceExistsException e){
		
		Map<String,String> map=new HashMap<>();
		map.put("Resource exists exception :", e.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		
		
	}
	
	
	
	@ExceptionHandler(exception = InvalidInputException.class)
	public ResponseEntity<?> invalidInputExceptionHandler(InvalidInputException e){
		Map<String ,String> map =new HashMap<>();
		map.put("Invalid Input Exception: ", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(map);
		
	}
	
	
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<?> handleException(Exception e){
		Map<String,String> map=new HashMap<>();
		map.put(" Unknown Exception :", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		
	}

}
