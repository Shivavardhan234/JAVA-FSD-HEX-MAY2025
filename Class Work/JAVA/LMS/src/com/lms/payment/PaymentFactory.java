package com.lms.payment;

import java.math.BigDecimal;
import java.util.Scanner;
public class PaymentFactory {
	
	public static BigDecimal getDailyLimit(Payment p) {
		switch (p) {
			case Payment.UPI:
				return BigDecimal.valueOf(100000);
			case Payment.NEFT:
				return BigDecimal.valueOf(200000);
			case Payment.IMPS:
				return BigDecimal.valueOf(500000);
				default:
					return null;
		}
		
		
		
		
	
	}

}
