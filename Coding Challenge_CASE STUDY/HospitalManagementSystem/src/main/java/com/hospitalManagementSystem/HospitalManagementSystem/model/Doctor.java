package com.hospitalManagementSystem.HospitalManagementSystem.model;

import java.time.LocalDate;

import com.hospitalManagementSystem.HospitalManagementSystem.enums.Role;
import com.hospitalManagementSystem.HospitalManagementSystem.enums.Speciality;
import com.hospitalManagementSystem.HospitalManagementSystem.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;



@Entity
public class Doctor {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int id;
		
		@Column(nullable = false)
		private String name;
		
		
		
		@Enumerated(EnumType.STRING)
		@Column(nullable = false)
		private Speciality speciality;
		
		
		
		@OneToOne
		@JoinColumn(nullable=false)
		private User user;
		
		
		
		
		
		
		
		
	//-----------------------------------Default constructor-------------------------------------------
		
		
			public Doctor() throws InvalidInputException {
				this.setUser(new User(Role.DOCTOR)); 
			}
		
		
	//-----------------------------------parameterized constructor-------------------------------------
		
		public Doctor(int id, String name,LocalDate dob, Speciality speciality, String contactNumber, String email,
				String address,User user) throws InvalidInputException {
			this.setId(id);
			this.setName( name);
			this.setSpeciality( speciality);
			this.setUser(user);
		}
		
		
		
		
		

		
		
		
	//---------------------------------getters---------------------------------------
		public int getId() {return id;}
		public String getName() {return name;}
		public Speciality getSpeciality() {return speciality;}
		public User getUser() {return user;}
		
		
		
		
		
		
		
	//-------------------------------setters-----------------------------------------
		
		
		/**Here we check whether id is proper or not .
		 * Checks weather the id is less than 0, if yes throws a InvalidInputException.
		 * Else sets object's id to given id.
		 * @param id
		 * @throws InvalidInputException
		 */
		public void setId(int id) throws InvalidInputException {
			if( id<=0) {
				throw new InvalidInputException("Doctor ID is Invalid. Please enter appropriate ID...!!!");
			}
			this.id = id;
		}
		
		
		
		
		/**Here we check whether name is proper or not
		 * if no throws a InvalidInputException.
		 * Else sets object's name to given name.
		 * @param name
		 * @throws InvalidInputException
		 */
		public void setName(String name)throws InvalidInputException {
			if(name==null || name.trim().isEmpty()) {
				throw new InvalidInputException("Name is Invalid. Please enter appropriate Name(Only alphabets and spaces allowed)...!!!");
			}
			this.name = name;
		}
		
		
		
		
		/**Here we check whether Speciality is proper or not .
		 * if speciality(ENUM) is null then throws a InvalidInputException.
		 * Else sets object's speciality to the given speciality. 
		 * @param speciality
		 * @throws InvalidInputException
		 */
		public void setSpeciality(Speciality speciality) throws InvalidInputException{
			if(speciality==null) {
				throw new InvalidInputException("Speciality is Invalid. Please enter appropriate Speciality...!!!");
			}//if statement
			this.speciality=speciality;
		}
		
	
		
		
		/**to set credentials and user type
		 * @param credentials
		 * @throws InvalidInputException
		 */
		public void setUser(User user) throws InvalidInputException{
			if (user==null) {
				throw new InvalidInputException("Provided user object is null. Please provide appropriate user object...!!!");
			}
			this.user=user;
		}
		

	}

