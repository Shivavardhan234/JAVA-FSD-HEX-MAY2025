package com.lms.controller;

import java.math.BigDecimal;
import java.util.*;

import com.lms.enums.Role;
import com.lms.exception.InvalidIdException;
import com.lms.exception.InvalidInputException;
import com.lms.model.Course;
import com.lms.model.Learner;
import com.lms.model.User;
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
			System.out.println("1. Sign in ");
			System.out.println("2. Register as a learner ");
			System.out.println("0. Exit");
			System.out.println("*******************************************************");
			
			int c=sc.nextInt();
			
			if (c==0) {
				System.out.println("Thank you for visiting, good bye(^o^)");
				break;//breaks the while loop
			}
			
			switch (c){
				case 1:
					System.out.println("logging in....");
					
					break;
					
				case 2: 
					sc.nextLine();
					System.out.print("Enter your name: ");
					String name = sc.nextLine();
					System.out.print("Enter your email: ");
					String email = sc.nextLine();
					System.out.print("Enter username: ");
					String username = sc.nextLine();
					System.out.print("Enter password: ");
					String pass = sc.nextLine();
					
					try {
						ls.addLearner(name, email, username, pass);
					} catch (InvalidInputException e) {
						System.out.println(e.getMessage());
						//e.printStackTrace();
					}
					
					break;
					

					
				default:
					System.out.println("Enter appropriate value");
				
			
			}//switch
		}//while loop
		
		sc.close();
		
		
	}//main method
	
	
	
	
	
	
	
	
	
	
	
	
	static void learnerDashboard(LearnerService ls,CourseService cs,EnrollService es, Scanner sc) {
		while(true) {
			System.out.println("***************  WELCOME TO LMS PORTAL  ***************");
			System.out.println("1. Get Learner By Id");
			System.out.println("2. Update Learner Details");
			System.out.println("3. Delete Learner By Id");
			System.out.println("4. Get All Courses");
			System.out.println("5. Get Course By Id");
			System.out.println("6. Get All Courses By Track");
			System.out.println("7. Add Enrollment");
			System.out.println("0. Exit");
			System.out.println("*******************************************************");
			
			int c=sc.nextInt();
			
			if (c==0) {
				System.out.println("Thank you for visiting, good bye(^o^)");
				break;//breaks the while loop
			}
			
			switch (c){
			//-----------------------fetching learner details by id-------------------------------------------------------------------
				case 1:
					System.out.println("enter id");
					try {
						System.out.println(ls.getById(sc.nextInt()));
					} catch (InvalidIdException e) {
						System.out.println(e.getMessage());
					}
						System.out.println();
					break;
			//----------------------------updating a record--------------------------------------------------------------------------
				case 2: 
					System.out.println("enter id");
					int id =sc.nextInt();
					sc.nextLine();
					System.out.println("enter name");
					String name =sc.nextLine();
					System.out.println("enter email");
					String email =sc.nextLine();
					
					Learner l=new Learner();
					l.setId(id);
					l.setName(name);
					l.setEmail(email);
					try {
						ls.updateLearner(id,l);
					} catch (InvalidIdException | InvalidInputException e) {
						System.out.println(e.getMessage());
					}
				break;
			//----------------------------------deleting a record by id-------------------------------------------------------------
				case 3:
					System.out.println("enter id");
					try {
						ls.deleteById(sc.nextInt());
					} catch (InvalidIdException e) {
						System.out.println(e.getMessage());
					}
				break;
				
				
					

			//------------------------------------get all courses--------------------------------------------------------------------------
				case 4: 
					List<Course> allCourse =cs.getAllCourse();
					allCourse.stream().forEach(q->  System.out.println("Course Id : "+q.getCourseId() +"\t Course name : "+ q.getTitle()+"\t Course fee : "+ q.getFee()+ "\t Track name : "+q.getTrack().getTrackname()));

					System.out.println();
					
					break;
			//-----------------------------------get courses by id------------------------------------------------------------------------
				case 5: 
					
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
				case 6: 
					
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
				case 7: 
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
		
	}//Learner dashboard method
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static void instructorDashboard(LearnerService ls,CourseService cs,TrackService ts,EnrollService es, Scanner sc){
		while(true) {
			System.out.println("***************  WELCOME TO LMS PORTAL  ***************");
			System.out.println("1. Get All Learners");
			System.out.println("2. Get Learner By Id");
			System.out.println("3. Delete Learner By Id");
			System.out.println("4. Add track");
			System.out.println("5. Add Course");
			System.out.println("6. Get All Courses");
			System.out.println("7. Get Course By Id");
			System.out.println("8. Get All Courses By Track");
			System.out.println("0. Exit");
			System.out.println("*******************************************************");
			
			int c=sc.nextInt();
			
			if (c==0) {
				System.out.println("Thank you for visiting, good bye(^o^)");
				break;//breaks the while loop
			}
			
			switch (c){
				
//----------------------------fetching all learner details--------------------------------------------------------------
				case 1: 
					List<Learner> lnr =ls.getAllLearners();
					lnr.stream().forEach(l-> System.out.println(l));

					System.out.println();
					
					break;
					
//-----------------------fetching learner details by id-------------------------------------------------------------------
				case 2:
					System.out.println("enter id");
				try {
					System.out.println(ls.getById(sc.nextInt()));
				} catch (InvalidIdException e) {
					System.out.println(e.getMessage());
				}
					System.out.println();
					break;

//----------------------------------deleting a record by id-------------------------------------------------------------
				case 3:
					System.out.println("enter id");
				try {
					ls.deleteById(sc.nextInt());
				} catch (InvalidIdException e) {
					System.out.println(e.getMessage());
				}
				break;
					
//----------------------------------adding a track-------------------------------------------------------------------------
				case 4:
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
				case 5:
					
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
				case 6: 
					List<Course> allCourse =cs.getAllCourse();
					allCourse.stream().forEach(q->  System.out.println("Course Id : "+q.getCourseId() +"\t Course name : "+ q.getTitle()+"\t Course fee : "+ q.getFee()+ "\t Track name : "+q.getTrack().getTrackname()));

					System.out.println();
					
					break;
//-----------------------------------get courses by id------------------------------------------------------------------------
				case 7: 
					
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
				case 8: 
					
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

					
					
				default:
					System.out.println("Enter appropriate value");
				
			
			}//switch
		}//while loop
	}// Instructor dashboard method
	
	

}//app class
