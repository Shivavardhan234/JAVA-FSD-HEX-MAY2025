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
import com.maverickbank.MaverickBank.enums.RequestType;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.AccountRequest;
import com.maverickbank.MaverickBank.model.Loan;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.AccountRequestRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.AccountRequestService;
import com.maverickbank.MaverickBank.service.LoanService;

@SpringBootTest
class AccountRequestServiceTest {

    @InjectMocks
    private AccountRequestService accountRequestService;

    @Mock
    private AccountRequestRepository accountRequestRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanService loanService;

    private User sampleUser;
    private Principal samplePrincipal;
    private Account sampleAccount1;
    private AccountRequest sampleRequest1;
    private AccountRequest sampleRequest2;

    @BeforeEach
    public void init() throws InvalidInputException {
        sampleUser = new User();
        sampleUser.setId(1010);
        sampleUser.setUsername("user.request");
        sampleUser.setStatus(ActiveStatus.ACTIVE);
        sampleUser.setRole(Role.CUSTOMER);

        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("user.request");

        sampleAccount1 = new Account();
        sampleAccount1.setId(301);
        sampleAccount1.setAccountNumber("11112222333");
        sampleAccount1.setAccountName("Primary Savings");
        sampleAccount1.setBalance(new BigDecimal("25000.00"));
        sampleAccount1.setAccountStatus(AccountStatus.OPEN);
        sampleAccount1.setKycCompliant(true);

        sampleRequest1 = new AccountRequest();
        sampleRequest1.setId(1);
        sampleRequest1.setAccount(sampleAccount1);
        sampleRequest1.setPurpose("Loan Inquiry");
        sampleRequest1.setRequestStatus(ApplicationStatus.PENDING);
        sampleRequest1.setRequestType(RequestType.OPEN);

        sampleRequest2 = new AccountRequest();
        sampleRequest2.setId(2);
        sampleRequest2.setAccount(sampleAccount1);
        sampleRequest2.setPurpose("Statement Copy");
        sampleRequest2.setRequestStatus(ApplicationStatus.PENDING);
        sampleRequest2.setRequestType(RequestType.CLOSE);
    }
    
 //============================================= ADD ===========================================
    @Test
    public void testCreateRequest() throws Exception {
        // Common mock – authenticated active user
        when(userRepository.getByUsername("user.request")).thenReturn(sampleUser);

        // Case 1: Successful request creation (type = OPEN)
        when(accountRepository.findById(sampleAccount1.getId())).thenReturn(Optional.of(sampleAccount1));
        when(accountRequestRepository.getPreviousRequest(sampleAccount1.getId(), RequestType.OPEN, ApplicationStatus.PENDING)).thenReturn(null);
        when(accountRequestRepository.save(Mockito.any(AccountRequest.class))).thenReturn(sampleRequest1);

        AccountRequest created = accountRequestService.createRequest(
                sampleAccount1.getId(), RequestType.OPEN, "Reopen", samplePrincipal);

        assertEquals(sampleRequest1, created);
        assertEquals(sampleAccount1, created.getAccount());
        assertEquals(ApplicationStatus.PENDING, created.getRequestStatus());
        assertEquals(RequestType.OPEN, created.getRequestType());

        // Case 2: Request type is null
        InvalidInputException e1 = assertThrows(InvalidInputException.class, () -> {
            accountRequestService.createRequest(sampleAccount1.getId(), null, "Purpose", samplePrincipal);
        });
        assertEquals("Null request type provided...!!!", e1.getMessage());

        // Case 3: Account not found
        when(accountRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException e2 = assertThrows(ResourceNotFoundException.class, () -> {
            accountRequestService.createRequest(999, RequestType.CLOSE, "Close", samplePrincipal);
        });
        assertEquals("No account record found with the given id...!!!", e2.getMessage());

        // Case 4: Account is CLOSED
        Account closedAccount = new Account();
        closedAccount.setId(302);
        closedAccount.setAccountStatus(AccountStatus.CLOSED);
        closedAccount.setBalance(BigDecimal.ZERO);

        when(accountRepository.findById(302)).thenReturn(Optional.of(closedAccount));

        InvalidActionException e3 = assertThrows(InvalidActionException.class, () -> {
            accountRequestService.createRequest(302, RequestType.CLOSE, "Close", samplePrincipal);
        });
        assertEquals("This account is permanently closed,Cannot perform any action...!!!", e3.getMessage());

        // Case 5: RequestType = CLOSE with non-zero balance
        sampleAccount1.setBalance(new BigDecimal("500.00")); // change temporarily

        when(accountRepository.findById(sampleAccount1.getId())).thenReturn(Optional.of(sampleAccount1));

        InvalidActionException e4 = assertThrows(InvalidActionException.class, () -> {
            accountRequestService.createRequest(sampleAccount1.getId(), RequestType.CLOSE, "Close Account", samplePrincipal);
        });
        assertEquals("This account has some amount in it. You cannot close the account...!!!", e4.getMessage());

        // Case 6: RequestType = CLOSE with pending loans
        sampleAccount1.setBalance(BigDecimal.ZERO); // set to 0 now
        Loan loan1 = new Loan(); loan1.setCleared(false);
        List<Loan> loans = Arrays.asList(loan1);

        when(loanService.getLoansByAccountId(sampleAccount1.getId(), samplePrincipal)).thenReturn(loans);

        InvalidActionException e5 = assertThrows(InvalidActionException.class, () -> {
            accountRequestService.createRequest(sampleAccount1.getId(), RequestType.CLOSE, "Close Account", samplePrincipal);
        });
        assertEquals("You have pending loans, you cannot close account...!!!", e5.getMessage());

        // Case 7: Duplicate request already exists
        sampleAccount1.setBalance(BigDecimal.ZERO);
        Loan clearedLoan = new Loan(); clearedLoan.setCleared(true);
        when(loanService.getLoansByAccountId(sampleAccount1.getId(), samplePrincipal)).thenReturn(Arrays.asList(clearedLoan));
        when(accountRequestRepository.getPreviousRequest(sampleAccount1.getId(), RequestType.CLOSE, ApplicationStatus.PENDING)).thenReturn(sampleRequest2);

        InvalidActionException e6 = assertThrows(InvalidActionException.class, () -> {
            accountRequestService.createRequest(sampleAccount1.getId(), RequestType.CLOSE, "Close Account", samplePrincipal);
        });
        assertEquals("You have already filed a request...!!!", e6.getMessage());
    }

    
    
    
//============================================= GET ============================================
    @Test
    public void testGetAllAccountRequests() throws Exception {
        // Common mock – active user
        when(userRepository.getByUsername("user.request")).thenReturn(sampleUser);

       
        List<AccountRequest> requestList = Arrays.asList(sampleRequest1, sampleRequest2);
        when(accountRequestRepository.findAll()).thenReturn(requestList);

        List<AccountRequest> result = accountRequestService.getAllRequests(samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleRequest1, result.get(0));
        assertEquals(sampleRequest2, result.get(1));

       
    }

    
    @Test
    public void testGetAccountRequestById() throws Exception {
        // Common mock – authenticated active user
        when(userRepository.getByUsername("user.request")).thenReturn(sampleUser);

        /* ---------- Case 1 : request exists ---------- */
        when(accountRequestRepository.findById(1)).thenReturn(Optional.of(sampleRequest1));

        AccountRequest result = accountRequestService.getRequestById(1, samplePrincipal);

        assertEquals(sampleRequest1, result);
        assertEquals(1, result.getId());
        assertEquals(sampleAccount1, result.getAccount());

        /* ---------- Case 2 : request not found ---------- */
        when(accountRequestRepository.findById(2)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            accountRequestService.getRequestById(2, samplePrincipal);
        });
        assertEquals("No account request found with given id...!!!", ex.getMessage());
    }
    
    
    @Test
    public void testGetRequestsByAccountId() throws Exception {
        // Common mock – authenticated active user
        when(userRepository.getByUsername("user.request")).thenReturn(sampleUser);

        /* ---------- Case 1 : requests exist for account ---------- */
        List<AccountRequest> requestList = Arrays.asList(sampleRequest1, sampleRequest2);
        when(accountRequestRepository.findByAccountId(sampleAccount1.getId()))
                .thenReturn(requestList);

        List<AccountRequest> result =
                accountRequestService.getRequestsByAccountId(sampleAccount1.getId(), samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(sampleRequest1, result.get(0));
        assertEquals(sampleRequest2, result.get(1));

        /* ---------- Case 2 : no requests found ---------- */
        when(accountRequestRepository.findByAccountId(sampleAccount1.getId()))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            accountRequestService.getRequestsByAccountId(sampleAccount1.getId(), samplePrincipal);
        });
        assertEquals("No account requests found for the given account id...!!! ", ex.getMessage());
    }


    
    @Test
    public void testGetAccountRequestsByStatus() throws Exception {
        // Common mock – authenticated active user
        when(userRepository.getByUsername("user.request")).thenReturn(sampleUser);

        /* ---------- Case 1 : requests found for given status ---------- */
        // sampleRequest1 is PENDING, sampleRequest2 is APPROVED
        when(accountRequestRepository.findByRequestStatus(ApplicationStatus.PENDING))
                .thenReturn(Arrays.asList(sampleRequest1));

        List<AccountRequest> pendingRequests =
                accountRequestService.getRequestsByStatus(ApplicationStatus.PENDING, samplePrincipal);

        assertEquals(1, pendingRequests.size());
        assertEquals(sampleRequest1, pendingRequests.get(0));
        assertEquals(ApplicationStatus.PENDING, pendingRequests.get(0).getRequestStatus());

        /* ---------- Case 2 : no requests for given status ---------- */
        when(accountRequestRepository.findByRequestStatus(ApplicationStatus.REJECTED))
                .thenReturn(Arrays.asList());

        List<AccountRequest> rejectedRequests =
                accountRequestService.getRequestsByStatus(ApplicationStatus.REJECTED, samplePrincipal);

        assertTrue(rejectedRequests.isEmpty());
    }
    
    
    
    
    
//========================================== UPDATE ===========================================
    @Test
    public void testAcceptAccountRequest() throws Exception {
        // Common mock – active user
        when(userRepository.getByUsername("user.request")).thenReturn(sampleUser);

        /* Case 1: success   */
        sampleRequest1.setRequestStatus(ApplicationStatus.PENDING);
        sampleRequest1.setRequestType(RequestType.CLOSE);            
        sampleAccount1.setAccountStatus(AccountStatus.OPEN);          
        when(accountRequestRepository.findById(1)).thenReturn(Optional.of(sampleRequest1));
        when(accountRepository.save(sampleAccount1)).thenReturn(sampleAccount1);                           

        AccountRequest accepted = accountRequestService.acceptRequest(1, samplePrincipal);

        assertEquals(ApplicationStatus.ACCEPTED, accepted.getRequestStatus());
        assertEquals(AccountStatus.CLOSED, sampleAccount1.getAccountStatus());
        assertEquals(sampleRequest1, accepted);

        /* Case 2: request already ACCEPTED  */
        AccountRequest alreadyAccepted = new AccountRequest();
        alreadyAccepted.setId(2);
        alreadyAccepted.setAccount(sampleAccount1);
        alreadyAccepted.setRequestStatus(ApplicationStatus.ACCEPTED);
        alreadyAccepted.setRequestType(RequestType.CLOSE);

        when(accountRequestRepository.findById(2))
                .thenReturn(Optional.of(alreadyAccepted));

        InvalidActionException ex1 = assertThrows(InvalidActionException.class, () -> {
            accountRequestService.acceptRequest(2, samplePrincipal);
        });
        assertEquals("This request is already Accepted...!!!", ex1.getMessage());

        /*  Case 3: request already REJECTED */
        AccountRequest alreadyRejected = new AccountRequest();
        alreadyRejected.setId(3);
        alreadyRejected.setAccount(sampleAccount1);
        alreadyRejected.setRequestStatus(ApplicationStatus.REJECTED);
        alreadyRejected.setRequestType(RequestType.CLOSE);

        when(accountRequestRepository.findById(3))
                .thenReturn(Optional.of(alreadyRejected));

        InvalidActionException ex2 = assertThrows(InvalidActionException.class, () -> {
            accountRequestService.acceptRequest(3, samplePrincipal);
        });
        assertEquals("This request is already REJECTED...!!!", ex2.getMessage());

      
        /*  Case 4: request ID not found */
        when(accountRequestRepository.findById(5))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex4 = assertThrows(ResourceNotFoundException.class, () -> {
            accountRequestService.acceptRequest(5, samplePrincipal);
        });
        assertEquals("No account request found with the given id...!!!", ex4.getMessage());
    }

    
    
    
    
    
    @Test
    public void testRejectAccountRequest() throws Exception {
        // Common mock – active user
        when(userRepository.getByUsername("user.request")).thenReturn(sampleUser);

        /* Case 1: success */
        sampleRequest1.setRequestStatus(ApplicationStatus.PENDING);    
        when(accountRequestRepository.findById(1))
                .thenReturn(Optional.of(sampleRequest1));
        when(accountRequestRepository.save(sampleRequest1))
                .thenReturn(sampleRequest1);

        AccountRequest rejected = accountRequestService.rejectRequest(1, samplePrincipal);

        assertEquals(ApplicationStatus.REJECTED, rejected.getRequestStatus());
        assertEquals(sampleRequest1, rejected);

        /* Case 2: already REJECTED*/
        AccountRequest alreadyRejected = new AccountRequest();
        alreadyRejected.setId(2);
        alreadyRejected.setAccount(sampleAccount1);
        alreadyRejected.setRequestStatus(ApplicationStatus.REJECTED);

        when(accountRequestRepository.findById(2))
                .thenReturn(Optional.of(alreadyRejected));

        InvalidActionException ex1 = assertThrows(InvalidActionException.class, () -> {
            accountRequestService.rejectRequest(2, samplePrincipal);
        });
        assertEquals("This request is already REJECTED...!!!", ex1.getMessage());

        /* Case 3: already ACCEPTED */
        AccountRequest alreadyAccepted = new AccountRequest();
        alreadyAccepted.setId(3);
        alreadyAccepted.setAccount(sampleAccount1);
        alreadyAccepted.setRequestStatus(ApplicationStatus.ACCEPTED);

        when(accountRequestRepository.findById(3))
                .thenReturn(Optional.of(alreadyAccepted));

        InvalidActionException ex2 = assertThrows(InvalidActionException.class, () -> {
            accountRequestService.rejectRequest(3, samplePrincipal);
        });
        assertEquals("This request is already ACCEPTED...!!!", ex2.getMessage());

        /* Case 4: request ID not found */
        when(accountRequestRepository.findById(4))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex3 = assertThrows(ResourceNotFoundException.class, () -> {
            accountRequestService.rejectRequest(4, samplePrincipal);
        });
        assertEquals("No account request found with the given id...!!!", ex3.getMessage());
    }


    
    
    
    

    @AfterEach
    public void afterTest() {
        sampleUser = null;
        samplePrincipal = null;
        sampleAccount1 = null;
        sampleRequest1 = null;
        sampleRequest2 = null;
    }
}
