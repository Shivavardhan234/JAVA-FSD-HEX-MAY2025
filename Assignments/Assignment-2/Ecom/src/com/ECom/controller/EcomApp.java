package com.ECom.controller;

import java.util.List;
import java.util.Scanner;

import com.ECom.model.Product;
import com.ECom.service.*;
public class EcomApp {

	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductService productService = new ProductService();
        PurchaseService purchaseService = new PurchaseService();

        int choice;
        while(true){
            System.out.println("****** Welcome to our website ******");
            System.out.println("1. Add Product");
            System.out.println("2. Get Products by Category");
            System.out.println("3. Add Purchase");
            System.out.println("0. Exit");
            System.out.println("****** ********************** ******");
            System.out.println("Enter your choice: ");
            choice = sc.nextInt();
            
            if(choice==0) {
            	System.out.println("Thank you. Good bye...");
            	break;
            }
            

            switch (choice) {
                case 1:
                	System.out.println("Enter Product id: ");
                    int id = sc.nextInt();
                    System.out.println("Enter Product Title: ");
                    sc.nextLine();
                    String title = sc.nextLine();

                    System.out.println("Enter Product Price: ");
                    double price = sc.nextDouble();
                    sc.nextLine();

                    System.out.println("Enter Product Description: ");
                    String description = sc.nextLine();

                    System.out.println("Enter Category ID: ");
                    int categoryId = sc.nextInt();

                    productService.addProduct(id,title, price, description, categoryId);
                    
                    break;

                case 2:
                    System.out.println("Enter Category id: ");
                    int catId = sc.nextInt();
                    List<Product> products = productService.getProductsByCategoryId(catId);

                    if (products.isEmpty()) {
                        System.out.println("No products found for the given category.");
                    } else {
                        for (Product p : products) {
                            System.out.println("ID: " + p.getId() + ", Title: " + p.getTitle() +
                                    ", Price: ₹" + p.getPrice() + ", Description: " + p.getDescription() +
                                    ", Category: " + p.getCategory().getName());
                        }
                    }
                    break;

                case 3:
                    
                    System.out.println("Enter Customer ID: ");
                    int customerId = sc.nextInt();

                    System.out.println("Enter Product ID: ");
                    int productId = sc.nextInt();

                    purchaseService.addPurchase( customerId, productId);
                    
                    break;

                

                default:
                    System.out.println("Invalid choice. Try again.");
            }//switch

        }//while loop

        sc.close();
    }// main method
}// class
