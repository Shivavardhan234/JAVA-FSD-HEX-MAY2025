package com.maverickbank.MaverickBank.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.model.RegulatoryReport;

public class RegulatoryReportValidation {

    public static void validateId(int id) throws InvalidInputException {
        if (id <= 0) {
            throw new InvalidInputException("Invalid Regulatory Report ID. Please provide a valid ID...!!!");
        }
    }

    public static void validateReportDate(LocalDate reportDate) throws InvalidInputException {
        if (reportDate == null || reportDate.isAfter(LocalDate.now())) {
            throw new InvalidInputException("Invalid report date. Please provide a valid date...!!!");
        }
    }

    public static void validateTotalCustomers(int totalCustomers) throws InvalidInputException {
        if (totalCustomers < 0) {
            throw new InvalidInputException("Total customers cannot be negative...!!!");
        }
    }

    public static void validateTotalAccounts(int totalAccounts) throws InvalidInputException {
        if (totalAccounts < 0) {
            throw new InvalidInputException("Total accounts cannot be negative...!!!");
        }
    }

    public static void validateTotalActiveLoans(int totalActiveLoans) throws InvalidInputException {
        if (totalActiveLoans < 0) {
            throw new InvalidInputException("Total active loans cannot be negative...!!!");
        }
    }

    public static void validateOutstandingLoanAmount(BigDecimal totalOutstandingLoans) throws InvalidInputException {
        if (totalOutstandingLoans == null || totalOutstandingLoans.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Invalid total outstanding loan amount...!!!");
        }
    }

    public static void validateKycCompliantAccounts(int kycCompliantAccounts) throws InvalidInputException {
        if (kycCompliantAccounts < 0) {
            throw new InvalidInputException("KYC compliant account count cannot be negative...!!!");
        }
    }

    public static void validateRegulatoryReportObject(RegulatoryReport report) throws InvalidInputException {
        if (report == null) {
            throw new InvalidInputException("Null RegulatoryReport object provided. Please provide a valid object...!!!");
        }
    }
}

