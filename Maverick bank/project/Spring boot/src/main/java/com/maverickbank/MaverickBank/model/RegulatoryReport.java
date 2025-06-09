package com.maverickbank.MaverickBank.model;


import java.time.LocalDate;

import com.maverickbank.MaverickBank.exception.InvalidInputException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "regulatory_report")
public class RegulatoryReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Column(name = "total_customers", nullable = false)
    private int totalCustomers;

    @Column(name = "total_accounts", nullable = false)
    private int totalAccounts;

    @Column(name = "total_active_loans", nullable = false)
    private int totalActiveLoans;


    @Column(name = "kyc_compliant_accounts", nullable = false)
    private int kycCompliantAccounts;



    // ---------------------- Constructors ----------------------

    public RegulatoryReport() {}

    public RegulatoryReport(int id, LocalDate reportDate, int totalCustomers, int totalAccounts,int totalActiveLoans, int kycCompliantAccounts) throws InvalidInputException { 
		setId(id);
		setReportDate(reportDate);
		setTotalCustomers(totalCustomers);
		setTotalAccounts(totalAccounts);
		setTotalActiveLoans(totalActiveLoans);
		setKycCompliantAccounts(kycCompliantAccounts);
		}


    // ---------------------- Getters ----------------------

    public int getId() {
        return id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public int getTotalAccounts() {
        return totalAccounts;
    }

    public int getTotalActiveLoans() {
        return totalActiveLoans;
    }

    
    public int getKycCompliantAccounts() {
        return kycCompliantAccounts;
    }

    

    // ---------------------- Setters with Validation ----------------------

    public void setId(int id) throws InvalidInputException {
        if (id <= 0) {
            throw new InvalidInputException("Invalid regulatory report ID provided.");
        }
        this.id = id;
    }

    public void setReportDate(LocalDate reportDate) throws InvalidInputException {
        if (reportDate == null || reportDate.isAfter(LocalDate.now())) {
            throw new InvalidInputException("Invalid report date provided.");
        }
        this.reportDate = reportDate;
    }

    public void setTotalCustomers(int totalCustomers) throws InvalidInputException {
        if (totalCustomers < 0) {
            throw new InvalidInputException("Total customers cannot be negative.");
        }
        this.totalCustomers = totalCustomers;
    }

    public void setTotalAccounts(int totalAccounts) throws InvalidInputException {
        if (totalAccounts < 0) {
            throw new InvalidInputException("Total accounts cannot be negative.");
        }
        this.totalAccounts = totalAccounts;
    }

    public void setTotalActiveLoans(int totalActiveLoans) throws InvalidInputException {
        if (totalActiveLoans < 0) {
            throw new InvalidInputException("Total active loans cannot be negative.");
        }
        this.totalActiveLoans = totalActiveLoans;
    }

   

    public void setKycCompliantAccounts(int kycCompliantAccounts) throws InvalidInputException {
        if (kycCompliantAccounts < 0) {
            throw new InvalidInputException("KYC compliant account count cannot be negative.");
        }
        this.kycCompliantAccounts = kycCompliantAccounts;
    }

   
}

