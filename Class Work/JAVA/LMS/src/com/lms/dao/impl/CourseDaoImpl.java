package com.lms.dao.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms.dao.*;
import com.lms.exception.InvalidIdException;
import com.lms.model.Course;
import com.lms.model.Track;
import com.lms.utility.DBUtility;
public class CourseDaoImpl implements CourseDao {
	DBUtility db = new DBUtility();
	
	
	@Override
	public void addCourse(Course course, int trackId) {
		Connection conn = db.connect();
		
		String q=" INSERT INTO course (course_id, title, fee, discount, publish_date, track_id) VALUES (?,?,?,?,?,?)";
		try {
			PreparedStatement ps= conn.prepareStatement(q);
			ps.setInt(1, course.getCourseId());
			ps.setString(2, course.getTitle());
			ps.setBigDecimal(3,course.getFee());
			ps.setBigDecimal(4, course.getDiscount());
			ps.setString(5, course.getPublishDate().toString());
			ps.setInt(6, trackId);
			ps.executeUpdate();
			
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("course added to the list...");
		
		db.close();
		
	}


	@Override
	public List<Course> getAllCourses() {
		Connection conn = db.connect();
		ArrayList<Course> list =new ArrayList<>();
		
		
		String q ="SELECT * FROM course JOIN track ON course.track_id = track.id";
		
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(q);
			ResultSet rs = ps.executeQuery(q);
			while(rs.next()==true) {
				Course course =new Course();
				course.setCourseId(rs.getInt("course_id"));
				course.setTitle(rs.getString("title"));
				course.setFee(rs.getBigDecimal("fee"));
				course.setDiscount(rs.getBigDecimal("discount"));
				course.setPublishDate(rs.getDate("publish_date").toLocalDate());
				Track t = new Track(rs.getInt("track_id"),rs.getString("name"));
				course.setTrack(t);
				list.add(course);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
				
		
		
		
		db.close();
		return list;
	}


	@Override
	public List<Course> getCourseByTrack(int trackId)throws InvalidIdException {
		Connection conn = db.connect();
		ArrayList<Course> list =new ArrayList<>();
		
		String q = "SELECT * FROM course JOIN track ON course.track_id = track.id WHERE track.id = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(q);
			ps.setInt(1, trackId);
			ResultSet rs= ps.executeQuery();
			while(rs.next()==true) {
				Course course =new Course();
				course.setCourseId(rs.getInt("course_id"));
				course.setTitle(rs.getString("title"));
				course.setFee(rs.getBigDecimal("fee"));
				course.setDiscount(rs.getBigDecimal("discount"));
				course.setPublishDate(rs.getDate("publish_date").toLocalDate());
				Track t = new Track(rs.getInt("track_id"),rs.getString("name"));
				course.setTrack(t);
				list.add(course);
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		db.close();
		return list;
	}


	@Override
	public Course getCourseById(int courseId) throws InvalidIdException {
		Connection conn = db.connect();
		Course course =new Course();
		String q = "SELECT * FROM course JOIN track ON course.track_id = track.id WHERE course.course_id = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(q);
			ps.setInt(1, courseId);
			ResultSet rs= ps.executeQuery();
			if(rs.next()==true) {
				
				course.setCourseId(rs.getInt("course_id"));
				course.setTitle(rs.getString("title"));
				course.setFee(rs.getBigDecimal("fee"));
				course.setDiscount(rs.getBigDecimal("discount"));
				course.setPublishDate(rs.getDate("publish_date").toLocalDate());
				Track t = new Track(rs.getInt("track_id"),rs.getString("name"));
				course.setTrack(t);
				
				
			}
			else {
				throw new InvalidIdException("id not found");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		db.close();
		return course;
	}

}
