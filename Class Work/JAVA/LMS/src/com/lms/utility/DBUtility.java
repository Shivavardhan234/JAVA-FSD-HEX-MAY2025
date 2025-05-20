package com.lms.utility;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtility {
	private  String  URL= "jdbc:mysql://localhost:3306/lms_db";
	private  String  USER="root";
	private  String  PASSWORD="Shivavardhan@t2s";
	private  String driver="com.mysql.cj.jdbc.Driver.class";
	private  Connection conn;
	private static DBUtility db= new DBUtility();
	
	
	private DBUtility() {};
	
	public static DBUtility getInstance() {
		return db;
	}
	
	
	public  Connection connect() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		try {
			conn= DriverManager.getConnection(URL,USER,PASSWORD);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		
		return conn;
		
	}//connect method
	
	
	
	public void close() {
		try {
			if (!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}//close method
	
	
}//DBUtility class
