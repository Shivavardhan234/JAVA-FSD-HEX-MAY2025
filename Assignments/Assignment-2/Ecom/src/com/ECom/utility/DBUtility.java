package com.ECom.utility;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;


public class DBUtility {
	
	
	private String  URL="jdbc:mysql://localhost:3306/ecom";
	private String USER="root";
	private String PASSWORD="Shivavardhan@t2s";
	private Connection connection;
	private static DBUtility db =new DBUtility();
	
	private DBUtility() {}
	
	
	public static DBUtility getInstance() {
		return db;
	}
	
	
	
	
	public  Connection connect() throws SQLException {
		
		try {
			connection=DriverManager.getConnection(URL,USER,PASSWORD);	
		}
		catch(SQLException e) {
			throw new SQLException("Error occured while eshtablishing connection...!!!");	
		}
		return connection;
	}
	
    
	
	public void close() {
		try {
			if(!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
//			e.printStackTrace();
		}
	}
	

}
