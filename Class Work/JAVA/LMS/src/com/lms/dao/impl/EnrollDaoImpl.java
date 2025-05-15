package com.lms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.lms.dao.EnrollDao;
import com.lms.model.Enroll;
import com.lms.utility.DBUtility;

public class EnrollDaoImpl implements EnrollDao {
	private DBUtility db = new DBUtility();

	@Override
	public void addEnrollment(Enroll enroll) {
		Connection conn = db.connect();
		
		String q = " INSERT INTO enroll (enrollment_id, date_of_enroll, fee_paid, coupons_used, learner_id, course_id) VALUES (?,?,?,?,?,?)";
		
		try {
			PreparedStatement ps = conn.prepareStatement(q);
			ps.setInt(1, enroll.getEnrollmentId());
			ps.setString(2, enroll.getDateOfEnroll().toString());
			ps.setBigDecimal(3, enroll.getFeePaid());
			ps.setString(4, enroll.getCuponUsed());
			ps.setInt(5, enroll.getLearner().getId());
			ps.setInt(6, enroll.getCourse().getCourseId());
			ps.executeUpdate();
			
			System.out.println("Learner successfully enrolled....");
			
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		db.close();
		return;
	}

}
