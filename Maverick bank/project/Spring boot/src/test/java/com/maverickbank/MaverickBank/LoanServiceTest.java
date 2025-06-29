package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.maverickbank.MaverickBank.enums.AccountStatus;
import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.LoanStatus;
import com.maverickbank.MaverickBank.enums.LoanType;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.LoanOpeningApplication;
import com.maverickbank.MaverickBank.model.LoanPayment;
import com.maverickbank.MaverickBank.model.LoanPlan;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.LoanOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.LoanPaymentRepository;
import com.maverickbank.MaverickBank.repository.LoanRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.AccountService;
import com.maverickbank.MaverickBank.service.LoanService;

@SpringBootTest
class LoanServiceTest {

    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private LoanOpeningApplicationRepository loanOpeningApplicationRepository;
    @Mock
    private LoanPaymentRepository loanPaymentRepository;

    private Account sampleAccount1;
    private LoanPlan sampleLoanPlan1;
    private LoanOpeningApplication sampleApplication1;
    private User sampleUser;
    private Principal samplePrincipal;

    @BeforeEach
    public void init() throws InvalidInputException {
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("test_user");
        sampleUser.setStatus(ActiveStatus.ACTIVE);

        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("test_user");

        sampleAccount1 = new Account();
        sampleAccount1.setId(301);
        sampleAccount1.setAccountNumber("11112222333");
        sampleAccount1.setAccountName("Primary Savings");
        sampleAccount1.setBalance(new BigDecimal("25000.00"));
        sampleAccount1.setAccountStatus(AccountStatus.OPEN);
        sampleAccount1.setKycCompliant(true);

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

        sampleApplication1 = new LoanOpeningApplication();
        sampleApplication1.setId(201);
        sampleApplication1.setAccount(sampleAccount1);
        sampleApplication1.setLoanPlan(sampleLoanPlan1);
        sampleApplication1.setPurpose("Buy House");
        sampleApplication1.setStatus(ApplicationStatus.ACCEPTED);
    }
    
    
    
 //====================================== ADD =================================================
    @Test
    public void testCreateLoan() throws Exception {
        // Common mock – authenticated active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1 – success (application ACCEPTED) ─────────── */
        
        sampleApplication1.setStatus(ApplicationStatus.ACCEPTED);

        // Loan object that repository will return after save
        Loan savedLoan = new Loan();
        savedLoan.setId(501);
        savedLoan.setAccount(sampleAccount1);
        savedLoan.setLoanPlan(sampleLoanPlan1);
        savedLoan.setLoanStartDate(LocalDate.now());
        savedLoan.setDisbursementDate(savedLoan.getLoanStartDate());
        savedLoan.setLoanEndDate(savedLoan.getLoanStartDate()
                              .plusMonths(sampleLoanPlan1.getLoanTerm()));
        savedLoan.setStatus(LoanStatus.ACTIVE);
        savedLoan.setTotalPenalty(BigDecimal.ZERO);
        savedLoan.setDueDate(savedLoan.getLoanStartDate()
                           .plusMonths(sampleLoanPlan1.getRepaymentFrequency()));
        savedLoan.setCleared(false);

        when(loanOpeningApplicationRepository.findById(sampleApplication1.getId()))
                .thenReturn(Optional.of(sampleApplication1));
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        Loan resultLoan =
                loanService.createLoan(sampleApplication1.getId(), samplePrincipal);

        assertEquals(savedLoan, resultLoan);
        assertEquals(sampleAccount1, resultLoan.getAccount());
        assertEquals(sampleLoanPlan1, resultLoan.getLoanPlan());
        assertEquals(LoanStatus.ACTIVE, resultLoan.getStatus());
        assertFalse(resultLoan.isCleared());

        /*Case 2 – application not APPROVED (PENDING) */
        sampleApplication1.setStatus(ApplicationStatus.PENDING);
        when(loanOpeningApplicationRepository.findById(202))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException ex1 = assertThrows(InvalidActionException.class, () -> {
            loanService.createLoan(202, samplePrincipal);
        });
        assertEquals("Loan application is not approved. Cannot create loan...!!!", ex1.getMessage());

        /* Case 3 - application ID not found */
        when(loanOpeningApplicationRepository.findById(303))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex2 = assertThrows(ResourceNotFoundException.class, () -> {
            loanService.createLoan(303, samplePrincipal);
        });
        assertEquals("No loan application found with the given ID...!!!", ex2.getMessage());
    }

    
    
    
    
//============================================= GET ===========================================
    @Test
    public void testGetAllLoans() throws Exception {
        // Setup: mock user and principal
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        // Case 1: success
        Loan loan1 = new Loan();
        loan1.setId(1);
        loan1.setAccount(sampleAccount1);
        loan1.setLoanPlan(sampleLoanPlan1);
        loan1.setLoanStartDate(LocalDate.now());
        loan1.setDisbursementDate(LocalDate.now());
        loan1.setLoanEndDate(LocalDate.now().plusMonths(60));
        loan1.setStatus(LoanStatus.ACTIVE);
        loan1.setTotalPenalty(BigDecimal.ZERO);
        loan1.setDueDate(LocalDate.now().plusMonths(12));
        loan1.setCleared(false);

        Loan loan2 = new Loan();
        loan2.setId(2);
        loan2.setAccount(sampleAccount1);
        loan2.setLoanPlan(sampleLoanPlan1);
        loan2.setLoanStartDate(LocalDate.now().minusMonths(1));
        loan2.setDisbursementDate(LocalDate.now().minusMonths(1));
        loan2.setLoanEndDate(LocalDate.now().plusMonths(59));
        loan2.setStatus(LoanStatus.ACTIVE);
        loan2.setTotalPenalty(BigDecimal.ZERO);
        loan2.setDueDate(LocalDate.now().plusMonths(11));
        loan2.setCleared(false);

        List<Loan> loanList = Arrays.asList(loan1, loan2);
        Page<Loan> mockPage = new PageImpl<>(loanList);
        when(loanRepository.findAll(PageRequest.of(0,10))).thenReturn(mockPage);

        List<Loan> result = loanService.getAllLoans(0,10,samplePrincipal);
        assertEquals(2, result.size());
        assertEquals(loan1.getId(), result.get(0).getId());
        assertEquals(loan2.getId(), result.get(1).getId());
    }

    
    
    @Test
    public void testGetLoanById() throws Exception {
        // Mock current authenticated user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        // Case 1: success - loan found
        Loan loan = new Loan();
        loan.setId(501);
        loan.setAccount(sampleAccount1);
        loan.setLoanPlan(sampleLoanPlan1);
        loan.setLoanStartDate(LocalDate.now());
        loan.setDisbursementDate(LocalDate.now());
        loan.setLoanEndDate(LocalDate.now().plusMonths(sampleLoanPlan1.getLoanTerm()));
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setTotalPenalty(BigDecimal.ZERO);
        loan.setDueDate(LocalDate.now().plusMonths(sampleLoanPlan1.getRepaymentFrequency()));
        loan.setCleared(false);

        when(loanRepository.findById(501)).thenReturn(Optional.of(loan));

        Loan result = loanService.getLoanById(501, samplePrincipal);

        assertEquals(501, result.getId());
        assertEquals(sampleAccount1, result.getAccount());
        assertEquals(sampleLoanPlan1, result.getLoanPlan());
        assertEquals(LoanStatus.ACTIVE, result.getStatus());
        assertFalse(result.isCleared());

        // Case 2: loan not found
        when(loanRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            loanService.getLoanById(999, samplePrincipal);
        });
        assertEquals("No loan found with the given ID...!!!", e.getMessage());
    }

    
    
    
    @Test
    public void testGetLoansByAccountId() throws Exception {
        // Common mock – authenticated, active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /*Case 1 : loans found for account */
        Loan loan1 = new Loan();
        loan1.setId(1);
        loan1.setAccount(sampleAccount1);
        loan1.setLoanPlan(sampleLoanPlan1);
        loan1.setLoanStartDate(LocalDate.now());
        loan1.setDisbursementDate(LocalDate.now());
        loan1.setLoanEndDate(LocalDate.now().plusMonths(60));
        loan1.setStatus(LoanStatus.ACTIVE);
        loan1.setTotalPenalty(BigDecimal.ZERO);
        loan1.setDueDate(LocalDate.now().plusMonths(12));
        loan1.setCleared(false);

        Loan loan2 = new Loan();
        loan2.setId(2);
        loan2.setAccount(sampleAccount1);
        loan2.setLoanPlan(sampleLoanPlan1);
        loan2.setLoanStartDate(LocalDate.now().minusMonths(1));
        loan2.setDisbursementDate(LocalDate.now().minusMonths(1));
        loan2.setLoanEndDate(LocalDate.now().plusMonths(59));
        loan2.setStatus(LoanStatus.ACTIVE);
        loan2.setTotalPenalty(BigDecimal.ZERO);
        loan2.setDueDate(LocalDate.now().plusMonths(11));
        loan2.setCleared(false);

        List<Loan> loanList = Arrays.asList(loan1, loan2);
        when(loanRepository.getByAccountId(sampleAccount1.getId())).thenReturn(loanList);

        List<Loan> result =
                loanService.getLoansByAccountId(sampleAccount1.getId(), samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(loan1.getId(), result.get(0).getId());
        assertEquals(loan2.getId(), result.get(1).getId());

        /* Case 2 : repository returns empty list */
        when(loanRepository.getByAccountId(sampleAccount1.getId()))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException ex1 = assertThrows(ResourceNotFoundException.class, () -> {
            loanService.getLoansByAccountId(sampleAccount1.getId(), samplePrincipal);
        });
        assertEquals("No loan found with the given account id...!!!", ex1.getMessage());

      
    }

    
    
    @Test
    public void testGetLoansByStatus() throws Exception {
        // Common mock – authenticated, active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: loans returned for ACTIVE status  */
        Loan activeLoan = new Loan();
        activeLoan.setId(101);
        activeLoan.setAccount(sampleAccount1);
        activeLoan.setLoanPlan(sampleLoanPlan1);
        activeLoan.setLoanStartDate(LocalDate.now());
        activeLoan.setDisbursementDate(LocalDate.now());
        activeLoan.setLoanEndDate(LocalDate.now().plusMonths(60));
        activeLoan.setStatus(LoanStatus.ACTIVE);
        activeLoan.setTotalPenalty(BigDecimal.ZERO);
        activeLoan.setDueDate(LocalDate.now().plusMonths(12));
        activeLoan.setCleared(false);

        when(loanRepository.getByStatus(LoanStatus.ACTIVE,PageRequest.of(0,10)))
                .thenReturn(Arrays.asList(activeLoan));

        List<Loan> activeLoans =
                loanService.getLoansByStatus(0,10,LoanStatus.ACTIVE, samplePrincipal);

        assertEquals(1, activeLoans.size());
        assertEquals(activeLoan, activeLoans.get(0));
        assertEquals(LoanStatus.ACTIVE, activeLoans.get(0).getStatus());

        /*Case 2: repository returns empty list  */
        when(loanRepository.getByStatus(LoanStatus.CLOSED,PageRequest.of(0,10)))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException ex1 = assertThrows(ResourceNotFoundException.class, () -> {
            loanService.getLoansByStatus(0,10,LoanStatus.CLOSED, samplePrincipal);
        });
        assertEquals("No loan found with the given status...!!!", ex1.getMessage());

      
    }
    
    
    
    
    @Test
    public void testGetLoansByAccountIdAndStatus() throws Exception {
        // Common mock – authenticated, active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: loans found for account + status */
        Loan activeLoan = new Loan();
        activeLoan.setId(301);
        activeLoan.setAccount(sampleAccount1);
        activeLoan.setLoanPlan(sampleLoanPlan1);
        activeLoan.setLoanStartDate(LocalDate.now());
        activeLoan.setDisbursementDate(LocalDate.now());
        activeLoan.setLoanEndDate(LocalDate.now().plusMonths(sampleLoanPlan1.getLoanTerm()));
        activeLoan.setStatus(LoanStatus.ACTIVE);
        activeLoan.setTotalPenalty(BigDecimal.ZERO);
        activeLoan.setDueDate(LocalDate.now().plusMonths(sampleLoanPlan1.getRepaymentFrequency()));
        activeLoan.setCleared(false);

        when(loanRepository.getByAccountIdAndStatus(sampleAccount1.getId(), LoanStatus.ACTIVE))
                .thenReturn(Arrays.asList(activeLoan));

        List<Loan> result = loanService.getLoansByAccountIdAndStatus(
                sampleAccount1.getId(), LoanStatus.ACTIVE, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(activeLoan, result.get(0));
        assertEquals(LoanStatus.ACTIVE, result.get(0).getStatus());

        /*  Case 2: repository returns empty list*/
        when(loanRepository.getByAccountIdAndStatus(sampleAccount1.getId(), LoanStatus.CLOSED))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException ex1 = assertThrows(ResourceNotFoundException.class, () -> {
            loanService.getLoansByAccountIdAndStatus(
                    sampleAccount1.getId(), LoanStatus.CLOSED, samplePrincipal);
        });
        assertEquals("No loan found with the given account number and status...!!!", ex1.getMessage());

       
    }

    
    
    
 //========================================== UPDATE ==========================================
    @Test
    public void testCloseLoan() throws Exception {
        // Common mock – authenticated, active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: success – loan status changes to CLOSED*/
        Loan openLoan = new Loan();
        openLoan.setId(601);
        openLoan.setAccount(sampleAccount1);
        openLoan.setLoanPlan(sampleLoanPlan1);
        openLoan.setLoanStartDate(LocalDate.now().minusMonths(3));
        openLoan.setDisbursementDate(LocalDate.now().minusMonths(3));
        openLoan.setLoanEndDate(LocalDate.now().plusMonths(57));
        openLoan.setStatus(LoanStatus.ACTIVE);
        openLoan.setTotalPenalty(BigDecimal.ZERO);
        openLoan.setDueDate(LocalDate.now().plusMonths(9));
        openLoan.setCleared(false);

        when(loanRepository.findById(601)).thenReturn(Optional.of(openLoan));
        when(loanRepository.save(openLoan)).thenReturn(openLoan);  

        Loan closedResult = loanService.closeLoan(601, samplePrincipal);

        assertEquals(LoanStatus.CLOSED, closedResult.getStatus());
        assertEquals(openLoan, closedResult);

        /*  Case 2: loan already CLOSED */
        openLoan.setStatus(LoanStatus.CLOSED);
        when(loanRepository.findById(602)).thenReturn(Optional.of(openLoan));

        InvalidActionException ex1 = assertThrows(InvalidActionException.class, () -> {
            loanService.closeLoan(602, samplePrincipal);
        });
        assertEquals("This loan is already closed...!!!", ex1.getMessage());

        /* Case 3: loan ID not found */
        when(loanRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex2 = assertThrows(ResourceNotFoundException.class, () -> {
            loanService.closeLoan(999, samplePrincipal);
        });
        assertEquals("No loan found with the given ID...!!!", ex2.getMessage());
    }

    
    @Test
    public void testUpdateTotalPenalty() throws Exception {
        // Common mock – authenticated, active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /*Case 1: success */
        Loan loan = new Loan();
        loan.setId(701);
        loan.setAccount(sampleAccount1);
        loan.setLoanPlan(sampleLoanPlan1);
        loan.setLoanStartDate(LocalDate.now().minusMonths(6));
        loan.setDisbursementDate(LocalDate.now().minusMonths(6));
        loan.setLoanEndDate(LocalDate.now().plusMonths(54));
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setTotalPenalty(BigDecimal.ZERO);
        loan.setDueDate(LocalDate.now().plusMonths(6));
        loan.setCleared(false);

        when(loanRepository.findById(701)).thenReturn(Optional.of(loan));
        when(loanRepository.save(loan)).thenReturn(loan);
        BigDecimal newPenalty = new BigDecimal("123.456"); 
        Loan updatedLoan = loanService.updateTotalPenalty(701, newPenalty, samplePrincipal);

        assertEquals(new BigDecimal("123.46"), updatedLoan.getTotalPenalty());

        /*  Case 2: negative penalty */
        BigDecimal negativePenalty = new BigDecimal("-10.00");

        InvalidInputException ex1 = assertThrows(InvalidInputException.class, () -> {
            loanService.updateTotalPenalty(701, negativePenalty, samplePrincipal);
        });
        assertEquals("Penalty cannot be negative", ex1.getMessage());

        /* Case 3: loan not found */
        when(loanRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex2 = assertThrows(ResourceNotFoundException.class, () -> {
            loanService.updateTotalPenalty(999, new BigDecimal("50.00"), samplePrincipal);
        });
        assertEquals("No loan found with the given ID...!!!", ex2.getMessage());
    }

    
    
    
    @Test
    public void testUpdateIsCleared() throws Exception {
        // Common mock – authenticated and active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: loan becomes CLEARED  */
       
        Loan loan = new Loan();
        loan.setId(801);
        loan.setAccount(sampleAccount1);
        loan.setLoanPlan(sampleLoanPlan1);                  
        loan.setLoanStartDate(LocalDate.now().minusMonths(60));
        loan.setLoanEndDate(LocalDate.now());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setTotalPenalty(BigDecimal.ZERO);
        loan.setDueDate(LocalDate.now());                  
        loan.setCleared(false);

        
        List<LoanPayment> fivePayments = Arrays.asList(
                new LoanPayment(), new LoanPayment(), new LoanPayment(),
                new LoanPayment(), new LoanPayment());

        
        when(loanRepository.findById(801)).thenReturn(Optional.of(loan));
        when(loanPaymentRepository.getByLoan(801)).thenReturn(fivePayments);
        when(loanRepository.save(loan)).thenReturn(loan);  

        Loan clearedLoan = loanService.updateIsCleared(801, samplePrincipal);

        assertTrue(clearedLoan.isCleared());
        assertNull(clearedLoan.getDueDate());

        /*  Case 2: loan NOT yet cleared*/
        loan.setCleared(false);
        List<LoanPayment> twoPayments = Arrays.asList(new LoanPayment(), new LoanPayment());

        when(loanRepository.findById(802)).thenReturn(Optional.of(loan));
        when(loanPaymentRepository.getByLoan(802)).thenReturn(twoPayments);

        Loan notClearedLoan = loanService.updateIsCleared(802, samplePrincipal);

        assertFalse(notClearedLoan.isCleared());

        /* Case 3: loan ID not found  */
        when(loanRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            loanService.updateIsCleared(999, samplePrincipal);
        });
        assertEquals("No loan found with the given ID...!!!", ex.getMessage());
    }

    

    
    
    
    
    
    
    
    
    
    
    

    @AfterEach
    public void afterTest() {
        sampleUser = null;
        samplePrincipal = null;
        sampleAccount1 = null;
        sampleLoanPlan1 = null;
        sampleApplication1 = null;
    }
}
