package com.lms.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {
	public static void main(String[] args) {
		
		System.out.println("----------Available Cities------------");
		for(City c : City.values()) {
			System.out.println(c);
		}
		
		
		City[] cities = City.values();
		
		
		System.out.println("----------Available Cities As String------------");
		List<String> cts = new ArrayList<String>();
		for (City c : City.values()) {
			cts.add(c.toString());
		}
		
		cts.stream().forEach(e->System.out.println(e));
		
		
		System.out.println("----------checking the string is in city------------");
		String check = "mumbai";
		try {
		City verify = City.valueOf(check.toUpperCase());
		System.out.println("yes it is present");
		}
		catch (Exception e) {
			System.out.println("The value you have entered is not in the enum....");
			e.printStackTrace();
		}
		
		
		
	}// main method

}//DEMO class
