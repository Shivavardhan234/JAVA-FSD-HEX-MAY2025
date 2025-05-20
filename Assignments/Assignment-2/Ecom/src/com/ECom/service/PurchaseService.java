package com.ECom.service;

import java.time.LocalDate;

import com.ECom.Dao.impl.*;
import com.ECom.Dao.interfaces.*;
import com.ECom.model.Customer;
import com.ECom.model.Product;
import com.ECom.model.Purchase;
public class PurchaseService {
	
	private PurchaseDao pd=new PurchaseDaoImpl();
    private ProductDao productDao=new ProductDaoImpl();
    private CustomerDao customerDao=new CustomerDaoImpl();

   

    public void addPurchase( int customerId, int productId) {
        Customer customer = customerDao.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Invalid Customer ID. Enter appropriate one...!!!");
        }

        Product product = productDao.getProductById(productId); 
        if (product == null) {
            System.out.println("Invalid Product ID.Enter appropriate value...!!!"); 
        }

        
        
        Purchase purchase = new Purchase();
        purchase.setDateOfPurchase(LocalDate.now());
        purchase.setProduct(product);
        purchase.setCustomer(customer);
        pd.addPurchase(purchase);
        
    }

}
