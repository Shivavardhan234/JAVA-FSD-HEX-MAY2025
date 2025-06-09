package com.maverickbank.MaverickBank.validation;

import java.math.BigDecimal;

import com.maverickbank.MaverickBank.enums.AccountStatus;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.Branch;

public class AccountValidation {
	
	 public  static void validateId(int id) throws InvalidInputException  {
			if( id<=0) {
				throw new InvalidInputException("Account ID is Invalid. Please enter appropriate Account ID...!!!");
			}
			return;
		}

		



		public static  void validateAccountNumber(String accountNumber) throws InvalidInputException {
			if(accountNumber==null || accountNumber.trim().isEmpty() || !accountNumber.matches("^[0-9]{11}$")) {
				throw new InvalidInputException("Invalid account number provided. Please provide appropriate account number...!!!");
			}
			return;
		}




		public static  void validateAccountName(String accountName) throws InvalidInputException{
			if( accountName.trim().isEmpty() ) {
				throw new InvalidInputException("Invalid account name  provided. Please provide appropriate account name...!!!");
			}
			return;
		}




		public  static void validateBranch(Branch branch)throws InvalidInputException {
			if(branch==null ) {
				throw new InvalidInputException("Invalid branch object provided. Please provide appropriate branch object...!!!");
			}
			return;
		}




		public static  void validateBalance(BigDecimal balance) throws InvalidInputException{
			if(balance==null || balance.compareTo(BigDecimal.ZERO) < 0) {
				throw new InvalidInputException("Invalid balance provided. Please provide appropriate account balance...!!!");
			}
			return;
		}


		public static void validateAccountStatus(AccountStatus accountStatus) throws InvalidInputException{
			if(accountStatus==null) {
				throw new InvalidInputException("Null account status provided. Account status should not be null...!!!");
			}
			return;
		}




		
		
		
		public static void validateAccountType(AccountType accountType) throws InvalidInputException {
	        if (accountType == null) {
	            throw new InvalidInputException("Null account type object provided. Please provide appropriate account type object...!!!");
	        }
	        return;
	    }
		
		
		public static void validateAccountObject(Account account) throws InvalidInputException {
	        if (account == null) {
	            throw new InvalidInputException("Null account type object provided. Please provide appropriate account type object...!!!");
	        }
	        return;
	    }
		
		
		public static void validateAccount(Account account) throws InvalidInputException {
	       validateAccountObject(account);
	       validateAccountType(account.getAccountType());
	       
	       validateBranch(account.getBranch());
	       
	        return;
	    }
		

}
