package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.maverickbank.MaverickBank.enums.AccountStatus;
import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.LoanType;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.LoanOpeningApplication;
import com.maverickbank.MaverickBank.model.LoanPlan;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.LoanOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.LoanPlanRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.LoanOpeningApplicationService;
import com.maverickbank.MaverickBank.service.LoanService;

@SpringBootTest
class LoanOpeningApplicationServiceTest {

    @InjectMocks
    private LoanOpeningApplicationService loanOpeningApplicationService;

    @Mock
    private LoanOpeningApplicationRepository loanOpeningApplicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanPlanRepository loanPlanRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LoanService loanService;

    private LoanOpeningApplication sampleApplication1;
    private LoanOpeningApplication sampleApplication2;
    private Account sampleAccount1;
    private LoanPlan sampleLoanPlan1;
    private User sampleUser;
    private Principal samplePrincipal;

    @BeforeEach
    public void init() throws InvalidInputException {
        // Sample user
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("new_user");
        sampleUser.setStatus(ActiveStatus.ACTIVE);

        // Sample principal
        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("new_user");

        // Sample account
        sampleAccount1 = new Account();
        sampleAccount1.setId(301);
        sampleAccount1.setAccountNumber("11112222333");
        sampleAccount1.setAccountName("Primary Savings");
        sampleAccount1.setBalance(new BigDecimal("25000.00"));
        sampleAccount1.setAccountStatus(AccountStatus.OPEN);
        sampleAccount1.setKycCompliant(true);

        // Sample loan plan
        sampleLoanPlan1 = new LoanPlan();
        sampleLoanPlan1.setId(101);
        sampleLoanPlan1.setLoanName("Home Loan Basic");
        sampleLoanPlan1.setLoanType(LoanType.HOME_LOAN);
        sampleLoanPlan1.setPrincipalAmount(new BigDecimal("500000"));
        sampleLoanPlan1.setLoanTerm(60);
        sampleLoanPlan1.setInterestRate(new BigDecimal("8.5"));
        sampleLoanPlan1.setInstallmentAmount(new BigDecimal("10250"));
        sampleLoanPlan1.setRepaymentFrequency(12);
        sampleLoanPlan1.setPenaltyRate(new BigDecimal("2.0"));
        sampleLoanPlan1.setPrePaymentPenalty(new BigDecimal("1.5"));
        sampleLoanPlan1.setGracePeriod(3);

        // Sample application 1
        sampleApplication1 = new LoanOpeningApplication();
        sampleApplication1.setId(1);
        sampleApplication1.setAccount(sampleAccount1);
        sampleApplication1.setLoanPlan(sampleLoanPlan1);
        sampleApplication1.setPurpose("Renovation");
        sampleApplication1.setStatus(ApplicationStatus.PENDING);

        // Sample application 2
        sampleApplication2 = new LoanOpeningApplication();
        sampleApplication2.setId(2);
        sampleApplication2.setAccount(sampleAccount1);
        sampleApplication2.setLoanPlan(sampleLoanPlan1);
        sampleApplication2.setPurpose("Business Expansion");
        sampleApplication2.setStatus(ApplicationStatus.ACCEPTED);
    }
    
 //============================================== ADD ===========================================
    @Test
    public void testCreateLoanApplication() throws Exception {
        // Common mock – authenticated active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /*Case 1: success */
        when(accountRepository.findById(sampleAccount1.getId()))
                .thenReturn(Optional.of(sampleAccount1));
        when(loanPlanRepository.findById(sampleLoanPlan1.getId()))
                .thenReturn(Optional.of(sampleLoanPlan1));
        when(loanOpeningApplicationRepository.getOldLoanOpeningApplications(
                sampleAccount1.getId(), sampleLoanPlan1.getId(), ApplicationStatus.PENDING))
                .thenReturn(Arrays.asList());
        when(loanOpeningApplicationRepository.save(Mockito.any(LoanOpeningApplication.class)))
                .thenReturn(sampleApplication1);

        LoanOpeningApplication created = loanOpeningApplicationService.createLoanApplication(
                sampleAccount1.getId(), sampleLoanPlan1.getId(), "Renovation", samplePrincipal);

        assertEquals(sampleApplication1, created);
        assertEquals(sampleAccount1, created.getAccount());
        assertEquals(sampleLoanPlan1, created.getLoanPlan());
        assertEquals(ApplicationStatus.PENDING, created.getStatus());
        assertEquals("Renovation", created.getPurpose());

        /*Case 2: account not found*/
        when(accountRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex1 = assertThrows(ResourceNotFoundException.class, () -> {
            loanOpeningApplicationService.createLoanApplication(
                    999, sampleLoanPlan1.getId(), "Purpose", samplePrincipal);
        });
        assertEquals("No account record found with the given id...!!!", ex1.getMessage());

        /*Case 3: loan plan not found*/
        when(accountRepository.findById(sampleAccount1.getId()))
                .thenReturn(Optional.of(sampleAccount1));
        when(loanPlanRepository.findById(888)).thenReturn(Optional.empty());

        ResourceNotFoundException ex2 = assertThrows(ResourceNotFoundException.class, () -> {
            loanOpeningApplicationService.createLoanApplication(
                    sampleAccount1.getId(), 888, "Purpose", samplePrincipal);
        });
        assertEquals("No loan plan record found with the given id...!!!", ex2.getMessage());

        /* Case 4: duplicate pending application exists */
        when(loanPlanRepository.findById(sampleLoanPlan1.getId())).thenReturn(Optional.of(sampleLoanPlan1));
        when(loanOpeningApplicationRepository.getOldLoanOpeningApplications(
                sampleAccount1.getId(), sampleLoanPlan1.getId(), ApplicationStatus.PENDING))
                .thenReturn(Arrays.asList(sampleApplication1));

        InvalidActionException ex3 = assertThrows(InvalidActionException.class, () -> {
            loanOpeningApplicationService.createLoanApplication(
                    sampleAccount1.getId(), sampleLoanPlan1.getId(), "Purpose", samplePrincipal);
        });
        assertEquals("You have already applied for this same loan loan ...!!!", ex3.getMessage());
    }
    
    
    
    
    //======================================= GET =============================================
    @Test
    public void testGetAllLoanApplications() throws Exception {
        // Common setup – authenticated user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: success with existing applications */
        List<LoanOpeningApplication> appList = Arrays.asList(sampleApplication1, sampleApplication2);
        when(loanOpeningApplicationRepository.findAll()).thenReturn(appList);

        List<LoanOpeningApplication> result = loanOpeningApplicationService.getAllLoanApplications(samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleApplication1, result.get(0));
        assertEquals(sampleApplication2, result.get(1));

        
    }
    
    
    
    
    @Test
    public void testGetLoanApplicationById() throws Exception {
        // Common mock – active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: application exists */
        when(loanOpeningApplicationRepository.findById(1))
                .thenReturn(Optional.of(sampleApplication1));

        LoanOpeningApplication result =
                loanOpeningApplicationService.getById(1, samplePrincipal);

        assertEquals(sampleApplication1, result);
        assertEquals(1, result.getId());
        assertEquals(sampleAccount1, result.getAccount());

        /* Case 2: application not found */
        when(loanOpeningApplicationRepository.findById(2))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            loanOpeningApplicationService.getById(2, samplePrincipal);
        });
        assertEquals("No loan application record eas found with the given id...!!!", ex.getMessage());
    }

    
    
    @Test
    public void testGetLoanApplicationsByAccountId() throws Exception {
        // Common mock – authenticated active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /*Case 1: account exists, applications found */
        when(accountRepository.findById(sampleAccount1.getId()))
                .thenReturn(Optional.of(sampleAccount1));
        List<LoanOpeningApplication> appList = Arrays.asList(sampleApplication1, sampleApplication2);
        when(loanOpeningApplicationRepository.getByAccountId(sampleAccount1.getId()))
                .thenReturn(appList);

        List<LoanOpeningApplication> result =
                loanOpeningApplicationService.getByAccountId(sampleAccount1.getId(), samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleApplication1, result.get(0));
        assertEquals(sampleApplication2, result.get(1));

        /* Case 2: account exists, no applications */
        when(loanOpeningApplicationRepository.getByAccountId(sampleAccount1.getId()))
                .thenReturn(Arrays.asList());

        List<LoanOpeningApplication> emptyResult =
                loanOpeningApplicationService.getByAccountId(sampleAccount1.getId(), samplePrincipal);

        assertTrue(emptyResult.isEmpty());

        /*Case 3: account not found */
        int unknownAccountId = 999;
        when(accountRepository.findById(unknownAccountId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            loanOpeningApplicationService.getByAccountId(unknownAccountId, samplePrincipal);
        });
        assertEquals("No account record found with the given id...!!!", ex.getMessage());
    }



    @Test
    public void testGetLoanApplicationsByLoanPlanId() throws Exception {
        // Common mock – authenticated user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: loan plan exists, applications found */
        when(loanPlanRepository.findById(sampleLoanPlan1.getId()))
                .thenReturn(Optional.of(sampleLoanPlan1));
        List<LoanOpeningApplication> appList = Arrays.asList(sampleApplication1, sampleApplication2);
        when(loanOpeningApplicationRepository.getByLoanPlanId(sampleLoanPlan1.getId()))
                .thenReturn(appList);

        List<LoanOpeningApplication> result = loanOpeningApplicationService
                .getByLoanPlanId(sampleLoanPlan1.getId(), samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleApplication1, result.get(0));
        assertEquals(sampleApplication2, result.get(1));

        /* Case 2: loan plan exists, no applications */
        when(loanOpeningApplicationRepository.getByLoanPlanId(sampleLoanPlan1.getId()))
                .thenReturn(Arrays.asList());

        List<LoanOpeningApplication> emptyResult = loanOpeningApplicationService
                .getByLoanPlanId(sampleLoanPlan1.getId(), samplePrincipal);

        assertTrue(emptyResult.isEmpty());

        /*Case 3: loan plan not found */
        int invalidLoanPlanId = 777;
        when(loanPlanRepository.findById(invalidLoanPlanId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            loanOpeningApplicationService.getByLoanPlanId(invalidLoanPlanId, samplePrincipal);
        });
        assertEquals("No loan plan record found with the given id...!!!", ex.getMessage());
    }
    
    
    
    
    @Test
    public void testGetLoanApplicationsByAccountIdAndStatus() throws Exception {
        // Common mock -- authenticated, active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: account exists, applications with status found*/
        when(accountRepository.findById(sampleAccount1.getId()))
                .thenReturn(Optional.of(sampleAccount1));
        List<LoanOpeningApplication> pendingApps =
                Arrays.asList(sampleApplication1);          // PENDING
        when(loanOpeningApplicationRepository.getByAccountIdAndStatus(
                sampleAccount1.getId(), ApplicationStatus.PENDING))
                .thenReturn(pendingApps);

        List<LoanOpeningApplication> result =
                loanOpeningApplicationService.getByAccountIdAndStatus(
                        sampleAccount1.getId(), ApplicationStatus.PENDING, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(sampleApplication1, result.get(0));
        assertEquals(ApplicationStatus.PENDING, result.get(0).getStatus());

        /* Case 2: account exists, but no applications for status */
        when(loanOpeningApplicationRepository.getByAccountIdAndStatus(
                sampleAccount1.getId(), ApplicationStatus.ACCEPTED))
                .thenReturn(Arrays.asList());

        List<LoanOpeningApplication> emptyResult =
                loanOpeningApplicationService.getByAccountIdAndStatus(
                        sampleAccount1.getId(), ApplicationStatus.ACCEPTED, samplePrincipal);

        assertTrue(emptyResult.isEmpty());

        /*Case 3: account not found*/
        int unknownAccountId = 404;
        when(accountRepository.findById(unknownAccountId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            loanOpeningApplicationService.getByAccountIdAndStatus(
                    unknownAccountId, ApplicationStatus.PENDING, samplePrincipal);
        });
        assertEquals("No account record was found with the given id...!!!", ex.getMessage());
    }
    
    
    
    @Test
    public void testGetLoanApplicationsByStatus() throws Exception {
        // Common mock - authenticated, active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: PENDING applications returned  */
        // Ensure sampleApplication1 is PENDING
        sampleApplication1.setStatus(ApplicationStatus.PENDING);
        List<LoanOpeningApplication> pendingApps = Arrays.asList(sampleApplication1);

        when(loanOpeningApplicationRepository.getByStatus(ApplicationStatus.PENDING))
                .thenReturn(pendingApps);

        List<LoanOpeningApplication> resultPending =
                loanOpeningApplicationService.getLoanApplicationsByStatus(
                        ApplicationStatus.PENDING, samplePrincipal);

        assertEquals(1, resultPending.size());
        assertEquals(sampleApplication1, resultPending.get(0));
        assertEquals(ApplicationStatus.PENDING, resultPending.get(0).getStatus());

        /* Case 2: no applications for ACCEPTED status*/
        when(loanOpeningApplicationRepository.getByStatus(ApplicationStatus.ACCEPTED))
                .thenReturn(Arrays.asList());

        List<LoanOpeningApplication> resultAccepted =
                loanOpeningApplicationService.getLoanApplicationsByStatus(
                        ApplicationStatus.ACCEPTED, samplePrincipal);

        assertTrue(resultAccepted.isEmpty());
    }

    
    
    
 //====================================== UPDATE ==============================================
    
    @Test
    public void testAcceptLoanOpeningApplication() throws Exception {
        // Common mock – authenticated active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /*Case 1: success */
        sampleApplication1.setStatus(ApplicationStatus.PENDING);

        // Loan to return after acceptance
        Loan sampleLoan = new Loan();
        sampleLoan.setId(9001);

        when(loanOpeningApplicationRepository.findById(1))
                .thenReturn(Optional.of(sampleApplication1));
        when(loanOpeningApplicationRepository.save(sampleApplication1))
                .thenReturn(sampleApplication1);
        when(loanService.createLoan(sampleApplication1.getId(), samplePrincipal))
                .thenReturn(sampleLoan);

        Loan createdLoan = loanOpeningApplicationService
                .acceptLoanOpeningApplication(1, samplePrincipal);

        assertEquals(sampleLoan, createdLoan);
        assertEquals(ApplicationStatus.ACCEPTED, sampleApplication1.getStatus());

        /* Case 2: already ACCEPTED */
        sampleApplication1.setStatus(ApplicationStatus.ACCEPTED);
        when(loanOpeningApplicationRepository.findById(2))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException ex1 = assertThrows(InvalidActionException.class, () -> {
            loanOpeningApplicationService.acceptLoanOpeningApplication(2, samplePrincipal);
        });
        assertEquals("This loan application is already ACCEPTED...!!!", ex1.getMessage());

        /*Case 3: already REJECTED*/
        sampleApplication1.setStatus(ApplicationStatus.REJECTED);
        when(loanOpeningApplicationRepository.findById(3))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException ex2 = assertThrows(InvalidActionException.class, () -> {
            loanOpeningApplicationService.acceptLoanOpeningApplication(3, samplePrincipal);
        });
        assertEquals("This loan application is already REJECTED. Cannot perform action...!!!", ex2.getMessage());

        /* Case 4: application not found */
        when(loanOpeningApplicationRepository.findById(4))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex3 = assertThrows(ResourceNotFoundException.class, () -> {
            loanOpeningApplicationService.acceptLoanOpeningApplication(4, samplePrincipal);
        });
        assertEquals("No loan application record found with the given id...!!!", ex3.getMessage());
    }
    
    
    
    
    @Test
    public void testRejectLoanOpeningApplication() throws Exception {
        // Common mock – active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1:  */
        sampleApplication1.setStatus(ApplicationStatus.PENDING);

        when(loanOpeningApplicationRepository.findById(1))
                .thenReturn(Optional.of(sampleApplication1));
        when(loanOpeningApplicationRepository.save(sampleApplication1))
                .thenReturn(sampleApplication1);

        LoanOpeningApplication rejected =
                loanOpeningApplicationService.rejectLoanOpeningApplication(1, samplePrincipal);

        assertEquals(sampleApplication1, rejected);
        assertEquals(ApplicationStatus.REJECTED, rejected.getStatus());

        /* Case 2: Already ACCEPTED  */
        sampleApplication1.setStatus(ApplicationStatus.ACCEPTED);
        when(loanOpeningApplicationRepository.findById(2))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException ex1 = assertThrows(InvalidActionException.class, () -> {
            loanOpeningApplicationService.rejectLoanOpeningApplication(2, samplePrincipal);
        });
        assertEquals("This loan application is already ACCEPTED. Cannot perform action...!!!", ex1.getMessage());

        /* Case 3: Already REJECTED  */
        sampleApplication1.setStatus(ApplicationStatus.REJECTED);
        when(loanOpeningApplicationRepository.findById(3))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException ex2 = assertThrows(InvalidActionException.class, () -> {
            loanOpeningApplicationService.rejectLoanOpeningApplication(3, samplePrincipal);
        });
        assertEquals("This loan application is already REJECTED...!!!", ex2.getMessage());

        /* Case 4: Application Not Found  */
        when(loanOpeningApplicationRepository.findById(4))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex3 = assertThrows(ResourceNotFoundException.class, () -> {
            loanOpeningApplicationService.rejectLoanOpeningApplication(4, samplePrincipal);
        });
        assertEquals("No loan application record found with the given id...!!!", ex3.getMessage());
    }



    
    
    

    @AfterEach
    public void afterTest() {
        sampleApplication1 = null;
        sampleApplication2 = null;
        sampleAccount1 = null;
        sampleLoanPlan1 = null;
        sampleUser = null;
        samplePrincipal = null;
    }
}

