package com.maverickbank.MaverickBank.model.users;

import java.time.LocalDate;

import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.User;

import jakarta.persistence.Entity;

@Entity
public class CIO extends Actor {
	
	//------------------default constructor-----------------------------------------
		public CIO() throws InvalidInputException {
			super(Role.ADMIN);
		}

		
		//----------------constructor with all fields----------------------------------
		public CIO(int id, String name, LocalDate dob, Gender gender, String contactNumber, String email, String address, User user) throws InvalidInputException {
			super(id, name, dob, gender,  contactNumber, email, address,user);
		}

		
		//---------------constructor without admin credentials-------------------------
		public CIO(int id, String name, LocalDate dob, Gender gender, String contactNumber, String email, String address) throws InvalidInputException {
			super(id, name, dob, gender, contactNumber, email, address);
		}


}
