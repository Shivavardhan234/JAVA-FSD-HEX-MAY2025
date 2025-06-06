package com.maverickbank.MaverickBank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration //<- This ensures that this class gets called during every API call
public class SecurityConfig {
	@Autowired
	private JwtFilter jwtFilter;

	
	@Bean
	 SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf((csrf)->csrf.disable())
			.authorizeHttpRequests((authorize) -> authorize
		 //================================== USER ============================================
					//----------------------- post ------------------------------------
					.requestMatchers("/api/user/add").hasAnyAuthority("ADMIN")
					//----------------------- get --------------------------------------
					.requestMatchers("/api/user/get/all").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/user/get/by-id/{id}").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/user/get/details").authenticated()
					//----------------------- put ---------------------------------------
					.requestMatchers("/api/user/update/username/{username}").authenticated()
					.requestMatchers("/api/user/update/password/password/{oldPassword}/{newPassword}").authenticated()
					.requestMatchers("/api/user/update/role/{id}/{newRole}").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/user/update/deactivate/{password}").authenticated()
					.requestMatchers("/api/user/update/activate/{password}").authenticated()
					.requestMatchers("/api/user/update/status/{id}/{status}").hasAnyAuthority("ADMIN")
					
					.requestMatchers("/api/user/update/delete/{password}").hasAnyAuthority("CUSTOMER")
					//---------------------- utils ---------------------------------------
					.requestMatchers("/api/user/token/v2").authenticated()
					.requestMatchers("/api/user/token/v1").authenticated()
					
					
					
		//================================ CUSTOMER ============================================
					//---------------------- post --------------------------------------------
					.requestMatchers("/api/customer/signup").permitAll()
					.requestMatchers("/api/customer/add/additional-details").hasAnyAuthority("CUSTOMER")
					
					//----------------------- get --------------------------------------------
					.requestMatchers("/api/customer/get/all").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/customer/get/by-id").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/customer/get/current").hasAnyAuthority("CUSTOMER")
					.requestMatchers("/api/customer/get/by-user-id").hasAnyAuthority("ADMIN")
					
					//---------------------- put ----------------------------------------------
					.requestMatchers("/api/customer/update/customer").hasAnyAuthority("CUSTOMER")

					
					
					
		//================================= EMPLOYEE =============================================
					//----------------------- post -----------------------------------
					.requestMatchers("/api/employee/add").hasAnyAuthority("ADMIN")
					
					//------------------------ get -----------------------------------
					.requestMatchers("/api/employee/get/all").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get/by-id/{id}").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get/by-user-id/{id}").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get/by-designation/{designation}").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get/by-branch/{branch}").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get/by-username/{username}").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get/by-email/{email}").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get/by-contactNumber/{contactNumber}").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get/current-employee").hasAnyAuthority(" LOAN_OFFICER", "ACCOUNT_MANAGER", "TRANSACTION_ANALYST", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					//------------------------ update ---------------------------------
					.requestMatchers("/api/employee/update/personal-details").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/update/professional-details").hasAnyAuthority("ADMIN")
					
					
					
		//==================================== CIO ==================================================		
					//----------------------- post ----------------------------------
					.requestMatchers("/api/cio/add").permitAll()
					
					//------------------------ get ----------------------------------
					.requestMatchers("/api/cio/get/all").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/cio/get/by-id").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/cio/get/by-id/{id}").hasAnyAuthority("ADMIN")
					//------------------------ put ----------------------------------
					.requestMatchers("/api/cio/update").hasAnyAuthority("ADMIN")
					
					
					
	   //===================================== BRANCH ================================================
				//----------------------------- add -------------------------------------------
					.requestMatchers("/api/branch/add").hasAnyAuthority("ADMIN")
				//----------------------------- get -------------------------------------------
					.requestMatchers("/api/branch/get/by-id").authenticated()
					.requestMatchers("/api/branch/get/all").authenticated()
					.requestMatchers("/api/branch/get/by-name").authenticated()
					.requestMatchers("/api/branch/get/by-state").authenticated()
				//----------------------------- put -------------------------------------------
					.requestMatchers("/api/branch/activate").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/branch/deactivate").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/branch/update/contact-number/{id}/{contactNumber}").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/branch/update/email/{id}/{email}").hasAnyAuthority("ADMIN")
					
					
	  //==================================== ACCOUNT TYPE =============================================
				//------------------------------ post ----------------------------------------
					.requestMatchers("/api/account-type/add").hasAnyAuthority("ADMIN")
				//------------------------------ get -----------------------------------------
					.requestMatchers("/api/account-type/get/all").authenticated()
					.requestMatchers("/api/account-type/get/by-name{type}").authenticated()
					.requestMatchers("/api/account-type/get/by-id/{id}").hasAnyAuthority("ADMIN"," LOAN_OFFICER", "ACCOUNT_MANAGER", "TRANSACTION_ANALYST", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------ put -----------------------------------------
					.requestMatchers("/api/account-type/update").hasAnyAuthority("ADMIN")
					
	//====================================== Account Opening Application ===================================
				//-------------------------------- post ------------------------------------------------
					.requestMatchers("/api/account-opening-application/add").hasAnyAuthority("CUSTOMER")
				//--------------------------------- get ------------------------------------------------
					.requestMatchers("/api/account-opening-application/get/all").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-opening-application/get/by-id/{id}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-opening-application/get/by-branch").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-opening-application/get/by-account-type").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-opening-application/get/by-customer-approval-status/{customerApprovalStatus}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-opening-application/get/by-employee-approval-status/{employeeApprovalStatus}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-opening-application/get/by-date/{date}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					
				//--------------------------------- put ------------------------------------------------
					.requestMatchers("/api/account-opening-application/update/customer-approval/{id}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-opening-application/update/approve/{id}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-opening-application/update/reject/{id}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					
					
	//=================================== CUSTOMER ACCOUNT OPENING APPLICATION ================================
					
					
					//Account
					//CustomerAccount
					//Account requests
					//Loan plan
					//Loan opening application
					//Loan
					//Loan payment history
					//Loan Closure Request
					//Transaction
					
				.anyRequest().authenticated()
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.httpBasic(Customizer.withDefaults());

		return http.build();
	}


	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	AuthenticationManager getAuthManager(AuthenticationConfiguration auth) 
			throws Exception {
		  return auth.getAuthenticationManager();
	 }

	
	
}
