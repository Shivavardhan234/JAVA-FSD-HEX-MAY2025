package com.ECom.model;

public class Product {
    private int id;
    private String title;
    private double price;
    private String description;
    private Category category; 

    /**default constructor
     * for empty objects
     */
    public Product() {}

    /**parameterized constructor to assign user defined values
     * @param id
     * @param title
     * @param price
     * @param description
     * @param category
     */
    public Product(int id, String title, double price, String description, Category category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    
    
    //--------------------------------Getters and Setters----------------------------------------
    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    
    
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { this.category = category; }
}

