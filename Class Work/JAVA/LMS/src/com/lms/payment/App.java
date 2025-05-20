package com.lms.payment;

import java.util.Scanner;

public class App {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		/*  Step 1 : print the available payment methods  */
		System.out.println("------Available payment methods------");
		for(Payment p : Payment.values()) {
			System.out.println(String.valueOf(p));
			
		}
		System.out.println("-------------------------------------");
		
		System.out.println("Enter your payment type");
		
		
		
		try {
			Payment paymentType = Payment.valueOf(sc.next().toUpperCase());
			if(paymentType.getStatus()==0) {
				System.out.println("The payment type you want to proceed with is available but currently inactive. \nSorry for the inconvinence..");
			}
			else {
				
				System.out.println("The payment type you want to proceed with is available and active...");
				System.out.println("The daily limit is: "+ PaymentFactory.getDailyLimit(paymentType));
			}
			
		}
		catch(Exception e) {
			System.out.println("The payment type you want to proceed with is not available....");
			
		}
		
		
		
		sc.close();
	}//main method
	
	
}//App class
