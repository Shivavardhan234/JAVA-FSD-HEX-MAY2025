package com.ECom.Dao.interfaces;

import java.util.List;

import com.ECom.model.Customer;

public interface CustomerDao {
	void addCustomer(Customer customer);
	
	
	Customer getCustomerById(int id);
	List<Customer> getAllCustomers();
	
	Customer updateCustomer(int id, Customer customer);
	
	void deleteCustomer(int id);
	

}
