package com.ECom.model;

public class Customer {
    private int id;
    private String name;
    private String city;

 //--------------Constructors--------------------------------------------------
    
    /**default constructor
     * for empty objects
     */
    public Customer() {}

    

    
    /**parameterized constructor to assign user defined values
     * @param id
     * @param name
     * @param city
     */
    public Customer(int id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    //----------------------Getters and Setters---------------------------------------
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    
    
    
    public void setId(int id) { this.id = id; }

     public void setName(String name) { this.name = name; }

    
    public void setCity(String city) { this.city = city; }
}

