package com.ECom.Dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.ECom.Dao.interfaces.PurchaseDao;
import com.ECom.model.Purchase;
import com.ECom.utility.DBUtility;

public class PurchaseDaoImpl implements PurchaseDao {
	DBUtility db =DBUtility.getInstance();
	
	
	@Override
    public void addPurchase(Purchase purchase) {
        String query = "INSERT INTO purchase ( date_of_purchase, customer_id, product_id) VALUES ( ?, ?, ?)";
        try {
        	Connection conn = db.connect();
        	
             PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, purchase.getDateOfPurchase().toString());
            ps.setInt(2, purchase.getCustomer().getId());
            ps.setInt(3, purchase.getProduct().getId());
            ps.executeUpdate();

            System.out.println("Purchase added successfully.");

        } catch (SQLException e) {
        	System.out.println("error occured while adding purchase");
           System.out.println(e.getMessage());
        } finally {
            db.close();
        }
    }

	

	@Override
	public Purchase getPurchaseById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Purchase> getAllPurchases() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Purchase updatePurchase(int id, Purchase purchase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePurchase(int id) {
		// TODO Auto-generated method stub
		
	}
}
