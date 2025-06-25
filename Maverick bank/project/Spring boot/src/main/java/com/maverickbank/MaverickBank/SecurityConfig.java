package com.maverickbank.MaverickBank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
					.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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
					.requestMatchers("/api/customer/get/by-id").permitAll()
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
					.requestMatchers("/api/cio/add").hasAnyAuthority("ADMIN")
					
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
					
	//============================================ ACCOUNT HOLDER =============================================
				//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/account-holder/add").hasAnyAuthority("CUSTOMER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/account-holder/get/all").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-holder/get/by-id/{id}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-holder/get/by-email/{email}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-holder/get/by-contact/{contactNumber}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-holder/get/by-aadhar/{aadharNumber}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-holderupdate/get/by-pan/{panCardNumber}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- put ------------------------------------------------
					.requestMatchers("/api/account-holder/update").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					
					
					
					
					
	//=================================== CUSTOMER ACCOUNT OPENING APPLICATION ================================
					
				//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/customer-account-opening-application/add").hasAnyAuthority("CUSTOMER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/customer-account-opening-application/get/all").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/customer-account-opening-application/get/by-id/{id}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/customer-account-opening-application/get/by-customer-id/{customerId}").hasAnyAuthority("CUSTOMER","ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/customer-account-opening-application/get/by-account-holder-id/{accountHolderId}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/customer-account-opening-application/get/by-application-id/{applicationId}").hasAnyAuthority("CUSTOMER","ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- put ------------------------------------------------
					.requestMatchers("/api/customer-account-opening-application/update").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/customer-account-opening-application/update/approval/{id}/{status}").hasAnyAuthority("CUSTOMER")
	
					
					
					
	//============================================= TRANSACTION ====================================================				
					//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/transaction/add").hasAnyAuthority("CUSTOMER","TRANSACTION_ANALYST","LOAN_OFFICER","SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/transaction/get/all").hasAnyAuthority("ACCOUNT_MANAGER","TRANSACTION_ANALYST", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/transaction/get/by-id/{id}").hasAnyAuthority("TRANSACTION_ANALYST","ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/transaction/get/by-date-range/{startDate}/{endDate}/{accountNumber}").hasAnyAuthority("CUSTOMER","ACCOUNT_MANAGER","TRANSACTION_ANALYST", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/transaction/get/credits/{startDate}/{endDate}/{accountNumber}").hasAnyAuthority("ACCOUNT_MANAGER","TRANSACTION_ANALYST", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/transaction/get/debits/{startDate}/{endDate}/{accountNumber}").hasAnyAuthority("ACCOUNT_MANAGER","TRANSACTION_ANALYST", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/transaction/get/account-statement/{accountNumber}").hasAnyAuthority("CUSTOMER")
					.requestMatchers("/api/transaction/get/last-transactions/{accountNumber}/{count}").hasAnyAuthority("CUSTOMER","ACCOUNT_MANAGER","TRANSACTION_ANALYST", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
				
					
					
					
					
	//=============================================== ACCOUNT ===================================================			
				//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/account/add").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/account/get/all").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account/get/by-id/{id}").hasAnyAuthority("CUSTOMER","LOAN_OFFICER","ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account/get/by-account-number/{accountNumber}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account/get/by-branch-id/{id}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account/get/by-account-status/{accountStatus}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account/get/by-account-type-id/{id}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- put ------------------------------------------------
					.requestMatchers("/api/account/update/name/{accountId}/{newName}update").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account/update/status/{accountId}/{status}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					
					
	//========================================== CUSTOMER ACCOUNT ================================================	
					//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/customer-account/add/{applicationId}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/customer-account/get/all").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/customer-account/get/by-id/{id}").hasAnyAuthority("CUSTOMER","ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/customer-account/get/by-customer-id/{customerId}").hasAnyAuthority("CUSTOMER","ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/customer-account/get/by-accountholder-id/{accountHolderId}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/customer-account/get/by-account-id/{accountId}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/customer-account/get/by-customer-account/{customerId}/{accountId}").hasAnyAuthority("CUSTOMER","ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- put ------------------------------------------------
					.requestMatchers("/api/customer-account/update/account-holder/{id}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					
	//================================================ ACCOUNT REQUESTS ================================================
					//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/account-request/add/{accountId}/{requestType}").hasAnyAuthority("CUSTOMER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/account-request/get/all").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-request/get/by-id/{id}").hasAnyAuthority("CUSTOMER","ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-request/get/by-status/{status}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-request/get/by-account-id/{accountId}").hasAnyAuthority("CUSTOMER","ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- put ------------------------------------------------
					.requestMatchers("/api/account-request/update/accept/{requestId}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/account-request/update/reject/{requestId}").hasAnyAuthority("ACCOUNT_MANAGER", "JUNIOR_OPERATIONS_MANAGER", "SENIOR_OPERATIONS_MANAGER")
					
					
					
	//===================================================== LOAN PLAN ======================================================
					//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/loan-plan/add").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/loan-plan/get/all").hasAnyAuthority("CUSTOMER","LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-plan/get/by-id/{id}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-plan/get/by-loan-type/{loanType}").hasAnyAuthority("CUSTOMER","LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- put ------------------------------------------------
					.requestMatchers("/api/loan-plan/update").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					
					
					
					
	//================================================== LOAN OPENING APPLICATION =================================================
				//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/loan-opening-application/add/{accountId}/{loanPlanId}").hasAnyAuthority("CUSTOMER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/loan-opening-application/get/all").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-opening-application/get/by-id/{id}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-opening-application/get/by-account-id/{accountId}").hasAnyAuthority("CUSTOMER","LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-opening-application/get/by-loan-plan-id/{loanPlanId}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-opening-application/get/by-account-id-status/{accountId}/{status}").hasAnyAuthority("CUSTOMER","LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-opening-application/get/by-status/{status}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- put ------------------------------------------------
					.requestMatchers("/api/loan-opening-application/update/accept/{id}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-opening-application/update/reject/{id}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					
					
					
	//===================================================== LOAN ====================================================================
				//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/loan/add/{loanApplicationId}").hasAnyAuthority("CUSTOMER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/loan/get/all").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan/get/by-id/{id}").hasAnyAuthority("CUSTOMER","LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan/get/by-account-id/{accountId}").hasAnyAuthority("CUSTOMER","LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan/get/by-account-id-status/{accountId}/{status}").hasAnyAuthority("CUSTOMER","LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan/get/by-status/{status}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- put ------------------------------------------------
					.requestMatchers("/api/loan/update/close/{id}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan/update/penalty/{loanId}/{penalty}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan/update/due-date/{loanId}/{dueDate}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan/update/is-cleared/{loanId}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					
					
	//==================================================== LOAN PAYMENT  =============================================================
				//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/loan-payment/add/{loanId}/{amountPaid}").hasAnyAuthority("CUSTOMER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/loan-payment/get/all").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-payment/get/by-id/{id}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-payment/get/by-loan-id/{loanId}").hasAnyAuthority("CUSTOMER","LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					
	//=================================================== LOAN CLOSURE REQUEST ========================================================

				//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/loan-closure/add/{loanId}/{purpose}").hasAnyAuthority("CUSTOMER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/loan-closure/get/all").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-closure/get/by-id/{id}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-closure/get/by-loan-id/{loanId}").hasAnyAuthority("CUSTOMER","LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-closure/get/by-status/{status}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-closure/get/by-loan-id-status/{loanId}/{status}").hasAnyAuthority("CUSTOMER","LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
				//------------------------------------- put ------------------------------------------------
					.requestMatchers("/api/loan-closure/update/reject/{id}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					.requestMatchers("/api/loan-closure/update/accept/{id}").hasAnyAuthority("LOAN_OFFICER", "SENIOR_OPERATIONS_MANAGER")
					
					
					
	//===================================================== REGULATORY REPORT =========================================================
					//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/regulatory-report/generate").hasAnyAuthority( "SENIOR_OPERATIONS_MANAGER","JUNIOR_OPERATIONS_MANAGER","REPORT_MANAGER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/regulatory-report/get/all").hasAnyAuthority( "SENIOR_OPERATIONS_MANAGER","JUNIOR_OPERATIONS_MANAGER","REPORT_MANAGER")
					.requestMatchers("/api/regulatory-report/get/by-id/{id}").hasAnyAuthority( "SENIOR_OPERATIONS_MANAGER","JUNIOR_OPERATIONS_MANAGER","REPORT_MANAGER")
					.requestMatchers("/api/regulatory-report/get/by-date-range/{startDate}/{endDate}").hasAnyAuthority( "SENIOR_OPERATIONS_MANAGER","JUNIOR_OPERATIONS_MANAGER","REPORT_MANAGER")
					
	//================================================== FINANCIAL PERFORMANCE REPORT ====================================================
					//------------------------------------ post ---------------------------------------------	
					.requestMatchers("/api/financial-performance-report/generate").hasAnyAuthority( "SENIOR_OPERATIONS_MANAGER","JUNIOR_OPERATIONS_MANAGER","REPORT_MANAGER")
				//------------------------------------- get ----------------------------------------------
					.requestMatchers("/api/financial-performance-report/get/all").hasAnyAuthority( "SENIOR_OPERATIONS_MANAGER","JUNIOR_OPERATIONS_MANAGER","REPORT_MANAGER")
					.requestMatchers("/api/financial-performance-report/get/by-id/{id}").hasAnyAuthority( "SENIOR_OPERATIONS_MANAGER","JUNIOR_OPERATIONS_MANAGER","REPORT_MANAGER")
					.requestMatchers("/api/financial-performance-report/get/by-date-range/{startDate}/{endDate}").hasAnyAuthority( "SENIOR_OPERATIONS_MANAGER","JUNIOR_OPERATIONS_MANAGER","REPORT_MANAGER")
					
					
					
					
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
