package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.LoanStatus;
import com.maverickbank.MaverickBank.enums.LoanType;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.LoanPayment;
import com.maverickbank.MaverickBank.model.LoanPlan;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.LoanPaymentRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.LoanPaymentService;
import com.maverickbank.MaverickBank.service.LoanService;

@SpringBootTest
public class LoanPaymentServiceTest {

    @InjectMocks
    private LoanPaymentService loanPaymentService;

    @Mock
    private LoanPaymentRepository loanPaymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanService loanService;

    private Principal samplePrincipal;
    private User sampleUser;
    private Loan sampleLoan;
    private LoanPayment samplePayment1;
    private LoanPayment samplePayment2;

    private LoanPlan sampleLoanPlan1;

    @BeforeEach
    public void init() throws InvalidInputException {
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("test_user");
        sampleUser.setStatus(ActiveStatus.ACTIVE);

        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("test_user");

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
        
        sampleLoan = new Loan();
        sampleLoan.setId(1);
        sampleLoan.setLoanStartDate(LocalDate.now());
        sampleLoan.setDisbursementDate(LocalDate.now());
        sampleLoan.setLoanEndDate(LocalDate.now().plusMonths(60));
        sampleLoan.setStatus(LoanStatus.ACTIVE);
        sampleLoan.setTotalPenalty(BigDecimal.ZERO);
        sampleLoan.setDueDate(LocalDate.now().plusMonths(12));
        sampleLoan.setCleared(false);

        samplePayment1 = new LoanPayment();
        samplePayment1.setId(201);
        samplePayment1.setLoan(sampleLoan);
        samplePayment1.setDueDate(LocalDate.now());
        samplePayment1.setAmountToBePaid(new BigDecimal("10000.00"));
        samplePayment1.setAmountPaid(new BigDecimal("10000.00"));
        samplePayment1.setPaymentDate(LocalDate.now());
        samplePayment1.setPenalty(BigDecimal.ZERO);

        samplePayment2 = new LoanPayment();
        samplePayment2.setId(202);
        samplePayment2.setLoan(sampleLoan);
        samplePayment2.setDueDate(LocalDate.now().minusMonths(1));
        samplePayment2.setAmountToBePaid(new BigDecimal("10000.00"));
        samplePayment2.setAmountPaid(new BigDecimal("9000.00"));
        samplePayment2.setPaymentDate(LocalDate.now());
        samplePayment2.setPenalty(new BigDecimal("200.00"));
    }
    
    
   //======================================= ADD ==============================================
    @Test
    public void testAddLoanPayment() throws Exception {
        //  Common mocks 
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        sampleLoan.setLoanPlan(sampleLoanPlan1);
        sampleLoan.setDueDate(LocalDate.now());
        BigDecimal installment = new BigDecimal("10250.00");                
        sampleLoan.getLoanPlan().setInstallmentAmount(installment);

        
        when(loanService.getLoanById(sampleLoan.getId(), samplePrincipal)).thenReturn(sampleLoan);

        
        when(loanService.updateDueDate(eq(sampleLoan.getId()), any(LocalDate.class), eq(samplePrincipal)))
                .thenReturn(sampleLoan);
        when(loanService.updateTotalPenalty(eq(sampleLoan.getId()), any(BigDecimal.class), eq(samplePrincipal)))
                .thenReturn(sampleLoan);
        when(loanService.updateIsCleared(sampleLoan.getId(), samplePrincipal)).thenReturn(sampleLoan);

        /* Case 1: success  */
        BigDecimal amountPaid = installment;                               
        LoanPayment savedPayment = new LoanPayment();
        savedPayment.setId(301);
        savedPayment.setLoan(sampleLoan);
        savedPayment.setDueDate(sampleLoan.getDueDate());
        savedPayment.setAmountToBePaid(installment);
        savedPayment.setAmountPaid(amountPaid);
        savedPayment.setPaymentDate(LocalDate.now());
        savedPayment.setPenalty(BigDecimal.ZERO);

        when(loanPaymentRepository.save(any(LoanPayment.class))).thenReturn(savedPayment);

        LoanPayment result = loanPaymentService
                .addLoanPayment(sampleLoan.getId(), amountPaid, samplePrincipal);

        assertEquals(savedPayment, result);
        assertEquals(installment, result.getAmountToBePaid());
        assertEquals(amountPaid, result.getAmountPaid());
        assertEquals(BigDecimal.ZERO, result.getPenalty());

        /*Case 2: under-payment (< installment) */
        BigDecimal underPayment = installment.subtract(new BigDecimal("1.00"));

        InvalidActionException ex1 = assertThrows(InvalidActionException.class, () -> {
            loanPaymentService.addLoanPayment(sampleLoan.getId(), underPayment, samplePrincipal);
        });
        assertEquals("Amount paid should be equal or greater than installment amount...!!!",
                     ex1.getMessage());

        /* Case 3: over-payment (> amountToBePaid + 1) */
        BigDecimal overPayment = installment.add(new BigDecimal("5.00"));  

        InvalidActionException ex2 = assertThrows(InvalidActionException.class, () -> {
            loanPaymentService.addLoanPayment(sampleLoan.getId(), overPayment, samplePrincipal);
        });
        assertEquals("You're trying to pay more than required. Cannot process...!!!",
                     ex2.getMessage());

        /* Case 4: loan not found  */
        when(loanService.getLoanById(999, samplePrincipal)).thenThrow(
                new ResourceNotFoundException("No loan found with the given ID...!!!"));

        ResourceNotFoundException ex3 = assertThrows(ResourceNotFoundException.class, () -> {
            loanPaymentService.addLoanPayment(999, installment, samplePrincipal);
        });
        assertEquals("No loan found with the given ID...!!!", ex3.getMessage());
    }

    
    
//========================================= GET ===============================================
    @Test
    public void testGetAllLoanPayments() throws Exception {
        //  Arrange
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        List<LoanPayment> expectedPayments = Arrays.asList(samplePayment1, samplePayment2);

        when(loanPaymentRepository.findAll()).thenReturn(expectedPayments);

       
        List<LoanPayment> actualPayments = loanPaymentService.getAllLoanPayments(samplePrincipal);

      
        assertEquals(2, actualPayments.size());
        assertEquals(expectedPayments, actualPayments);
    }

    
    
    
    @Test
    public void testGetLoanPaymentById_Success() throws Exception {
        
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);
        when(loanPaymentRepository.findById(1)).thenReturn(Optional.of(samplePayment1));

        
        LoanPayment actualPayment = loanPaymentService.getLoanPaymentById(1, samplePrincipal);
        assertEquals(samplePayment1, actualPayment);
    }

    
    
    @Test
    public void testGetPaymentsByLoanId() throws Exception {
        // Common mock – authenticated, active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: payments exist for loan*/
        
        when(loanPaymentRepository.getByLoan(sampleLoan.getId()))
                .thenReturn(Arrays.asList(samplePayment1, samplePayment2));

        List<LoanPayment> result =
                loanPaymentService.getPaymentsByLoanId(sampleLoan.getId(), samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(samplePayment1, result.get(0));
        assertEquals(samplePayment2, result.get(1));

        /* Case 2: repository returns empty list */
        when(loanPaymentRepository.getByLoan(sampleLoan.getId()))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException ex1 = assertThrows(ResourceNotFoundException.class, () -> {
            loanPaymentService.getPaymentsByLoanId(sampleLoan.getId(), samplePrincipal);
        });
        assertEquals("No loan payments found for the fiven loan id...!!!", ex1.getMessage());

      
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    @AfterEach
    public void afterTest() {
        sampleUser = null;
        samplePrincipal = null;
        sampleLoan = null;
        samplePayment1 = null;
        samplePayment2 = null;
    }

    // We'll start implementing test methods next.
}
