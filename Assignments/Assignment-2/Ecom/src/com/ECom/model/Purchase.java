package com.ECom.model;

import java.time.LocalDate;

public class Purchase {
    private int id;
    private LocalDate dateOfPurchase;
    private Customer customer; 
    private Product product;  

 //--------------------------------Constructors-----------------------------------------
    /**default constructor
     * for empty objects
     */
    public Purchase() {}

    
    
    
    
    /**parameterized constructor to assign user defined values
     * @param id
     * @param dateOfPurchase
     * @param customer
     * @param product
     */
    public Purchase(int id, LocalDate dateOfPurchase, Customer customer, Product product) {
        this.id = id;
        this.dateOfPurchase = dateOfPurchase;
        this.customer = customer;
        this.product = product;
    }

    //----------------------Getters and Setters----------------------------------------
    public int getId() { return id; }
    public LocalDate getDateOfPurchase() { return dateOfPurchase; }
    public Customer getCustomer() { return customer; }
    public Product getProduct() { return product; }
    
    
    public void setId(int id) { this.id = id; }
    public void setDateOfPurchase(LocalDate dateOfPurchase) { this.dateOfPurchase = dateOfPurchase; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setProduct(Product product) { this.product = product; }
}

