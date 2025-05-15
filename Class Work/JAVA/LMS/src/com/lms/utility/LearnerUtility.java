package com.lms.utility;

import java.util.*;
import com.lms.model.*;
public class LearnerUtility {
	private static List<Learner> list=new ArrayList<>() ;
	
	public LearnerUtility() {
		if (list==null || list.isEmpty()) {
			list = generateSampleData();
		}
		
	}
	
	
	//-------------------------------get the data-----------------------------------------------------------
	public List<Learner> getSampleData(){
		
		return list;
		
	}
	
	
	//-------------------------if data isnt present----------------------------------------------------------
	private static List<Learner> generateSampleData(){
		Learner l1= new Learner();
		Learner l2= new Learner();
		Learner l3= new Learner();
		Learner l4= new Learner();
		Learner l5= new Learner();
		
//		l1.setId((int)(Math.random()* 10000000));
//		l2.setId((int)(Math.random()* 10000000));
//		l3.setId((int)(Math.random()* 10000000));
//		l4.setId((int)(Math.random()* 10000000));
//		l5.setId((int)(Math.random()* 10000000))
		l1.setId(1);
		l2.setId(2);
		l3.setId(3);
		l4.setId(4);
		l5.setId(5);
		
		l1.setName("shiva vardhan");
		l2.setName("sankeerthan");
		l3.setName("aditya varma");
		l4.setName("ching chang chin");
		l5.setName("Monkey D luffy");
		
		l1.setEmail("shiva_cake@gmail.com");
		l2.setEmail("sankeerthan.saketh@gmail.com");
		l3.setEmail("yours.aditya@yahoo.com");
		l4.setEmail("ching.chang.chin@gmail.com");
		l5.setEmail("Monkey.D.luffy@outlook.com");
		
		
		return Arrays.asList(l1,l2,l3,l4,l5);
		
	}
	
	
	
	public static void setList(List<Learner> list) {
		
		LearnerUtility.list=list;
	}
	
	
	

}
