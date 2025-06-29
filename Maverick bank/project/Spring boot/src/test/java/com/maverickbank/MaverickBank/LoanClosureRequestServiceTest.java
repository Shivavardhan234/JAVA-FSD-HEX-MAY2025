package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.LoanStatus;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.LoanClosureRequest;
import com.maverickbank.MaverickBank.model.LoanPlan;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.LoanClosureRequestRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.LoanClosureRequestService;
import com.maverickbank.MaverickBank.service.LoanService;


@SpringBootTest
public class LoanClosureRequestServiceTest {

    @InjectMocks
    private LoanClosureRequestService loanClosureRequestService;

    @Mock
    private LoanClosureRequestRepository loanClosureRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanService loanService;

    private Loan sampleLoan;
    private Principal samplePrincipal;
    private User sampleUser;
    private LoanClosureRequest sampleRequest1;
    private LoanClosureRequest sampleRequest2;

    @BeforeEach
    public void setUp() throws InvalidInputException {

        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("test_user");
        sampleUser.setStatus(ActiveStatus.ACTIVE);

        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("test_user");

        sampleLoan = new Loan();
        sampleLoan.setId(1);
        sampleLoan.setLoanPlan(new LoanPlan());
        sampleLoan.setAccount(new Account());
        sampleLoan.setLoanStartDate(LocalDate.now());
        sampleLoan.setDisbursementDate(LocalDate.now());
        sampleLoan.setLoanEndDate(LocalDate.now().plusMonths(60));
        sampleLoan.setStatus(LoanStatus.ACTIVE);
        sampleLoan.setTotalPenalty(BigDecimal.ZERO);
        sampleLoan.setDueDate(LocalDate.now().plusMonths(12));
        sampleLoan.setCleared(false);

        sampleRequest1 = new LoanClosureRequest();
        sampleRequest1.setId(101);
        sampleRequest1.setLoan(sampleLoan);
        sampleRequest1.setRequestStatus(ApplicationStatus.PENDING);
        sampleRequest1.setPurpose("Early closure request");

        sampleRequest2 = new LoanClosureRequest();
        sampleRequest2.setId(102);
        sampleRequest2.setLoan(sampleLoan);
        sampleRequest2.setRequestStatus(ApplicationStatus.ACCEPTED);
        sampleRequest2.setPurpose("Maturity closure request");

        when(userRepository.getByUsername("test_user")).thenReturn(sampleUser);
    }
    
 //===================================== ADD =================================================
    
    
    
    @Test
    void testCreateLoanClosureRequest() throws Exception {
        // Given: active user and cleared loan
        when(samplePrincipal.getName()).thenReturn("test_user");
        when(userRepository.getByUsername("test_user")).thenReturn(sampleUser);

        sampleLoan.setCleared(true); // Must be cleared to request closure
        when(loanService.getLoanById(sampleLoan.getId(), samplePrincipal)).thenReturn(sampleLoan);

        // No previous pending requests
        when(loanClosureRequestRepository.getByLoanIdAndRequestStatus(sampleLoan.getId(), ApplicationStatus.PENDING))
            .thenReturn(Arrays.asList());

        LoanClosureRequest newRequest = new LoanClosureRequest();
        newRequest.setId(1);
        newRequest.setLoan(sampleLoan);
        newRequest.setPurpose("Closing paid loan");
        newRequest.setRequestStatus(ApplicationStatus.PENDING);

        when(loanClosureRequestRepository.save(any(LoanClosureRequest.class))).thenReturn(newRequest);

        
        LoanClosureRequest result = loanClosureRequestService.createLoanClosureRequest(
            sampleLoan.getId(), "Closing paid loan", samplePrincipal
        );

        
        assertNotNull(result);
        assertEquals(sampleLoan, result.getLoan());
        assertEquals("Closing paid loan", result.getPurpose());
        assertEquals(ApplicationStatus.PENDING, result.getRequestStatus());

        // --- Invalid Cases ---

        // Case: loan not cleared
        sampleLoan.setCleared(false);
        when(loanService.getLoanById(sampleLoan.getId(), samplePrincipal)).thenReturn(sampleLoan);

        assertThrows(InvalidActionException.class, () ->
            loanClosureRequestService.createLoanClosureRequest(sampleLoan.getId(), "try", samplePrincipal)
        );

        // Case: already has pending request
        sampleLoan.setCleared(true);
        when(loanClosureRequestRepository.getByLoanIdAndRequestStatus(sampleLoan.getId(), ApplicationStatus.PENDING))
            .thenReturn(List.of(new LoanClosureRequest()));

        assertThrows(InvalidActionException.class, () ->
            loanClosureRequestService.createLoanClosureRequest(sampleLoan.getId(), "try again", samplePrincipal)
        );
    }

    
    
//============================================ GET ==============================================
    @Test
    public void testGetAllLoanClosureRequests() throws Exception {
        // Common mock – authenticated, active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1: requests exist  */
      
        Page<LoanClosureRequest> mockPage= new PageImpl<>(Arrays.asList(sampleRequest1, sampleRequest2));
        when(loanClosureRequestRepository.findAll(PageRequest.of(0, 10))).thenReturn(mockPage);

        List<LoanClosureRequest> result = loanClosureRequestService.getAllRequests(0,10,samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleRequest1, result.get(0));
        assertEquals(sampleRequest2, result.get(1));

        /*  Case 2: no requests  */
        Page<LoanClosureRequest> emptyMockPage= new PageImpl<>(Arrays.asList());
        when(loanClosureRequestRepository.findAll(PageRequest.of(0, 10))).thenReturn(emptyMockPage);
        
        List<LoanClosureRequest> emptyResult = loanClosureRequestService.getAllRequests(0,10,samplePrincipal);

        assertTrue(emptyResult.isEmpty());
    }

    
    
    @Test
    public void testGetLoanClosureRequestById() throws Exception {
        // Common mock – authenticated, active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /*  Case 1   request exists */
        

        when(loanClosureRequestRepository.findById(1)).thenReturn(Optional.of(sampleRequest1));

        LoanClosureRequest result =
                loanClosureRequestService.getRequestById(1, samplePrincipal);

        assertEquals(sampleRequest1, result);
        assertEquals(101, result.getId());
        assertEquals(ApplicationStatus.PENDING, result.getRequestStatus());

        /*  Case 2    request not found */
        when(loanClosureRequestRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            loanClosureRequestService.getRequestById(99, samplePrincipal);
        });
        assertEquals("No closure request found with the provided ID...!!!", ex.getMessage());
    }

    
    
    
    
    @Test
    public void testGetLoanClosureRequestsByLoanId() throws Exception {
        // Active user mock
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /*  Case 1   closure requests exist */
        

        List<LoanClosureRequest> list = List.of(sampleRequest1,sampleRequest2);

        when(loanClosureRequestRepository.findByLoanId(1)).thenReturn(list);

        List<LoanClosureRequest> result =
                loanClosureRequestService.getByLoanId(1, samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleRequest1, result.get(0));

        /*  Case 2    no closure requests */
        when(loanClosureRequestRepository.findByLoanId(2)).thenReturn(Arrays.asList());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            loanClosureRequestService.getByLoanId(2, samplePrincipal);
        });
        assertEquals("No closure requests found for the given loan ID...!!!", ex.getMessage());
    }

    
    
    
    
    
    @Test
    public void testGetLoanClosureRequestsByStatus() throws Exception {
        //  Arrange: authenticated active user 
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /*  Case 1 - requests found for PENDING status*/
        sampleRequest1.setRequestStatus(ApplicationStatus.PENDING);
        when(loanClosureRequestRepository.getByRequestStatus(ApplicationStatus.PENDING,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList(sampleRequest1));

        List<LoanClosureRequest> pendingResult =
                loanClosureRequestService.getByStatus(0,10,ApplicationStatus.PENDING, samplePrincipal);

        assertEquals(1, pendingResult.size());
        assertEquals(sampleRequest1, pendingResult.get(0));
        assertEquals(ApplicationStatus.PENDING, pendingResult.get(0).getRequestStatus());

        /* Case 2 - no requests for ACCEPTED status */
        when(loanClosureRequestRepository.getByRequestStatus(ApplicationStatus.ACCEPTED,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            loanClosureRequestService.getByStatus(0,10,ApplicationStatus.ACCEPTED, samplePrincipal);
        });
        assertEquals("No loan closure requests found for the given status...!!!", ex.getMessage());
    }
    
    
    @Test
    public void testGetLoanClosureRequestsByLoanIdAndStatus() throws Exception {
        
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        sampleRequest1.setRequestStatus(ApplicationStatus.PENDING);

        List<LoanClosureRequest> actualList = Arrays.asList(sampleRequest1);

        // Case 1: Valid loan ID and status returns list
        when(loanClosureRequestRepository.getByLoanIdAndRequestStatus(1, ApplicationStatus.PENDING))
                .thenReturn(actualList);

        List<LoanClosureRequest> result = loanClosureRequestService
                .getByLoanIdAndStatus(1, ApplicationStatus.PENDING, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(sampleRequest1, result.get(0));
        assertEquals(ApplicationStatus.PENDING, result.get(0).getRequestStatus());

        //  Case 2: No matching requests throws exception
        when(loanClosureRequestRepository.getByLoanIdAndRequestStatus(2, ApplicationStatus.ACCEPTED))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            loanClosureRequestService.getByLoanIdAndStatus(2, ApplicationStatus.ACCEPTED, samplePrincipal);
        });

        assertEquals("No loan closure requests found for the given loan ID and status...!!", ex.getMessage());
    }
    
    
    
    
 //======================================= UPDATE =============================================
    @Test
    public void testRejectLoanClosureRequest() throws Exception {
        // Common mock - active user
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /*  Case 1: PENDING  REJECTED (success) */
        
        sampleRequest1.setRequestStatus(ApplicationStatus.PENDING);
        when(loanClosureRequestRepository.findById(1)).thenReturn(Optional.of(sampleRequest1));
        when(loanClosureRequestRepository.save(sampleRequest1)).thenReturn(sampleRequest1);

        LoanClosureRequest rejected =
                loanClosureRequestService.rejectLoanClosureRequest(1, samplePrincipal);

        assertEquals(ApplicationStatus.REJECTED, rejected.getRequestStatus());
        assertEquals(sampleRequest1, rejected);

        /*Case 2: already REJECTED*/
        sampleRequest1.setRequestStatus(ApplicationStatus.REJECTED);
        when(loanClosureRequestRepository.findById(2)).thenReturn(Optional.of(sampleRequest1));

        InvalidActionException ex1 = assertThrows(InvalidActionException.class, () ->
                loanClosureRequestService.rejectLoanClosureRequest(2, samplePrincipal));
        assertEquals("This closure request is already REJECTED...!!!", ex1.getMessage());

        /*  Case 3: already ACCEPTED  */
        sampleRequest1.setRequestStatus(ApplicationStatus.ACCEPTED);
        when(loanClosureRequestRepository.findById(3)).thenReturn(Optional.of(sampleRequest1));

        InvalidActionException ex2 = assertThrows(InvalidActionException.class, () ->
                loanClosureRequestService.rejectLoanClosureRequest(3, samplePrincipal));
        assertEquals("This closure request is already ACCEPTED. Cannot reject...!!!", ex2.getMessage());

        /*  Case 4: request not found */
        when(loanClosureRequestRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex3 = assertThrows(ResourceNotFoundException.class, () ->
                loanClosureRequestService.rejectLoanClosureRequest(99, samplePrincipal));
        assertEquals("No loan closure request found with id...!!!", ex3.getMessage());
    }

    
    
    
    
    
    
    @Test
    public void testAcceptLoanClosureRequest() throws Exception {
        // Active user mock
        when(userRepository.getByUsername(samplePrincipal.getName())).thenReturn(sampleUser);

        /* Case 1    PENDING  ACCEPTED (success)  */
        sampleRequest1.setRequestStatus(ApplicationStatus.PENDING);
        when(loanClosureRequestRepository.findById(1)).thenReturn(Optional.of(sampleRequest1));
        when(loanClosureRequestRepository.save(sampleRequest1)).thenReturn(sampleRequest1);
        when(loanService.closeLoan(sampleLoan.getId(), samplePrincipal)).thenReturn(sampleLoan);

        LoanClosureRequest accepted =
                loanClosureRequestService.acceptLoanClosureRequest(1, samplePrincipal);

        assertEquals(ApplicationStatus.ACCEPTED, accepted.getRequestStatus());
        assertEquals(sampleRequest1, accepted);

        /* Case 2    already ACCEPTED */
        sampleRequest1.setRequestStatus(ApplicationStatus.ACCEPTED);
        when(loanClosureRequestRepository.findById(2)).thenReturn(Optional.of(sampleRequest1));

        InvalidActionException ex1 = assertThrows(InvalidActionException.class, () ->
                loanClosureRequestService.acceptLoanClosureRequest(2, samplePrincipal));
        assertEquals("This closure request is already ACCEPTED...!!!", ex1.getMessage());

        /*  Case 3    already REJECTED  */
        sampleRequest1.setRequestStatus(ApplicationStatus.REJECTED);
        when(loanClosureRequestRepository.findById(3)).thenReturn(Optional.of(sampleRequest1));

        InvalidActionException ex2 = assertThrows(InvalidActionException.class, () ->
                loanClosureRequestService.acceptLoanClosureRequest(3, samplePrincipal));
        assertEquals("This closure request is already REJECTED. Cannot accept...!!", ex2.getMessage());

        /*  Case 4    request ID not found  */
        when(loanClosureRequestRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex3 = assertThrows(ResourceNotFoundException.class, () ->
                loanClosureRequestService.acceptLoanClosureRequest(99, samplePrincipal));
        assertEquals("No loan closure request found with id...!!!", ex3.getMessage());
    }



    
    
    
    
    

    @AfterEach
    public void tearDown() {
        sampleLoan = null;
        sampleRequest1 = null;
        sampleRequest2 = null;
    }

   
}

