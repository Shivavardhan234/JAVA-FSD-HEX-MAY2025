package com.ECom.Dao.interfaces;

import java.util.List;

import com.ECom.model.Category;

public interface CategoryDao {
	
	void addCategory(Category category);
	
	Category getcategoryById(int id);
	List<Category> getAllcategories();
	
	Category updateCategory(int id, Category category);
	
	void deleteCategory(int id);

	Category getCategoryById(int categoryId);

}
