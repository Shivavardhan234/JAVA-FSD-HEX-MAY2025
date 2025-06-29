package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.LoanStatus;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.FinancialPerformanceReport;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.FinancialPerformanceReportRepository;
import com.maverickbank.MaverickBank.repository.LoanPaymentRepository;
import com.maverickbank.MaverickBank.repository.LoanRepository;
import com.maverickbank.MaverickBank.repository.TransactionRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.FinancialPerformanceReportService;

@SpringBootTest
class FinancialPerformanceReportServiceTest {

    @InjectMocks
    private FinancialPerformanceReportService financialPerformanceReportService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private FinancialPerformanceReportRepository financialPerformanceReportRepository;

    @Mock
    private LoanPaymentRepository loanPaymentRepository;

    private User sampleUser;
    private Principal samplePrincipal;
    private FinancialPerformanceReport sampleReport1;
    private FinancialPerformanceReport sampleReport2;

    @BeforeEach
    public void init() throws InvalidInputException {
        sampleUser = new User();
        sampleUser.setId(1001);
        sampleUser.setUsername("john.doe");
        sampleUser.setStatus(ActiveStatus.ACTIVE);
        sampleUser.setRole(Role.ADMIN);

        samplePrincipal=mock(Principal.class);

        sampleReport1 = new FinancialPerformanceReport();
        sampleReport1.setId(1);
        sampleReport1.setReportDate(LocalDate.of(2024, 12, 31));
        sampleReport1.setTotalDeposits(new BigDecimal("1000000.00"));
        sampleReport1.setTotalNumberOfLoansIssued(20);
        sampleReport1.setTotalNumberOfActiveLoans(15);
        sampleReport1.setTotalLoanAmountIssued(new BigDecimal("500000.00"));
        sampleReport1.setTotalLoanRepayment(new BigDecimal("300000.00"));
        sampleReport1.setTotalTransactions(500);
        sampleReport1.setTotaltransactionAmount(new BigDecimal("2000000.00"));

        sampleReport2 = new FinancialPerformanceReport();
        sampleReport2.setId(2);
        sampleReport2.setReportDate(LocalDate.of(2025, 1, 31));
        sampleReport2.setTotalDeposits(new BigDecimal("2000000.00"));
        sampleReport2.setTotalNumberOfLoansIssued(25);
        sampleReport2.setTotalNumberOfActiveLoans(18);
        sampleReport2.setTotalLoanAmountIssued(new BigDecimal("700000.00"));
        sampleReport2.setTotalLoanRepayment(new BigDecimal("400000.00"));
        sampleReport2.setTotalTransactions(600);
        sampleReport2.setTotaltransactionAmount(new BigDecimal("2500000.00"));
        
        when(samplePrincipal.getName()).thenReturn("john.doe");
    }
    
//============================================ ADD ============================================
    @Test
    public void testGenerateReport() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("john.doe")).thenReturn(sampleUser);
        when(accountRepository.getTotalBankBalance()).thenReturn(new BigDecimal("1000000.00"));
        when(loanRepository.getTotalLoanPrincipalIssued()).thenReturn(new BigDecimal("500000.00"));
        when(loanPaymentRepository.getTotalLoanRepayment()).thenReturn(new BigDecimal("300000.00"));
        when(transactionRepository.getTotalTransactionAmount()).thenReturn(new BigDecimal("2000000.00"));
        when(transactionRepository.getTotalNumberOfTransactions()).thenReturn(500);
        when(loanRepository.count()).thenReturn(20l);
        when(loanRepository.getTotalActiveLoans(LoanStatus.ACTIVE)).thenReturn(15);
        when(financialPerformanceReportRepository.save(any(FinancialPerformanceReport.class))).thenReturn(sampleReport1);

        FinancialPerformanceReport generatedReport = financialPerformanceReportService.generateReport(samplePrincipal);

        assertEquals(LocalDate.of(2024, 12, 31), generatedReport.getReportDate());
        assertEquals(new BigDecimal("1000000.00"), generatedReport.getTotalDeposits());
        assertEquals(new BigDecimal("500000.00"), generatedReport.getTotalLoanAmountIssued());
        assertEquals(new BigDecimal("300000.00"), generatedReport.getTotalLoanRepayment());
        assertEquals(new BigDecimal("2000000.00"), generatedReport.getTotaltransactionAmount());
        assertEquals(500, generatedReport.getTotalTransactions());
        assertEquals(20, generatedReport.getTotalNumberOfLoansIssued());
        assertEquals(15, generatedReport.getTotalNumberOfActiveLoans());

        // Case 2: Bank balance is null
        when(accountRepository.getTotalBankBalance()).thenReturn(null);

        InvalidInputException e1 = assertThrows(InvalidInputException.class, () -> {
            financialPerformanceReportService.generateReport(samplePrincipal);
        });
        assertEquals("Invalid total deposits provided. please provide appropriate total deposits...!!!", e1.getMessage());

        // Case 3: Loan amount issued is null
        when(accountRepository.getTotalBankBalance()).thenReturn(new BigDecimal("1000000.00"));
        when(loanRepository.getTotalLoanPrincipalIssued()).thenReturn(null);

        InvalidInputException e2 = assertThrows(InvalidInputException.class, () -> {
            financialPerformanceReportService.generateReport(samplePrincipal);
        });
        assertEquals("Invalid total loan amount. please provide appropriate loan amount...!!!", e2.getMessage());

        // Case 4: Loan repayment is null
        when(loanRepository.getTotalLoanPrincipalIssued()).thenReturn(new BigDecimal("500000.00"));
        when(loanPaymentRepository.getTotalLoanRepayment()).thenReturn(null);

        InvalidInputException e3 = assertThrows(InvalidInputException.class, () -> {
            financialPerformanceReportService.generateReport(samplePrincipal);
        });
        assertEquals("Invalid total loan repayment. please provide appropriate loan repayment...!!!", e3.getMessage());

        // Case 5: Transaction amount is null
        when(loanPaymentRepository.getTotalLoanRepayment()).thenReturn(new BigDecimal("300000.00"));
        when(transactionRepository.getTotalTransactionAmount()).thenReturn(null);

        InvalidInputException e4 = assertThrows(InvalidInputException.class, () -> {
            financialPerformanceReportService.generateReport(samplePrincipal);
        });
        assertEquals("Invalid total transaction amount provided. Please provide appropriate total transaction amount...!!!", e4.getMessage());

       
    }
    
    
    
    
    
    
    
//========================================== GET ==============================================
    @Test
    public void testGetAllReports() throws Exception {
        // Case 1 – two reports returned
        when(userRepository.getByUsername("john.doe")).thenReturn(sampleUser);
        List<FinancialPerformanceReport> reportList = Arrays.asList(sampleReport1, sampleReport2);
        Page<FinancialPerformanceReport> mockPage = new PageImpl<>(reportList);
        when(financialPerformanceReportRepository.findAll(PageRequest.of(0, 10))).thenReturn(mockPage);

        List<FinancialPerformanceReport> result = financialPerformanceReportService.getAllReports(0,10,samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleReport1, result.get(0));
        assertEquals(sampleReport2, result.get(1));

        // Case 2 – no reports in the DB
        Page<FinancialPerformanceReport> emptyMockPage = new PageImpl<>(Arrays.asList());
        when(financialPerformanceReportRepository.findAll(PageRequest.of(0, 10))).thenReturn(emptyMockPage);

        List<FinancialPerformanceReport> emptyResult = financialPerformanceReportService.getAllReports(0,10,samplePrincipal);

        assertTrue(emptyResult.isEmpty());
    }

    
    
    @Test
    public void testGetReportById() throws Exception {
        // Case 1 – Report exists
        when(userRepository.getByUsername("john.doe")).thenReturn(sampleUser);
        when(financialPerformanceReportRepository.findById(1)).thenReturn(Optional.of(sampleReport1));

        FinancialPerformanceReport result = financialPerformanceReportService.getReportById(1, samplePrincipal);

        assertEquals(sampleReport1, result);
        assertEquals(1, result.getId());
        assertEquals(LocalDate.of(2024, 12, 31), result.getReportDate());

        // Case 2 – Report not found
        when(financialPerformanceReportRepository.findById(2)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            financialPerformanceReportService.getReportById(2, samplePrincipal);
        });
        assertEquals("Report not found with id...!!!", e.getMessage());
    }

    
    
    
    @Test
    public void testGetReportsByDateRange() throws Exception {
        // Common mock for authenticated user
        when(userRepository.getByUsername("john.doe")).thenReturn(sampleUser);

        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate   = LocalDate.of(2025, 1, 31);

        // Case 1 – reports found within range
        List<FinancialPerformanceReport> reportsInRange = Arrays.asList(sampleReport1, sampleReport2);
        when(financialPerformanceReportRepository.getReportsByDates(startDate, endDate, PageRequest.of(0, 10)))
                .thenReturn(reportsInRange);

        List<FinancialPerformanceReport> result =
                financialPerformanceReportService.getReportsByDateRange(0,10,startDate, endDate, samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleReport1, result.get(0));
        assertEquals(sampleReport2, result.get(1));

        // Case 2 – no reports found (empty list)
        when(financialPerformanceReportRepository.getReportsByDates(startDate, endDate, PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e1 = assertThrows(ResourceNotFoundException.class, () -> {
            financialPerformanceReportService.getReportsByDateRange(0,10,startDate, endDate, samplePrincipal);
        });
        assertEquals("No Financial Performance Reports found between given dates...!!!", e1.getMessage());

       
    }


    
    
    
    
    
    
    

    @AfterEach
    public void afterTest() {
        sampleUser = null;
        samplePrincipal = null;
        sampleReport1 = null;
        sampleReport2 = null;
    }
}

