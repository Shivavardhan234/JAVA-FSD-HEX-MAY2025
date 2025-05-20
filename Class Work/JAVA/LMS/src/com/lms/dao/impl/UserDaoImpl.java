package com.lms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.lms.dao.UserDao;
import com.lms.exception.InvalidIdException;
import com.lms.exception.InvalidInputException;
import com.lms.model.User;
import com.lms.utility.DBUtility;

public class UserDaoImpl implements UserDao{
	DBUtility db = DBUtility.getInstance();Connection conn ;

	@Override
	public int addUser(User user) throws InvalidInputException{
		conn = db.connect();
		String q ="INSERT INTO user(user_id,username, password, role) VALUES(?,?,?,?)";
		
		try {
			PreparedStatement ps =conn.prepareStatement(q);
			ps.setInt(1, user.getUserId());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getPassword());
			ps.setString(4, String.valueOf(user.getRole()));
			ps.executeUpdate();
			
			return user.getUserId();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		
		
	
		//closing the connection
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return 0;
	}
	
	
	
	
	
	
	

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserById(int id) throws InvalidIdException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateUser(int id, User user) throws InvalidIdException, InvalidInputException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(int id) throws InvalidIdException {
		// TODO Auto-generated method stub
		
	}
	

}
