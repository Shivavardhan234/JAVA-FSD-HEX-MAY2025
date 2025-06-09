package com.ECom.model;

import com.ECom.enums.CategoryType;

public class Category {
    private int id;
    private CategoryType name;

    
    
    /**default constructor
     * for empty objects
     */
    public Category() {}

    
    
    /**parameterized constructor to assign user defined values
     * @param id
     * @param name
     */
    public Category(int id, CategoryType name) {
        this.id = id;
        this.name = name;
    }
    
    

    //------------------------Getters and Setters----------------------------------
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public CategoryType getName() { return name; }
    public void setName(CategoryType name) { this.name = name; }
}
