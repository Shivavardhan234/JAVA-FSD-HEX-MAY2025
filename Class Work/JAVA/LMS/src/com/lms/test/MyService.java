package com.lms.test;

public class MyService {

	
	public double calculatePercent(int totalMarks, int marksScored)  {
		if(marksScored>totalMarks){
			throw new RuntimeException("marks scored should not be greater than total marks");
		}
		else if(marksScored<0||totalMarks<0){
			throw new RuntimeException("marks should not be less than 0");
		}
		return ((double)marksScored/totalMarks)*100;
		
	}
	
	
	
	
	
	
	
}
