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
					//user 
					.requestMatchers("/api/user/add").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/user/token/v2").authenticated()
					.requestMatchers("/api/user/token/v1").authenticated()
					.requestMatchers("/api/user/get-all").hasAnyAuthority("ADMIN")
					//Customer
					.requestMatchers("/api/customer/signup").permitAll()
					.requestMatchers("/api/customer/get-all").hasAnyAuthority("ADMIN")
					//Employee
					.requestMatchers("/api/employee/add").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get-all").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get-by-username").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get-by-email").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/employee/get-by-contactNumber").hasAnyAuthority("ADMIN")
					//CIO
					.requestMatchers("/api/cio/add").permitAll()
					.requestMatchers("/api/cio/get-all").hasAnyAuthority("ADMIN")
					//Branch
					.requestMatchers("/api/branch/get/all").authenticated()
					.requestMatchers("/api/branch/get/by-name").authenticated()
					.requestMatchers("/api/branch/get/by-state").authenticated()
					.requestMatchers("/api/branch/add").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/branch/activate").hasAnyAuthority("ADMIN")
					.requestMatchers("/api/branch/deactivate").hasAnyAuthority("ADMIN")
					//Account type
					.requestMatchers("/api/account-type/get-all").authenticated()
					.requestMatchers("/api/account-type/get-by-name{type}").authenticated()
					//Account Opening Application
					.requestMatchers("/api/account-opening-application/add").hasAnyAuthority("CUSTOMER")
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
