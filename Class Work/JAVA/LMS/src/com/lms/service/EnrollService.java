package com.lms.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

import com.lms.dao.impl.*;
import com.lms.exception.InvalidIdException;
import com.lms.dao.*;
import com.lms.model.Learner;
import com.lms.utility.Coupon;
import com.lms.model.Course;
import com.lms.model.Enroll;
public class EnrollService {
	
	CourseService cs = new CourseService();
	LearnerService ls = new LearnerService();
	EnrollDao eDao =new EnrollDaoImpl();
	
	public void addEnrollment( int learner_id,int course_id,Scanner sc) throws InvalidIdException {
		
		
		
		int id =(int)(Math.random()*10000000);
		LocalDate dateOfEnroll = LocalDate.now();
		System.out.println("Do you have any coupon? Y/N");
		String coupons = sc.next();
		Coupon couponsUsed=null;
		BigDecimal feePaid;
		Learner learner =ls.getById(learner_id);
		Course course = cs.getCourseById(course_id);
		BigDecimal totalFee=course.getFee();
		BigDecimal discount =BigDecimal.ZERO;
		if(coupons.equals("Y")) {
			System.out.println("Enter Coupon code");
			try {
			couponsUsed=Coupon.valueOf(sc.next().toUpperCase());
			
			discount=BigDecimal.valueOf(couponsUsed.getDiscount()/100);
			System.out.println("Discount: "+ discount);
			
			
			}
			catch(Exception e) {
				System.out.println("No coupon with the code available");
			}
			
		}
		
		feePaid = totalFee.subtract(totalFee.multiply(discount));
		
		Enroll enroll =new Enroll (id, dateOfEnroll, feePaid, couponsUsed, learner, course);
		eDao.addEnrollment(enroll);
		
		
	}

}
