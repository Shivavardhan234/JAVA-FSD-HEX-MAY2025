package com.ECom.Dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.ECom.Dao.*;
import com.ECom.Dao.impl.*;
import com.ECom.Dao.interfaces.CategoryDao;
import com.ECom.Dao.interfaces.ProductDao;
import com.ECom.enums.CategoryType;
import com.ECom.model.Category;
import com.ECom.model.Product;
import com.ECom.utility.DBUtility;

public class ProductDaoImpl implements ProductDao{
	private DBUtility db = DBUtility.getInstance();
	private CategoryDao cat =new CategoryDaoImpl();
	

    /**takes the product object from the user and inserts into database using the connection established
     * @param product
     */
	@Override
    public void addProduct(Product product) {
        String query = "INSERT INTO product (id, title, price, description, category_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, product.getId());
            ps.setString(2, product.getTitle());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getDescription());
            ps.setInt(5, product.getCategory().getId());

            ps.executeUpdate();
            System.out.println("Product inserted successfully.");

        } catch (SQLException e) {
        	System.out.println(e.getMessage()); 
        } finally {
            db.close();
        }
    }
    
    
    
    
    
    /**gets all the products with the matching category id
     *
     */
    @Override
    public List<Product> getProductsByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();
        CategoryDao cat=new CategoryDaoImpl();
        Category category = cat.getCategoryById(categoryId);
        
        String query = "SELECT * FROM product WHERE category_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setTitle(rs.getString("title"));
                product.setPrice(rs.getDouble("price"));
                product.setDescription(rs.getString("description"));
                product.setCategory(category);
                products.add(product);
            }

        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        } finally {
            db.close();
        }

        return products;
    }

    
//----------------------------------------get product by id---------------------------------------
	@Override
	public Product getProductById(int id) {
        Product product = null;
        String query = "SELECT * FROM product WHERE id = ?";

        try {
        	Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setTitle(rs.getString("title"));
                product.setPrice(rs.getDouble("price"));
                product.setDescription(rs.getString("description"));

                int categoryId = rs.getInt("category_id");
                Category category = cat.getcategoryById(categoryId);
                product.setCategory(category);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            db.close();
        }

        return product;
    }

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product updateProduct(int id, Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteProduct(int id) {
		// TODO Auto-generated method stub
		
	}

}
