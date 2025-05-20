package com.lms.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;



	class calculatePercentTest {
		MyService ms;
		
		@BeforeEach
		void init() {
			ms=new MyService();
			
		}

		@Test
		void samptest() {
			Assertions.assertEquals(50, ms.calculatePercent(100, 50));
			Assertions.assertDoesNotThrow(() ->{ 
	            ms.calculatePercent(100, 50);
	        });
			RuntimeException e= Assertions.assertThrows(RuntimeException.class,() ->{ 
	            ms.calculatePercent(50, 100);
	        });
			Assertions.assertEquals("marks scored should not be greater than total marks", e.getMessage());
			
		}

	}


