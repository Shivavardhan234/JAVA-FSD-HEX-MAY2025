package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.maverickbank.MaverickBank.model.RegulatoryReport;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.CustomerRepository;
import com.maverickbank.MaverickBank.repository.LoanRepository;
import com.maverickbank.MaverickBank.repository.RegulatoryReportRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.RegulatoryReportService;

@SpringBootTest
class RegulatoryReportServiceTest {

    @InjectMocks
    private RegulatoryReportService regulatoryReportService;

    @Mock
    private RegulatoryReportRepository regulatoryReportRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LoanRepository loanRepository;

    private User sampleUser;
    private Principal samplePrincipal;
    private RegulatoryReport sampleReport1;
    private RegulatoryReport sampleReport2;

    @BeforeEach
    public void init() throws InvalidInputException {
        sampleUser = new User();
        sampleUser.setId(1002);
        sampleUser.setUsername("admin.user");
        sampleUser.setStatus(ActiveStatus.ACTIVE);
        sampleUser.setRole(Role.ADMIN);

        samplePrincipal=mock(Principal.class);

        sampleReport1 = new RegulatoryReport();
        sampleReport1.setId(1);
        sampleReport1.setReportDate(LocalDate.of(2024, 10, 1));
        sampleReport1.setTotalCustomers(100);
        sampleReport1.setTotalAccounts(80);
        sampleReport1.setTotalActiveLoans(25);
        sampleReport1.setKycCompliantAccounts(75);

        sampleReport2 = new RegulatoryReport();
        sampleReport2.setId(2);
        sampleReport2.setReportDate(LocalDate.of(2025, 1, 1));
        sampleReport2.setTotalCustomers(120);
        sampleReport2.setTotalAccounts(90);
        sampleReport2.setTotalActiveLoans(30);
        sampleReport2.setKycCompliantAccounts(85);
        
        when(samplePrincipal.getName()).thenReturn("admin.user");
    }

    
 //========================================== ADD ============================================= 
    
    @Test
    public void testGenerateRegulatoryReport() throws Exception {
       
        when(userRepository.getByUsername("admin.user")).thenReturn(sampleUser);

       //CASE 1 success
        when(customerRepository.count()).thenReturn(100L);     
        when(accountRepository.count()).thenReturn(80L);
        when(loanRepository.countByStatus(LoanStatus.ACTIVE)).thenReturn(25);
        when(accountRepository.countByKycCompleted(true)).thenReturn(75);
        when(regulatoryReportRepository.save(any(RegulatoryReport.class)))
                .thenReturn(sampleReport1);

        RegulatoryReport generated = regulatoryReportService
                .generateRegulatoryReport(samplePrincipal);

        assertEquals(sampleReport1, generated);
        assertEquals(100, generated.getTotalCustomers());
        assertEquals(80, generated.getTotalAccounts());
        assertEquals(25, generated.getTotalActiveLoans());
        assertEquals(75, generated.getKycCompliantAccounts());

      
    }
    
    
 //======================================== GET =============================================== 
    @Test
    public void testGetAllRegulatoryReports() throws Exception {
        // Common mock – active user
        when(userRepository.getByUsername("admin.user")).thenReturn(sampleUser);

        
        List<RegulatoryReport> reportList = Arrays.asList(sampleReport1, sampleReport2);
        Page<RegulatoryReport> mockPage = new PageImpl<>(reportList);
        when(regulatoryReportRepository.findAll(PageRequest.of(0, 10))).thenReturn(mockPage);

        List<RegulatoryReport> result = regulatoryReportService.getAllReports(0,10,samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleReport1, result.get(0));
        assertEquals(sampleReport2, result.get(1));

        
    }
    
    
    
    
    @Test
    public void testGetRegulatoryReportById() throws Exception {
        // Common mock – authenticated, active user
        when(userRepository.getByUsername("admin.user")).thenReturn(sampleUser);

        /* ---------- Case 1: report exists ---------- */
        when(regulatoryReportRepository.findById(1)).thenReturn(Optional.of(sampleReport1));

        RegulatoryReport result = regulatoryReportService.getReportById(1, samplePrincipal);

        assertEquals(sampleReport1, result);
        assertEquals(1, result.getId());
        assertEquals(LocalDate.of(2024, 10, 1), result.getReportDate());

        /* ---------- Case 2: report not found ---------- */
        when(regulatoryReportRepository.findById(2)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            regulatoryReportService.getReportById(2, samplePrincipal);
        });
        assertEquals("Regulatory report not found with id: 2", ex.getMessage());
    }

    
    
    @Test
    public void testGetRegulatoryReportsByDateRange() throws Exception {
        // Common mock – active user
        when(userRepository.getByUsername("admin.user")).thenReturn(sampleUser);

        LocalDate startDate = LocalDate.of(2024, 9, 1);
        LocalDate endDate   = LocalDate.of(2025, 2, 1);

        /* -------- Case 1: reports found in range -------- */
        List<RegulatoryReport> reportsInRange = Arrays.asList(sampleReport1, sampleReport2);
        when(regulatoryReportRepository.getByReportDateRange(startDate, endDate,PageRequest.of(0, 10)))
                .thenReturn(reportsInRange);

        List<RegulatoryReport> result =
                regulatoryReportService.getReportsByDateRange(0,10,startDate, endDate, samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleReport1, result.get(0));
        assertEquals(sampleReport2, result.get(1));

        /* -------- Case 2: empty list returned -------- */
        when(regulatoryReportRepository.getByReportDateRange(startDate, endDate,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException ex1 = assertThrows(ResourceNotFoundException.class, () -> {
            regulatoryReportService.getReportsByDateRange(0,10,startDate, endDate, samplePrincipal);
        });
        assertEquals("No Regulatory report found in the given date range...!!!", ex1.getMessage());

      
    }



    
    
    
    
    
    
    
    
    
    @AfterEach
    public void afterTest() {
        sampleUser = null;
        samplePrincipal = null;
        sampleReport1 = null;
        sampleReport2 = null;
    }
}

