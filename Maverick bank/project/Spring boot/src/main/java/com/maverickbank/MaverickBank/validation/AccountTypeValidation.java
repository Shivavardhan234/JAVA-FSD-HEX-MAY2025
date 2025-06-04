package com.maverickbank.MaverickBank.validation;

import java.math.BigDecimal;

import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.exception.InvalidInputException;

public class AccountTypeValidation {
	public static void validateAccountTypeId(int id) throws InvalidInputException {
        if (id <= 0) {
            throw new InvalidInputException("Account Type ID is invalid. It must be greater than zero...!!!");
        }
        return;
    }

    public static void validateAccountType(BankAccountType accountType) throws InvalidInputException {
        if (accountType == null) {
            throw new InvalidInputException("Account Type cannot be null.");
        }
        return;
    }

    public static void validateInterestRate(BigDecimal interestRate) throws InvalidInputException {
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Invalid intrest rate provided. Please provide appropriate intrest rate...!!!");
        }
        return;
    }

    public static void validateMinimumBalance(BigDecimal minimumBalance) throws InvalidInputException {
        if (minimumBalance == null || minimumBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Invalid Minimum balance provided. Please provide appropriate min balance...!!!");
        }
        return;
    }

    public static void validateTransactionLimit(int transactionLimit) throws InvalidInputException {
        if (transactionLimit < 0) {
            throw new InvalidInputException("Invalid transaction limit provided. Transaction limit should not be less than zero...!!!");
        }
        return;
    }

    public static void validateTransactionAmountLimit(BigDecimal transactionAmountLimit) throws InvalidInputException {
        if (transactionAmountLimit == null || transactionAmountLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Invalid transaction amount limit provided. Please provide appropriate transaction amount limit...!!!");
        }
        return;
    }

    public static void validateWithdrawLimit(int withdrawLimit) throws InvalidInputException {
        if (withdrawLimit < 0) {
            throw new InvalidInputException("Invalid Withdraw limit provided. Withdraw limit should not be less than zero...!!!");
        }
        return;
    }

}
