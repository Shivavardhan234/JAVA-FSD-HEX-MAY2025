package com.lms.controller;

import java.math.BigDecimal;
import java.util.*;

import com.lms.exception.InvalidIdException;
import com.lms.exception.InvalidInputException;
import com.lms.model.Course;
import com.lms.model.Learner;
import com.lms.service.*;

public class App {
	public static void main(String[] args)  {
		Scanner sc= new Scanner(System.in);
		LearnerService ls= new LearnerService();
		TrackService ts =new TrackService();
		CourseService cs=new CourseService();
		EnrollService es=new EnrollService();
		
		while(true) {
			System.out.println("***************  WELCOME TO LMS PORTAL  ***************");
			System.out.println("1. Add Learner");
			System.out.println("2. Get All Learners");
			System.out.println("3. Get Learner By Id");
			System.out.println("4. Update Learner Details");
			System.out.println("5. Delete Learner By Id");
			System.out.println("6. Add track");
			System.out.println("7. Add Course");
			System.out.println("8. Get All Courses");
			System.out.println("9. Get Course By Id");
			System.out.println("10. Get All Courses By Track");
			System.out.println("11. Add Enrollment");
			System.out.println("0. Exit");
			System.out.println("*******************************************************");
			
			int c=sc.nextInt();
			
			if (c==0) {
				System.out.println("Thank you for visiting, good bye(^o^)");
				break;//breaks the while loop
			}
			
			switch (c){
				case 1:
					System.out.println("enter learner name");
					sc.nextLine();
					String add_name= sc.nextLine();
					System.out.println("enter learner email");
					String add_email= sc.nextLine();
				try {
					ls.addLearner(add_name,add_email);
				} catch (InvalidInputException e) {
					System.out.println(e.getMessage());
				}
					
					break;
					
//----------------------------fetching all learner details--------------------------------------------------------------
				case 2: 
					List<Learner> lnr =ls.getAllLearners();
					lnr.stream().forEach(l-> System.out.println(l));

					System.out.println();
					
					break;
					
//-----------------------fetching learner details by id-------------------------------------------------------------------
				case 3:
					System.out.println("enter id");
				try {
					System.out.println(ls.getById(sc.nextInt()));
				} catch (InvalidIdException e) {
					System.out.println(e.getMessage());
				}
					System.out.println();
					break;
//----------------------------updating a record--------------------------------------------------------------------------
				case 4: 
					System.out.println("enter id");
					int id =sc.nextInt();
					sc.nextLine();
					System.out.println("enter name");
					String name =sc.nextLine();
					System.out.println("enter email");
					String email =sc.nextLine();
					Learner l=new Learner(id,name,email);
					try {
						ls.updateLearner(id,l);
					} catch (InvalidIdException | InvalidInputException e) {
						System.out.println(e.getMessage());
					}
					break;
					
//----------------------------------deleting a record by id-------------------------------------------------------------
				case 5:
					System.out.println("enter id");
				try {
					ls.deleteById(sc.nextInt());
				} catch (InvalidIdException e) {
					System.out.println(e.getMessage());
				}
				break;
					
//----------------------------------adding a track-------------------------------------------------------------------------
				case 6:
					System.out.println("enter track name");
					sc.nextLine();
					String track_name= sc.nextLine();
					
				try {
					ts.addTrack(track_name);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
					break;
					
//-------------------------------adding a course-----------------------------------------------------------------------------
				case 7:
					
					Course course = new Course();
					System.out.println("enter course title");
					sc.nextLine();
					String title= sc.nextLine();
					System.out.println("Enter fee");
					BigDecimal fee=BigDecimal.valueOf(sc.nextDouble());
					System.out.println("Enter discount");
					BigDecimal discount=BigDecimal.valueOf(sc.nextDouble());
					System.out.println("Enter track Id");
					int trackId= sc.nextInt();
					
					course.setTitle(title);
					course.setFee(fee);
					course.setDiscount(discount);
					
					
					
				try {
					cs.addCourse(course, trackId);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
					break;
//------------------------------------get all courses--------------------------------------------------------------------------
				case 8: 
					List<Course> allCourse =cs.getAllCourse();
					allCourse.stream().forEach(q->  System.out.println("Course Id : "+q.getCourseId() +"\t Course name : "+ q.getTitle()+"\t Course fee : "+ q.getFee()+ "\t Track name : "+q.getTrack().getTrackname()));

					System.out.println();
					
					break;
//-----------------------------------get courses by id------------------------------------------------------------------------
				case 9: 
					
				Course courseById;
				System.out.println("Enter course id");
				int cId=sc.nextInt();
				try {
					courseById = cs.getCourseById(cId);
					System.out.println(" Course Id : "+courseById.getCourseId() +"\n Course name : "+ courseById.getTitle()+"\n Course fee : "+ courseById.getFee()+ "\n Track name : "+courseById.getTrack().getTrackname());

				} catch (InvalidIdException e) {
					System.out.println(e.getMessage());
				}
					
				System.out.println();
					
					break;
					
//-----------------------------------get courses by track------------------------------------------------------------------------
				case 10: 
					
				List<Course> allCourseInTrack;
				System.out.println("Enter track id");
				int tId=sc.nextInt();
				try {
					allCourseInTrack = cs.getCourseByTrack(tId);
					allCourseInTrack.stream().forEach(q->  System.out.println("Course Id : "+q.getCourseId() +"\t Course name : "+ q.getTitle()+"\t Course fee : "+ q.getFee()+ "\t Track name : "+q.getTrack().getTrackname()));

				} catch (InvalidIdException e) {
					System.out.println(e.getMessage());
				}
					
					System.out.println();
					
					break;
//---------------------------------------------Add enrollment-------------------------------------------------------------------
				case 11: 
					System.out.println("Enter learner id");
					int learnerId = sc.nextInt();
					System.out.println("Enter course id");
					int courseId = sc.nextInt();
				try {
					es.addEnrollment(learnerId, courseId,sc);
				} catch (InvalidIdException e) {
					System.out.println(e.getMessage());
				}
					break;
					
					
				default:
					System.out.println("Enter appropriate value");
				
			
			}//switch
		}//while loop
		
		sc.close();
		
		
	}//main method
	
	

}//app class
