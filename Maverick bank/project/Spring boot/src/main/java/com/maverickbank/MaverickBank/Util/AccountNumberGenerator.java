package com.maverickbank.MaverickBank.Util;

import java.util.Random;

public class AccountNumberGenerator {
	public static String accountNumberGenerator() {
		Random random=new Random();
		StringBuilder accountNumber=new StringBuilder();
		
		for(int i=0;i<11;i++) {
			accountNumber.append(random.nextInt(10));
		}
		return String.valueOf(accountNumber);
	}

}
