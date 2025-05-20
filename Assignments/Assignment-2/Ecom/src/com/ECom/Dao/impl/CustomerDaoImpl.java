package com.ECom.Dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.ECom.Dao.interfaces.CustomerDao;
import com.ECom.model.Customer;
import com.ECom.utility.DBUtility;

public class CustomerDaoImpl implements CustomerDao {
	DBUtility db=DBUtility.getInstance();
	
	
	@Override
	public void addCustomer(Customer customer) {
		String query = "INSERT INTO customer (id, name, city) VALUES (?, ?, ?)";

        try {
        	 Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getCity());
            ps.executeUpdate();
            System.out.println("Customer successfully added to the database...");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
    
        } finally {
            db.close();
        }
	}

	@Override
	public Customer getCustomerById(int id) {
		 String query = "SELECT * FROM customer WHERE id = ?";
	        Customer customer = null;

	        try {
	        	 Connection conn = db.connect();
	             PreparedStatement ps = conn.prepareStatement(query);

	            ps.setInt(1, id);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                customer = new Customer();
	                customer.setId(rs.getInt("id"));
	                customer.setName(rs.getString("name"));
	                customer.setCity(rs.getString("city"));
	            }

	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            db.close();
	        }

	        return customer;
	}

	@Override
	public List<Customer> getAllCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer updateCustomer(int id, Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCustomer(int id) {
		// TODO Auto-generated method stub
		
	}
	

}
