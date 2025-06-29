package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.CustomerAccount;
import com.maverickbank.MaverickBank.model.CustomerAccountOpeningApplication;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.BranchRepository;
import com.maverickbank.MaverickBank.repository.CustomerAccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.AccountOpeningApplicationService;
import com.maverickbank.MaverickBank.service.AccountService;
import com.maverickbank.MaverickBank.service.AccountTypeService;
import com.maverickbank.MaverickBank.service.BranchService;
import com.maverickbank.MaverickBank.service.CustomerAccountService;

@SpringBootTest
class AccountOpeningApplicationServiceTest {

    @InjectMocks
    private AccountOpeningApplicationService accountOpeningApplicationService;

    @Mock
    private AccountOpeningApplicationRepository accountOpeningApplicationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BranchService branchService;
    @Mock
    private AccountTypeService accountTypeService;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private CustomerAccountService customerAccountService;
    @Mock
    private AccountService accountService;
    @Mock
    private CustomerAccountOpeningApplicationRepository customerAccountOpeningApplicationRepository;

    private User sampleUser;
    private Principal samplePrincipal;

    private Branch sampleBranch1;
    private AccountType sampleAccountType1;
    private AccountOpeningApplication sampleApplication1;
    private AccountOpeningApplication sampleApplication2;

    @BeforeEach
    public void init() throws Exception {
        // Sample Principal User
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("new_user");
        sampleUser.setPassword("userPassword@123");
        sampleUser.setRole(Role.CUSTOMER);
        sampleUser.setStatus(ActiveStatus.ACTIVE);

        // Principal Mocking
        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("new_user");

        // Sample Branch
        sampleBranch1 = new Branch();
        sampleBranch1.setId(101);
        sampleBranch1.setIfsc("MVRK0000001");
        sampleBranch1.setBranchName("Hyderabad Central");
        sampleBranch1.setAddress("123 Central Ave, Hyderabad");
        sampleBranch1.setContactNumber("9999999999");
        sampleBranch1.setEmail("hydcentral@maverickbank.com");
        sampleBranch1.setStatus(ActiveStatus.ACTIVE);

        // Sample Account Type
        sampleAccountType1 = new AccountType();
        sampleAccountType1.setAccountTypeId(101);
        sampleAccountType1.setAccountType(BankAccountType.SAVINGS);
        sampleAccountType1.setInterestRate(new BigDecimal("3.5"));
        sampleAccountType1.setMinimumBalance(new BigDecimal("1000.00"));
        sampleAccountType1.setTransactionLimit(10);
        sampleAccountType1.setTransactionAmountLimit(new BigDecimal("50000.00"));
        sampleAccountType1.setWithdrawLimit(5);

        // Sample Application 1
        sampleApplication1 = new AccountOpeningApplication();
        sampleApplication1.setId(201);
        sampleApplication1.setBranch(sampleBranch1);
        sampleApplication1.setAccountType(sampleAccountType1);
        sampleApplication1.setAccountName("Ravi Kumar");
        sampleApplication1.setPurpose("Salary Account");
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.PENDING);
        sampleApplication1.setEmployeeApprovalStatus(ApplicationStatus.PENDING);
        sampleApplication1.setApplicationDateTime(LocalDateTime.now());

        // Sample Application 2
        sampleApplication2 = new AccountOpeningApplication();
        sampleApplication2.setId(202);
        sampleApplication2.setBranch(sampleBranch1);
        sampleApplication2.setAccountType(sampleAccountType1);
        sampleApplication2.setAccountName("Anjali Mehta");
        sampleApplication2.setPurpose("Savings");
        sampleApplication2.setCustomerApprovalStatus(ApplicationStatus.ACCEPTED);
        sampleApplication2.setEmployeeApprovalStatus(ApplicationStatus.ACCEPTED);
        sampleApplication2.setApplicationDateTime(LocalDateTime.now());
    }

 //======================================= ADD ===========================================
    
    
    @Test
    public void testAddAccountOpeningApplication() throws Exception {
        // ---------- Case 1: Success – application saved ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchService.getByName(sampleBranch1.getBranchName(), samplePrincipal))
                .thenReturn(sampleBranch1);
        when(accountTypeService.getAccountTypeByName(
                sampleAccountType1.getAccountType(), samplePrincipal))
                .thenReturn(sampleAccountType1);
        when(accountOpeningApplicationRepository.save(sampleApplication1))
                .thenReturn(sampleApplication1);

        AccountOpeningApplication added =
                accountOpeningApplicationService.addAccountOpeningApplication(
                        sampleApplication1, samplePrincipal);

        assertEquals(201, added.getId());
        assertEquals(sampleBranch1, added.getBranch());
        assertEquals(sampleAccountType1, added.getAccountType());
        assertEquals(ApplicationStatus.PENDING, added.getCustomerApprovalStatus());
        assertEquals(ApplicationStatus.PENDING, added.getEmployeeApprovalStatus());
        assertNotNull(added.getApplicationDateTime());

        // ---------- Case 2: Branch not found  ResourceNotFoundException ----------
        when(branchService.getByName("Ghost Branch", samplePrincipal))
                .thenThrow(new ResourceNotFoundException("No branch record with given name...!!!"));

        AccountOpeningApplication branchFail = new AccountOpeningApplication();
        branchFail.setBranch(new Branch());                 
        branchFail.getBranch().setBranchName("Ghost Branch");
        branchFail.setAccountType(sampleAccountType1);

        ResourceNotFoundException eBranch = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.addAccountOpeningApplication(branchFail, samplePrincipal);
        });
        assertEquals("No branch record with given name...!!!", eBranch.getMessage());

        // Case 3: Account-type not found 
        when(branchService.getByName(sampleBranch1.getBranchName(), samplePrincipal))
                .thenReturn(sampleBranch1);                                 
        when(accountTypeService.getAccountTypeByName(BankAccountType.CURRENT, samplePrincipal))
                .thenReturn(null);

        AccountOpeningApplication typeFail = new AccountOpeningApplication();
        typeFail.setBranch(sampleBranch1);
        AccountType missingType = new AccountType();
        missingType.setAccountType(BankAccountType.CURRENT);             
        typeFail.setAccountType(missingType);

        InvalidInputException eType = assertThrows(InvalidInputException.class, () -> {
            accountOpeningApplicationService.addAccountOpeningApplication(typeFail, samplePrincipal);
        });
        assertEquals("Invalid account type object provided. Please provide appropriate account type object...!!!", eType.getMessage());
    }
    
    
//========================================= GET ==========================================
    @Test
    public void testGetAllAccountOpeningApplication() throws Exception {
        // ---------- Case 1: Success – repository returns two applications ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        Page<AccountOpeningApplication> mockPage=new PageImpl<>(Arrays.asList(sampleApplication1, sampleApplication2));
        when(accountOpeningApplicationRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(mockPage);

        List<AccountOpeningApplication> allApps =
                accountOpeningApplicationService.getAllAccountOpeningApplication(0,10,samplePrincipal);

        assertEquals(2, allApps.size());
        assertEquals(201, allApps.get(0).getId());
        assertEquals(202, allApps.get(1).getId());

        // ---------- Case 2: Empty list ----------
        Page<AccountOpeningApplication> emptyMockPage= new PageImpl<>(Arrays.asList());
        when(accountOpeningApplicationRepository.findAll(PageRequest.of(0, 10))).thenReturn(emptyMockPage);

        List<AccountOpeningApplication> emptyApps =
                accountOpeningApplicationService.getAllAccountOpeningApplication(0,10,samplePrincipal);

        assertTrue(emptyApps.isEmpty());

        
    }


    
    
    @Test
    public void testGetAccountOpeningApplicationById() throws Exception {
        // ---------- Case 1: Success ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountOpeningApplicationRepository.findById(201)).thenReturn(Optional.of(sampleApplication1));

        AccountOpeningApplication result =
                accountOpeningApplicationService.getAccountOpeningApplicationById(201, samplePrincipal);

        assertEquals(201, result.getId());
        assertEquals("Ravi Kumar", result.getAccountName());
        assertEquals(ApplicationStatus.PENDING, result.getCustomerApprovalStatus());

        // ---------- Case 2: Application with given ID not found ----------
        when(accountOpeningApplicationRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.getAccountOpeningApplicationById(999, samplePrincipal);
        });
        assertEquals("No Account application record with the given id...!!!", e.getMessage());
    }
    
    
    @Test
    public void testGetAccountOpeningApplicationByBranch() throws Exception {
        // ---------- Case 1: Success 
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchService.getById(101, samplePrincipal)).thenReturn(sampleBranch1);
        when(accountOpeningApplicationRepository.getAccountOpeningApplicationByBranchId(101))
                .thenReturn(Arrays.asList(sampleApplication1, sampleApplication2));

        Branch branchById = new Branch();
        branchById.setId(101);                     

        List<AccountOpeningApplication> result =
                accountOpeningApplicationService.getAccountOpeningApplicationByBranch(
                        branchById, samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(201, result.get(0).getId());
        assertEquals(202, result.get(1).getId());

        // ---------- Case 2: No applications for branch 
        when(accountOpeningApplicationRepository.getAccountOpeningApplicationByBranchId(101))
                .thenReturn(Arrays.asList());                  

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.getAccountOpeningApplicationByBranch(branchById, samplePrincipal);
        });
        assertEquals("No Account application records with the given branch...!!!", eNotFound.getMessage());

        // ---------- Case 3: Invalid branch details
        Branch invalidBranch = new Branch();        

        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            accountOpeningApplicationService.getAccountOpeningApplicationByBranch(invalidBranch, samplePrincipal);
        });
        assertEquals(
            "Invalid branch details. Provide appropriate branch details...!!!",
            eInvalid.getMessage()
        );
    }

    
    
    @Test
    public void testGetAccountOpeningApplicationByAccountType() throws Exception {
        // ---------- Case 1: Success
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountTypeService.getAccountTypeById(101, samplePrincipal))
                .thenReturn(sampleAccountType1);
        when(accountOpeningApplicationRepository.getAccountOpeningApplicationByAccountTypeId(101))
                .thenReturn(Arrays.asList(sampleApplication1, sampleApplication2));

        AccountType typeById = new AccountType();
        typeById.setAccountTypeId(101);                     

        List<AccountOpeningApplication> result =
                accountOpeningApplicationService.getAccountOpeningApplicationByAccountType(
                        typeById, samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(201, result.get(0).getId());
        assertEquals(202, result.get(1).getId());

        // ---------- Case 2: No applications for type 
        when(accountOpeningApplicationRepository.getAccountOpeningApplicationByAccountTypeId(101))
                .thenReturn(Arrays.asList());                            

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.getAccountOpeningApplicationByAccountType(typeById, samplePrincipal);
        });
        assertEquals("No Account application records with the given account type...!!!", eNotFound.getMessage());

        // ---------- Case 3: Invalid account-type details 
        AccountType invalidType = new AccountType();          

        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            accountOpeningApplicationService.getAccountOpeningApplicationByAccountType(invalidType, samplePrincipal);
        });
        assertEquals(
            "Invalid account type details. Provide appropriate account type details...!!!",
            eInvalid.getMessage()
        );
    }
    
    
    
    @Test
    public void testGetAccountOpeningApplicationByCustomerApprovalStatus() throws Exception {
        //  Case 1: Success 
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountOpeningApplicationRepository.getAccountOpeningApplicationByCustomerApprovalStatus(
                ApplicationStatus.PENDING))
                .thenReturn(Arrays.asList(sampleApplication1));

        List<AccountOpeningApplication> pendingApps =
                accountOpeningApplicationService.getAccountOpeningApplicationByCustomerApprovalStatus(
                        ApplicationStatus.PENDING, samplePrincipal);

        assertEquals(1, pendingApps.size());
        assertEquals(201, pendingApps.get(0).getId());
        assertEquals(ApplicationStatus.PENDING, pendingApps.get(0).getCustomerApprovalStatus());

        // ---------- Case 2: No records for ACCEPTED 
        when(accountOpeningApplicationRepository.getAccountOpeningApplicationByCustomerApprovalStatus(
                ApplicationStatus.ACCEPTED))
                .thenReturn(Arrays.asList());   

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.getAccountOpeningApplicationByCustomerApprovalStatus(
                    ApplicationStatus.ACCEPTED, samplePrincipal);
        });
        assertEquals(
            "No Account application records with the given customer approval status...!!!",
            eNotFound.getMessage()
        );

    }

    
    
    
    
    @Test
    public void testGetAccountOpeningApplicationByDate() throws Exception {
        // ---------- Common setup ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        
        LocalDate date = LocalDate.of(2025, 6, 26);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();

        // ---------- Case 1: Success 
        sampleApplication1.setApplicationDateTime(start.plusHours(9));  
        when(accountOpeningApplicationRepository.getAccountOpeningApplicationByDate(start, end))
                .thenReturn(Arrays.asList(sampleApplication1));

        List<AccountOpeningApplication> found =
                accountOpeningApplicationService.getAccountOpeningApplicationByDate(date, samplePrincipal);

        assertEquals(1, found.size());
        assertEquals(201, found.get(0).getId());
        assertEquals(ApplicationStatus.PENDING, found.get(0).getCustomerApprovalStatus());

        // ---------- Case 2: No records 
        when(accountOpeningApplicationRepository.getAccountOpeningApplicationByDate(start, end))
                .thenReturn(Arrays.asList());  

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.getAccountOpeningApplicationByDate(date, samplePrincipal);
        });
        assertEquals("No Account application records with the given date...!!!", eNotFound.getMessage());

        // ---------- Case 3: Null date 
        InvalidInputException eNullDate = assertThrows(InvalidInputException.class, () -> {
            accountOpeningApplicationService.getAccountOpeningApplicationByDate(null, samplePrincipal);
        });
        assertEquals("Invalid null date provided...!!!", eNullDate.getMessage());
    }

    
    
    @Test
    public void testGetAccountOpeningApplicationByEmployeeApprovalStatus() throws Exception {
        // ---------- Common setup ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        ApplicationStatus status = ApplicationStatus.PENDING;

        // ---------- Case 1: Success 
        sampleApplication1.setEmployeeApprovalStatus(ApplicationStatus.PENDING);
        when(accountOpeningApplicationRepository.getAccountOpeningApplicationByEmployeeApprovalStatus(status,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList(sampleApplication1));

        List<AccountOpeningApplication> result =
                accountOpeningApplicationService.getAccountOpeningApplicationByEmployeeApprovalStatus(0,10,status, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(201, result.get(0).getId());
        assertEquals(ApplicationStatus.PENDING, result.get(0).getEmployeeApprovalStatus());

        // ---------- Case 2: Repository returns null ----------
        when(accountOpeningApplicationRepository.getAccountOpeningApplicationByEmployeeApprovalStatus(status,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.getAccountOpeningApplicationByEmployeeApprovalStatus(0,10,status, samplePrincipal);
        });
        assertEquals("No Account application records with the employee approval status...!!!", e.getMessage());
    }

    
 //========================================== UPDATE =====================================
    @Test
    public void testUpdateCustomerApprovalStatus() throws Exception {
        // Common stub: active principal
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        // ---- Case 1 – all customers ACCEPTED → status ACCEPTED ----
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.PENDING);
        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));

        CustomerAccountOpeningApplication ca1 = new CustomerAccountOpeningApplication();
        ca1.setCustomerApproval(ApplicationStatus.ACCEPTED);
        CustomerAccountOpeningApplication ca2 = new CustomerAccountOpeningApplication();
        ca2.setCustomerApproval(ApplicationStatus.ACCEPTED);
        when(customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(201))
                .thenReturn(Arrays.asList(ca1, ca2));

        when(accountOpeningApplicationRepository.save(sampleApplication1))
                .thenReturn(sampleApplication1);

        AccountOpeningApplication accepted =
                accountOpeningApplicationService.updateCustomerApprovalStatus(201, samplePrincipal);

        assertEquals(ApplicationStatus.ACCEPTED, accepted.getCustomerApprovalStatus());

        // ---- Case 2 – one REJECTED → status REJECTED ----
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.PENDING);   // reset
        CustomerAccountOpeningApplication cr1 = new CustomerAccountOpeningApplication();
        cr1.setCustomerApproval(ApplicationStatus.REJECTED);
        CustomerAccountOpeningApplication cr2 = new CustomerAccountOpeningApplication();
        cr2.setCustomerApproval(ApplicationStatus.ACCEPTED);
        when(customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(201))
                .thenReturn(Arrays.asList(cr1, cr2));

        AccountOpeningApplication rejected =
                accountOpeningApplicationService.updateCustomerApprovalStatus(201, samplePrincipal);

        assertEquals(ApplicationStatus.REJECTED, rejected.getCustomerApprovalStatus());

        // ---- Case 3 – no REJECTED, not all ACCEPTED → status PENDING ----
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.PENDING);   // reset
        CustomerAccountOpeningApplication cp1 = new CustomerAccountOpeningApplication();
        cp1.setCustomerApproval(ApplicationStatus.PENDING);
        when(customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(201))
                .thenReturn(Arrays.asList(cp1));

        AccountOpeningApplication pending =
                accountOpeningApplicationService.updateCustomerApprovalStatus(201, samplePrincipal);

        assertEquals(ApplicationStatus.PENDING, pending.getCustomerApprovalStatus());

        // ---- Case 4 – application ID not found ----
        when(accountOpeningApplicationRepository.findById(999))
                .thenReturn(Optional.empty());

        ResourceNotFoundException eId = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.updateCustomerApprovalStatus(999, samplePrincipal);
        });
        assertEquals("No Account application record with the given id...!!!", eId.getMessage());

        // ---- Case 5 – no customer-application records ----
        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));
        when(customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(201))
                .thenReturn(Arrays.asList());                       

        ResourceNotFoundException eList = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.updateCustomerApprovalStatus(201, samplePrincipal);
        });
        assertEquals(
            "No customer account opening applications with the given account opening application id...!!!",
            eList.getMessage()
        );
    }
    
    
    
    
    
    
    
    @Test
    public void testApproveApplication() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        
        CustomerAccountOpeningApplication customerAccountOpeningApplication1 = new CustomerAccountOpeningApplication();
        customerAccountOpeningApplication1.setId(1);
        customerAccountOpeningApplication1.setCustomerApproval(ApplicationStatus.ACCEPTED);
        CustomerAccountOpeningApplication customerAccountOpeningApplication2 = new CustomerAccountOpeningApplication();
        customerAccountOpeningApplication2.setId(2);
        customerAccountOpeningApplication2.setCustomerApproval(ApplicationStatus.ACCEPTED);

        
        Account newAccount = new Account();
        newAccount.setId(301);
        newAccount.setAccountType(sampleAccountType1);
        newAccount.setBranch(sampleBranch1);
        newAccount.setAccountName(sampleApplication1.getAccountName());

        //CustomerAccount objects 
        CustomerAccount customerAccount1 = new CustomerAccount();
        CustomerAccount customerAccount2 = new CustomerAccount();

        // ---------- Case 1: Success – ALL ok ----------
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.ACCEPTED);
        sampleApplication1.setEmployeeApprovalStatus(ApplicationStatus.PENDING);

        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));
        when(customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(201))
                .thenReturn(Arrays.asList(customerAccountOpeningApplication1, customerAccountOpeningApplication2));
        when(accountService.createAccount(any(Account.class), eq(samplePrincipal)))
                .thenReturn(newAccount);
        when(customerAccountService.createCustomerAccount(customerAccountOpeningApplication1.getId(), newAccount, samplePrincipal))
                .thenReturn(customerAccount1);
        when(customerAccountService.createCustomerAccount(customerAccountOpeningApplication2.getId(), newAccount, samplePrincipal))
                .thenReturn(customerAccount2);
        when(accountOpeningApplicationRepository.save(sampleApplication1))
                .thenReturn(sampleApplication1);

        List<CustomerAccount> created =
                accountOpeningApplicationService.approveApplication(201, samplePrincipal);

        assertEquals(2, created.size());

        // ---------- Case 2: Application ID not found ----------
        when(accountOpeningApplicationRepository.findById(999))
                .thenReturn(Optional.empty());

        ResourceNotFoundException eId = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.approveApplication(999, samplePrincipal);
        });
        assertEquals("No Account application record with the given id...!!!", eId.getMessage());

        // ---------- Case 3: Customer status PENDING ⇒ InvalidActionException ----------
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.PENDING);
        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException eCustomer = assertThrows(InvalidActionException.class, () -> {
            accountOpeningApplicationService.approveApplication(201, samplePrincipal);
        });
        assertEquals(
            "This account opening application is not eligible to approve...!!!  ",
            eCustomer.getMessage()
        );

        // ---------- Case 4: Employee status already REJECTED ----------
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.ACCEPTED);
        sampleApplication1.setEmployeeApprovalStatus(ApplicationStatus.REJECTED);
        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException eEmployeeRejected = assertThrows(InvalidActionException.class, () -> {
            accountOpeningApplicationService.approveApplication(201, samplePrincipal);
        });
        assertEquals(
            "This account opening application is already REJECTED.Cannot perform action...!!!  ",
            eEmployeeRejected.getMessage()
        );

        // ---------- Case 5: Employee status already ACCEPTED ----------
        sampleApplication1.setEmployeeApprovalStatus(ApplicationStatus.ACCEPTED);
        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException eEmployeeAccepted = assertThrows(InvalidActionException.class, () -> {
            accountOpeningApplicationService.approveApplication(201, samplePrincipal);
        });
        assertEquals(
            "This account opening application is already ACCEPTED...!!!  ",
            eEmployeeAccepted.getMessage()
        );

        // ---------- Case 6: No customer-application records ----------
        sampleApplication1.setEmployeeApprovalStatus(ApplicationStatus.PENDING); 
        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));
        when(customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(201))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException eNoCustomerAccountOpeningApplication= assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.approveApplication(201, samplePrincipal);
        });
        assertEquals(
            "No customer account opening applications with the given account opening application id...!!!",
            eNoCustomerAccountOpeningApplication.getMessage()
        );
    }

    
    
    
    
    @Test
    public void testRejectApplication() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        // ---------- Case 1: Success – customer ACCEPTED & employee PENDING ----------
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.ACCEPTED);
        sampleApplication1.setEmployeeApprovalStatus(ApplicationStatus.PENDING);

        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));
        when(accountOpeningApplicationRepository.save(sampleApplication1))
                .thenReturn(sampleApplication1);

        AccountOpeningApplication rejected =
                accountOpeningApplicationService.rejectApplication(201, samplePrincipal);

        assertEquals(ApplicationStatus.REJECTED, rejected.getEmployeeApprovalStatus());

        // ---------- Case 2: Application ID not found ----------
        when(accountOpeningApplicationRepository.findById(999))
                .thenReturn(Optional.empty());

        ResourceNotFoundException eId = assertThrows(ResourceNotFoundException.class, () -> {
            accountOpeningApplicationService.rejectApplication(999, samplePrincipal);
        });
        assertEquals("No Account application record with the given id...!!!", eId.getMessage());

        // ---------- Case 3: Customer status PENDING  → InvalidActionException ----------
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.PENDING);
        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException eCustomerPending = assertThrows(InvalidActionException.class, () -> {
            accountOpeningApplicationService.rejectApplication(201, samplePrincipal);
        });
        assertEquals(
            "This account opening application is not eligible to reject...!!!  ",
            eCustomerPending.getMessage()
        );

        // ---------- Case 4: Employee status already REJECTED ----------
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.ACCEPTED);
        sampleApplication1.setEmployeeApprovalStatus(ApplicationStatus.REJECTED);
        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException eEmployeeRejected = assertThrows(InvalidActionException.class, () -> {
            accountOpeningApplicationService.rejectApplication(201, samplePrincipal);
        });
        assertEquals(
            "This account opening application is already REJECTED.Cannot perform action...!!!  ",
            eEmployeeRejected.getMessage()
        );

        // ---------- Case 5: Employee status already ACCEPTED ----------
        sampleApplication1.setEmployeeApprovalStatus(ApplicationStatus.ACCEPTED);
        when(accountOpeningApplicationRepository.findById(201))
                .thenReturn(Optional.of(sampleApplication1));

        InvalidActionException eEmployeeAccepted = assertThrows(InvalidActionException.class, () -> {
            accountOpeningApplicationService.rejectApplication(201, samplePrincipal);
        });
        assertEquals(
            "This account opening application is already ACCEPTED...!!!  ",
            eEmployeeAccepted.getMessage()
        );
    }


    
    
    


 
    
    
    
    
    
    
    
    
    
    
    
    @AfterEach
    public void afterTest() {
        sampleUser = null;
        samplePrincipal = null;
        sampleBranch1 = null;
        sampleAccountType1 = null;
        sampleApplication1 = null;
        sampleApplication2 = null;
    }
}

