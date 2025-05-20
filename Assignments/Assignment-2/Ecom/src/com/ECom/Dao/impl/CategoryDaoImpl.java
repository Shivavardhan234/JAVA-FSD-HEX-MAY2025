package com.ECom.Dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.ECom.Dao.interfaces.CategoryDao;
import com.ECom.enums.CategoryType;
import com.ECom.model.Category;
import com.ECom.utility.DBUtility;

public class CategoryDaoImpl implements CategoryDao {
	private DBUtility db = DBUtility.getInstance();

	
	
	
	
	
    /**getCategoryById() is a method which returns category object if it matches to the given categoryId
     * @param categoryId
     * @return
     */
    public Category getCategoryById(int categoryId) {
    	
        String query = "SELECT * FROM category WHERE id = ?";
        Category category = null;
        try {
        	 Connection conn=db.connect();
             PreparedStatement ps =conn.prepareStatement(query);
            ps.setInt(1, categoryId);
            ResultSet rs=ps.executeQuery();

            if (rs.next()) {
            	
                int id = rs.getInt("id");
                String name = rs.getString("name");
                CategoryType categoryType = CategoryType.valueOf(name.toUpperCase().replace(" ", "_"));
                category = new Category(id, categoryType);
            }

        } catch (SQLException | IllegalArgumentException e) {
        	System.out.println(e.getMessage());
        } finally {
            db.close();
        }

        return category;
    }

    
    
    
    
    
    
    
	@Override
	public void addCategory(Category category) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Category getcategoryById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> getAllcategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category updateCategory(int id, Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCategory(int id) {
		// TODO Auto-generated method stub
		
	}

}
