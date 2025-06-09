package com.ECom.service;

import java.util.List;

import com.ECom.Dao.impl.*;
import com.ECom.Dao.interfaces.*;
import com.ECom.model.*;
public class ProductService {
	private ProductDao pd=new ProductDaoImpl();
    private CategoryDao cd = new CategoryDaoImpl();

   

    public void addProduct(int id, String title, double price, String description, int categoryId) {
        Category category = cd.getCategoryById(categoryId);
        if (category == null) {
            System.out.println("Invalid Category ID");
        }

        Product product = new Product(id, title, price, description, category);
        pd.addProduct(product);
    }

    
    
    public List<Product> getProductsByCategoryId(int categoryId) {
        return pd.getProductsByCategoryId(categoryId);
    }
}
