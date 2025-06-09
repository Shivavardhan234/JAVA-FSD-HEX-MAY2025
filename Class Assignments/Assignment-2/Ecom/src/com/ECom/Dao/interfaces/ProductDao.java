package com.ECom.Dao.interfaces;

import java.util.List;

import com.ECom.model.Product;

public interface ProductDao {
	
	void addProduct(Product product);
	
	
	Product getProductById(int id);
	List<Product> getAllProducts();
	
	Product updateProduct(int id, Product product);
	
	void deleteProduct(int id);


	List<Product> getProductsByCategoryId(int categoryId);

}
