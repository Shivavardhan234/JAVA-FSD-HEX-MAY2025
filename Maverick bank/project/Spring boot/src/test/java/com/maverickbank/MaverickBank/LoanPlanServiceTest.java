package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.boot.test.context.SpringBootTest;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.LoanType;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.LoanPlan;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.LoanPlanRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.LoanPlanService;

@SpringBootTest
public class LoanPlanServiceTest {

    @InjectMocks
    private LoanPlanService loanPlanService;

    @Mock
    private LoanPlanRepository loanPlanRepository;

    @Mock
    private UserRepository userRepository;

    // Sample data
    private LoanPlan sampleLoanPlan1;
    private LoanPlan sampleLoanPlan2;
    private User sampleUser;
    private Principal samplePrincipal;

    @BeforeEach
    public void init() throws InvalidInputException {
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("loan_user");
        sampleUser.setPassword("LoanPass@123");
        sampleUser.setRole(Role.REPORT_MANAGER);
        sampleUser.setStatus(ActiveStatus.ACTIVE);

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

        sampleLoanPlan2 = new LoanPlan();
        sampleLoanPlan2.setId(102);
        sampleLoanPlan2.setLoanName("Personal Loan Elite");
        sampleLoanPlan2.setLoanType(LoanType.PERSONAL_LOAN);
        sampleLoanPlan2.setPrincipalAmount(new BigDecimal("200000"));
        sampleLoanPlan2.setLoanTerm(36);
        sampleLoanPlan2.setInterestRate(new BigDecimal("10.0"));
        sampleLoanPlan2.setInstallmentAmount(new BigDecimal("6500"));
        sampleLoanPlan2.setRepaymentFrequency(12);
        sampleLoanPlan2.setPenaltyRate(new BigDecimal("2.5"));
        sampleLoanPlan2.setPrePaymentPenalty(new BigDecimal("2.0"));
        sampleLoanPlan2.setGracePeriod(2);

        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("loan_user");
    }

 //====================================== ADD LOAN PLAN ==================================  
    @Test
    public void testCreateLoanPlan() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("loan_user")).thenReturn(sampleUser);
        when(loanPlanRepository.getLoanPlanByName("Home Loan Basic")).thenReturn(null);
        when(loanPlanRepository.save(sampleLoanPlan1)).thenReturn(sampleLoanPlan1);

        LoanPlan result = loanPlanService.createLoanPlan(sampleLoanPlan1, samplePrincipal);

        assertEquals(sampleLoanPlan1, result);
        assertEquals("Home Loan Basic", result.getLoanName());
        assertEquals(LoanType.HOME_LOAN, result.getLoanType());
        assertEquals(new BigDecimal("500000"), result.getPrincipalAmount());

        // Case 2: Duplicate loan name
        when(loanPlanRepository.getLoanPlanByName("Home Loan Basic")).thenReturn(sampleLoanPlan1);

        InvalidActionException e = assertThrows(InvalidActionException.class, () -> {
            loanPlanService.createLoanPlan(sampleLoanPlan1, samplePrincipal);
        });
        assertEquals("A loan plan with this name already exists...!!!", e.getMessage());
    }

    
    //========================================= GET ======================================
    @Test
    public void testGetAllLoanPlans() throws Exception {
        when(userRepository.getByUsername("loan_user")).thenReturn(sampleUser);
        when(loanPlanRepository.findAll()).thenReturn(Arrays.asList(sampleLoanPlan1, sampleLoanPlan2));

        List<LoanPlan> result = loanPlanService.getAllLoanPlans(samplePrincipal);

        assertEquals(2, result.size());
        assertTrue(result.contains(sampleLoanPlan1));
        assertTrue(result.contains(sampleLoanPlan2));
    }
    
    
    
    @Test
    public void testGetLoanPlanById() throws Exception {
        when(userRepository.getByUsername("loan_user")).thenReturn(sampleUser);
        when(loanPlanRepository.findById(101)).thenReturn(Optional.of(sampleLoanPlan1));

        //case 1
        LoanPlan result = loanPlanService.getLoanPlanById(101, samplePrincipal);

        assertEquals(sampleLoanPlan1, result);

        // Case 2: Not found
        when(loanPlanRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            loanPlanService.getLoanPlanById(999, samplePrincipal);
        });
        assertEquals("No loan plan found with the given id...!!!", e.getMessage());
    }


    @Test
    public void testGetLoanPlansByType() throws Exception {
        when(userRepository.getByUsername("loan_user")).thenReturn(sampleUser);
        when(loanPlanRepository.getByLoanType(LoanType.HOME_LOAN)).thenReturn(Arrays.asList(sampleLoanPlan1));

        List<LoanPlan> result = loanPlanService.getLoanPlansByType(LoanType.HOME_LOAN, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(sampleLoanPlan1, result.get(0));
    }

    
 //====================================== UPDATE =========================================
    @Test
    public void testUpdateLoanPlan() throws Exception {
        // Case 1: Successful update with selected fields
        when(userRepository.getByUsername("loan_user")).thenReturn(sampleUser);
        when(loanPlanRepository.findById(sampleLoanPlan1.getId())).thenReturn(Optional.of(sampleLoanPlan1));

        LoanPlan updatedInput = new LoanPlan();
        updatedInput.setId(sampleLoanPlan1.getId());
        updatedInput.setLoanName("Updated Plan");
        updatedInput.setPrincipalAmount(new BigDecimal("800000.00"));
        updatedInput.setLoanTerm(240); 

        LoanPlan updatedPlan = new LoanPlan();
        updatedPlan.setId(sampleLoanPlan1.getId());
        updatedPlan.setLoanName("Updated Plan");
        updatedPlan.setLoanType(sampleLoanPlan1.getLoanType());
        updatedPlan.setPrincipalAmount(new BigDecimal("800000.00"));
        updatedPlan.setLoanTerm(240);
        updatedPlan.setInterestRate(sampleLoanPlan1.getInterestRate());
        updatedPlan.setInstallmentAmount(sampleLoanPlan1.getInstallmentAmount());
        updatedPlan.setRepaymentFrequency(sampleLoanPlan1.getRepaymentFrequency());
        updatedPlan.setPenaltyRate(sampleLoanPlan1.getPenaltyRate());
        updatedPlan.setPrePaymentPenalty(sampleLoanPlan1.getPrePaymentPenalty());
        updatedPlan.setGracePeriod(sampleLoanPlan1.getGracePeriod());

        when(loanPlanRepository.save(any(LoanPlan.class))).thenReturn(updatedPlan);

        LoanPlan result = loanPlanService.updateLoanPlan(updatedInput, samplePrincipal);

        assertEquals("Updated Plan", result.getLoanName());
        assertEquals(new BigDecimal("800000.00"), result.getPrincipalAmount());
        assertEquals(240, result.getLoanTerm());

        // Case 2: Invalid ID (Not found)
        LoanPlan notFoundInput = new LoanPlan();
        notFoundInput.setId(999);

        when(loanPlanRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            loanPlanService.updateLoanPlan(notFoundInput, samplePrincipal);
        });
        assertEquals("No Loan Plan record found with the given id...!!!", e.getMessage());
    }

    
    
    
    
    
    
    
    
    
    
    
    
    @AfterEach
    public void tearDown() {
        sampleLoanPlan1 = null;
        sampleLoanPlan2 = null;
        sampleUser = null;
        samplePrincipal = null;
    }
}
