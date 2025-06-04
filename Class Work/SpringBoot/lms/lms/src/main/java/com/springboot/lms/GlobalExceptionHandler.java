package com.springboot.lms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springboot.lms.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(exception = ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundexception(ResourceNotFoundException e){
		Map<String,String> map=new HashMap<>();
		map.put("Resource Not Found Exception message", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		
	}
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<?> handleException(Exception e){
		Map<String,String> map=new HashMap<>();
		map.put(" Exception message", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		
	}

}
