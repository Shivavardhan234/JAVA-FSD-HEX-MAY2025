package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.maverickbank.MaverickBank.enums.AccountStatus;
import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.CustomerAccount;
import com.maverickbank.MaverickBank.model.CustomerAccountOpeningApplication;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.repository.CustomerAccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.CustomerAccountRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.AccountHolderService;
import com.maverickbank.MaverickBank.service.CustomerAccountService;

@SpringBootTest
class CustomerAccountServiceTest {

    /* ===== Service under test ===== */
    @InjectMocks
    private CustomerAccountService customerAccountService;

    /* ===== Mocked dependencies ===== */
    @Mock 
    private CustomerAccountRepository                     customerAccountRepository;
    @Mock 
    private UserRepository                                 userRepository;
    @Mock 
    private CustomerAccountOpeningApplicationRepository    customerAccountOpeningApplicationRepository;
    @Mock 
    private PasswordEncoder                                passwordEncoder;
    @Mock 
    private AccountHolderService                           accountHolderService;

    
    
    /* ===== Shared sample data ===== */
    private User                 sampleUser1;
    private Principal            samplePrincipal;

    private AccountType          sampleAccountType1;
    private Customer             sampleCustomer1;
    private AccountHolder        sampleHolder1;
    private Account              sampleAccount1;
    private CustomerAccount      sampleCustomerAccount1;

    /* ===== Set-up ===== */
    @BeforeEach
    public void init() throws InvalidInputException {
       
        sampleUser1 = new User();
        sampleUser1.setId(1);
        sampleUser1.setUsername("new_user");
        sampleUser1.setPassword("userPassword@123");
        sampleUser1.setRole(Role.CUSTOMER);
        sampleUser1.setStatus(ActiveStatus.ACTIVE);

        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("new_user");

       
        sampleAccountType1 = new AccountType();
        sampleAccountType1.setAccountTypeId(201);
        sampleAccountType1.setAccountType(BankAccountType.SAVINGS);
        sampleAccountType1.setInterestRate(new BigDecimal("3.50"));
        sampleAccountType1.setMinimumBalance(new BigDecimal("1000.00"));
        sampleAccountType1.setTransactionLimit(10);
        sampleAccountType1.setTransactionAmountLimit(new BigDecimal("50000.00"));
        sampleAccountType1.setWithdrawLimit(5);

       
        sampleCustomer1 = new Customer();
        sampleCustomer1.setId(101);
        sampleCustomer1.setName("Alice");
        sampleCustomer1.setDob(LocalDate.of(1995, 5, 20));
        sampleCustomer1.setGender(Gender.FEMALE);
        sampleCustomer1.setContactNumber("1234567890");
        sampleCustomer1.setEmail("alice@example.com");
        sampleCustomer1.setAddress("123 Wonderland Avenue");
        sampleCustomer1.setUser(sampleUser1);

      
        sampleHolder1 = new AccountHolder();
        sampleHolder1.setId(101);
        sampleHolder1.setName("Ravi Kumar");
        sampleHolder1.setDob(LocalDate.of(1995, 3, 10));
        sampleHolder1.setGender(Gender.MALE);
        sampleHolder1.setContactNumber("9876543210");
        sampleHolder1.setEmail("ravi.kumar@example.com");
        sampleHolder1.setAddress("123 Main St, Hyderabad");
        sampleHolder1.setPanCardNumber("ABCPT1234Q");
        sampleHolder1.setAadharNumber("123456789012");

      
        sampleAccount1 = new Account();
        sampleAccount1.setId(301);
        sampleAccount1.setAccountNumber("11112222333");
        sampleAccount1.setAccountName("Primary Savings");
        sampleAccount1.setAccountStatus(AccountStatus.OPEN);
        sampleAccount1.setBalance(new BigDecimal("5000.00"));
        sampleAccount1.setKycCompliant(false);
       
        
        sampleCustomerAccount1 = new CustomerAccount();
        sampleCustomerAccount1.setId(501);
        sampleCustomerAccount1.setCustomer(sampleCustomer1);
        sampleCustomerAccount1.setAccountHolder(sampleHolder1);
        sampleCustomerAccount1.setAccount(sampleAccount1);
        sampleCustomerAccount1.setPin("$2a$10$hashedPINstringFor4321"); 
    }

  
    
//==============================================================================================
    @Test
    public void testCreateCustomerAccount() throws Exception {
        // ---------- common active–user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser1);

        /* ---------- build a CustomerAccountOpeningApplication record ---------- */
        CustomerAccountOpeningApplication caoApplication = new CustomerAccountOpeningApplication();
        caoApplication.setId(601);
        caoApplication.setCustomer(sampleCustomer1);
        caoApplication.setAccountHolder(sampleHolder1);
        caoApplication.setAccountOpeningApplication(new AccountOpeningApplication());
        caoApplication.getAccountOpeningApplication().setCustomerApprovalStatus(ApplicationStatus.ACCEPTED);
        caoApplication.getAccountOpeningApplication().setEmployeeApprovalStatus(ApplicationStatus.ACCEPTED);

        // repository returns the application by id
        when(customerAccountOpeningApplicationRepository.findById(601))
                .thenReturn(Optional.of(caoApplication));

        // PIN build: first 6 digits of "1234567890" -> "123456"
        when(passwordEncoder.encode("123456")).thenReturn("$hashed$123456");

        // expect save to return the same object
        CustomerAccount created = new CustomerAccount();
        created.setId(777);
        created.setCustomer(sampleCustomer1);
        created.setAccountHolder(sampleHolder1);
        created.setAccount(sampleAccount1);
        created.setPin("$hashed$123456");

        when(customerAccountRepository.save(any(CustomerAccount.class))).thenReturn(created);

        // ---------- Case 1 : success ----------
        CustomerAccount result =
                customerAccountService.createCustomerAccount(601, sampleAccount1, samplePrincipal);

        assertEquals(777, result.getId());
        assertEquals(sampleCustomer1, result.getCustomer());
        assertEquals(sampleAccount1, result.getAccount());
        assertEquals("$hashed$123456", result.getPin());

        // ---------- Case 2 : application not accepted ----------
        caoApplication.getAccountOpeningApplication().setCustomerApprovalStatus(ApplicationStatus.PENDING);

        InvalidInputException eInvalid = assertThrows(
            InvalidInputException.class,
            () -> customerAccountService.createCustomerAccount(601, sampleAccount1, samplePrincipal)
        );
        assertEquals(
            "Acceptance not proper for the provided customer account opening application...!!!",
            eInvalid.getMessage()
        );

        // ---------- Case 3 : id not found ----------
        when(customerAccountOpeningApplicationRepository.findById(999))
                .thenReturn(Optional.empty());

        ResourceNotFoundException eNotFound = assertThrows(
            ResourceNotFoundException.class,
            () -> customerAccountService.createCustomerAccount(999, sampleAccount1, samplePrincipal)
        );
        assertEquals(
            "No application record found with the given id...!!!",
            eNotFound.getMessage()
        );
    }
    
 //========================================= GET ==============================================
    @Test
    public void testGetById() throws Exception {
        // Common active-user stub
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser1);

        // -------- Case 1: Success – record exists --------
        when(customerAccountRepository.findById(501)).thenReturn(Optional.of(sampleCustomerAccount1));

        CustomerAccount found =customerAccountService.getById(501, samplePrincipal);

        assertEquals(501, found.getId());
        assertEquals(sampleCustomer1, found.getCustomer());
        assertEquals(sampleAccount1, found.getAccount());

        // -------- Case 2: Record not found --------
        when(customerAccountRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
            ResourceNotFoundException.class,
            () -> customerAccountService.getById(999, samplePrincipal)
        );
        assertEquals(
            "No customer account recotd found with the given id...!!! ",
            e.getMessage()
        );
    }
    
    
    
    
    @Test
    public void testGetAllCustomerAccounts() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser1);

        // ---------- Case 1: Success – repository returns one record ----------
        when(customerAccountRepository.findAll())
                .thenReturn(Arrays.asList(sampleCustomerAccount1));

        List<CustomerAccount> result =
                customerAccountService.getAllCustomerAccounts(samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(501, result.get(0).getId());

        // ---------- Case 2: Empty list ----------
        when(customerAccountRepository.findAll()).thenReturn(Arrays.asList());

        List<CustomerAccount> empty =
                customerAccountService.getAllCustomerAccounts(samplePrincipal);

        assertTrue(empty.isEmpty());

        // ---------- Case 3: Repository returns null ----------
        when(customerAccountRepository.findAll()).thenReturn(null);

        List<CustomerAccount> nullResult =
                customerAccountService.getAllCustomerAccounts(samplePrincipal);

        assertNull(nullResult);
    }
    
    
    
    
    @Test
    public void testGetByCustomerId() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser1);

        int customerId = 101;   // sampleCustomer1 ID

        // ---------- Case 1: Success – list returned ----------
        when(customerAccountRepository.getByCustomerId(customerId))
                .thenReturn(Arrays.asList(sampleCustomerAccount1));

        List<CustomerAccount> result =
                customerAccountService.getByCustomerId(customerId, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(501, result.get(0).getId());
        assertEquals(sampleCustomer1, result.get(0).getCustomer());

        // ---------- Case 2: Empty list ⇒ ResourceNotFoundException ----------
        when(customerAccountRepository.getByCustomerId(customerId))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(
            ResourceNotFoundException.class,
            () -> customerAccountService.getByCustomerId(customerId, samplePrincipal)
        );
        assertEquals(
            "No customer accounts found with customer id...!!!",
            e.getMessage()
        );
    }

    
    
    
    @Test
    public void testGetByAccountHolderId() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser1);

        int holderId = 101;   // sampleHolder1 ID

        // ---------- Case 1: Success – list returned ----------
        when(customerAccountRepository.getByAccountHolderId(holderId))
                .thenReturn(Arrays.asList(sampleCustomerAccount1));

        List<CustomerAccount> result =
                customerAccountService.getByAccountHolderId(holderId, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(501, result.get(0).getId());
        assertEquals(sampleHolder1, result.get(0).getAccountHolder());

        // ---------- Case 2: Empty list -> ResourceNotFoundException ----------
        when(customerAccountRepository.getByAccountHolderId(holderId))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(
            ResourceNotFoundException.class,
            () -> customerAccountService.getByAccountHolderId(holderId, samplePrincipal)
        );
        assertEquals(
            "No customer accounts found with account holder id...!!!",
            e.getMessage()
        );
    }


    
    @Test
    public void testGetByAccountId() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser1);

        int accountId = 301;   // sampleAccount1 ID

        // ---------- Case 1: Success – list returned ----------
        when(customerAccountRepository.getByAccountId(accountId))
                .thenReturn(Arrays.asList(sampleCustomerAccount1));

        List<CustomerAccount> result =
                customerAccountService.getByAccountId(accountId, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(501, result.get(0).getId());
        assertEquals(sampleAccount1, result.get(0).getAccount());

        // ---------- Case 2: Empty list  ResourceNotFoundException ----------
        when(customerAccountRepository.getByAccountId(accountId))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(
            ResourceNotFoundException.class,
            () -> customerAccountService.getByAccountId(accountId, samplePrincipal)
        );
        assertEquals("No customer accounts found with account id...!!!", e.getMessage());
    }


    @Test
    public void testGetByCustomerIdAndAccountId() throws Exception {
        // Setup user
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser1);
        
        when(customerAccountRepository.getByCustomerIdAndAccountId(101, 301))
                .thenReturn(sampleCustomerAccount1);

        // Success Case
        CustomerAccount result = customerAccountService.getByCustomerIdAndAccountId(101, 301, samplePrincipal);
        assertEquals(sampleCustomerAccount1, result);

        // Failure Case
        when(customerAccountRepository.getByCustomerIdAndAccountId(999, 888)).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            customerAccountService.getByCustomerIdAndAccountId(999, 888, samplePrincipal);
        });

        assertEquals("No customer accounts found with this customerId and account id...!!!", ex.getMessage());
    }

    
    
    

    
    
    
 //============================================= UPDATE =======================================
    @Test
    public void testUpdateAccountHolder() throws Exception {
        
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser1);

        
        when(customerAccountRepository.findById(501))
                .thenReturn(Optional.of(sampleCustomerAccount1));

        // New account-holder to add
        AccountHolder newHolder = new AccountHolder();
        newHolder.setId(555);
        newHolder.setName("New Holder");
        newHolder.setDob(LocalDate.of(1990, 4, 4));
        newHolder.setGender(Gender.MALE);
        newHolder.setContactNumber("1112223333");
        newHolder.setEmail("new.holder@example.com");
        newHolder.setAddress("789 New Street");
        newHolder.setPanCardNumber("XYZPF1234L");
        newHolder.setAadharNumber("111122223333");

        // accountHolderService returns the saved holder (same object, id already set)
        when(accountHolderService.addAccountHolder(newHolder, samplePrincipal))
                .thenReturn(newHolder);

        // Save returns the updated customer-account
        when(customerAccountRepository.save(sampleCustomerAccount1))
                .thenReturn(sampleCustomerAccount1);

        // ----- Case 1 : success -----
        CustomerAccount updated =
                customerAccountService.updateAccountHolder(501, newHolder, samplePrincipal);

        assertEquals(555, updated.getAccountHolder().getId());
        assertEquals("New Holder", updated.getAccountHolder().getName());

        // ----- Case 2 : customer-account id not found -----
        when(customerAccountRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
            ResourceNotFoundException.class,
            () -> customerAccountService.updateAccountHolder(999, newHolder, samplePrincipal)
        );
        assertEquals(
            "No customer account recotd found with the given id...!!! ",
            e.getMessage()
        );
    }

    
    
    
    
    
    
    @AfterEach
    public void afterTest() {
        sampleUser1           = null;
        samplePrincipal       = null;
        sampleAccountType1    = null;
        sampleCustomer1       = null;
        sampleHolder1         = null;
        sampleAccount1        = null;
        sampleCustomerAccount1 = null;
    }

}

