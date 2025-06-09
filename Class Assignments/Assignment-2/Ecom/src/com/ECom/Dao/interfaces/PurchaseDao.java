package com.ECom.Dao.interfaces;

import java.util.List;

import com.ECom.model.Purchase;

public interface PurchaseDao {
	
	void addPurchase(Purchase purchase);
	
	Purchase getPurchaseById(int id);
	List<Purchase> getAllPurchases();
	
	Purchase updatePurchase(int id, Purchase purchase);
	
	void deletePurchase(int id);
	

}
