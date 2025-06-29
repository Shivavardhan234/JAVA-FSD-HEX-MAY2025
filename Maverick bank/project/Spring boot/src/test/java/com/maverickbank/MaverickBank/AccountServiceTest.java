package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.maverickbank.MaverickBank.enums.AccountStatus;
import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.enums.PaymentMedium;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.NotEnoughFundsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Account;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.CustomerAccount;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.CustomerRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.AccountService;
import com.maverickbank.MaverickBank.service.CustomerAccountService;
import com.maverickbank.MaverickBank.service.TransactionService;

@SpringBootTest
class AccountServiceTest {

    /* ===== service under test ===== */
    @InjectMocks
    private AccountService accountService;

    /* ===== mocked dependencies ===== */
    @Mock private AccountRepository          accountRepository;
    @Mock private UserRepository             userRepository;
    @Mock private TransactionService         transactionService;
    @Mock private PasswordEncoder            passwordEncoder;
    @Mock private CustomerAccountService     customerAccountService;
    @Mock private CustomerRepository         customerRepository;

    /* ===== shared sample data ===== */
    private User      sampleUser;
    private Principal samplePrincipal;

    private Branch          sampleBranch1;
    private AccountType     sampleAccountType1;
    private AccountType     sampleAccountType2;
    private Account         sampleAccount1;
    private Account         sampleAccount2;
    private AccountHolder   sampleAccountHolder1;
    private AccountHolder   sampleAccountHolder2;
    private CustomerAccount sampleCustomerAccount1;
    private	Customer sampleCustomer1;

    /* ===== set-up ===== */
    @BeforeEach
    public void init() throws Exception {
        /* --- User & Principal --- */
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("new_user");
        sampleUser.setPassword("userPassword@123");
        sampleUser.setRole(Role.CUSTOMER);
        sampleUser.setStatus(ActiveStatus.ACTIVE);

        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("new_user");

        /* --- Branch (unlinked) --- */
        sampleBranch1 = new Branch();
        sampleBranch1.setId(101);
        sampleBranch1.setIfsc("MVRK0000001");
        sampleBranch1.setBranchName("Hyderabad Central");
        sampleBranch1.setAddress("123 Central Ave, Hyderabad");
        sampleBranch1.setContactNumber("9999999999");
        sampleBranch1.setEmail("hydcentral@maverickbank.com");
        sampleBranch1.setStatus(ActiveStatus.ACTIVE);

        /* --- Account Types (unlinked) --- */
        sampleAccountType1 = new AccountType();
        sampleAccountType1.setAccountTypeId(201);
        sampleAccountType1.setAccountType(BankAccountType.SAVINGS);
        sampleAccountType1.setInterestRate(new BigDecimal("3.50"));
        sampleAccountType1.setMinimumBalance(new BigDecimal("1000.00"));
        sampleAccountType1.setTransactionLimit(10);
        sampleAccountType1.setTransactionAmountLimit(new BigDecimal("50000.00"));
        sampleAccountType1.setWithdrawLimit(5);

        sampleAccountType2 = new AccountType();
        sampleAccountType2.setAccountTypeId(202);
        sampleAccountType2.setAccountType(BankAccountType.CURRENT);
        sampleAccountType2.setInterestRate(new BigDecimal("0.00"));
        sampleAccountType2.setMinimumBalance(new BigDecimal("5000.00"));
        sampleAccountType2.setTransactionLimit(50);
        sampleAccountType2.setTransactionAmountLimit(new BigDecimal("100000.00"));
        sampleAccountType2.setWithdrawLimit(20);

        /* --- Accounts (NOT linked to branch/type yet) --- */
        sampleAccount1 = new Account();
        sampleAccount1.setId(301);
        sampleAccount1.setAccountNumber("11112222333");
        sampleAccount1.setAccountName("Primary Savings");
        sampleAccount1.setBalance(new BigDecimal("25000.00"));
        sampleAccount1.setAccountStatus(AccountStatus.OPEN);
        sampleAccount1.setKycCompliant(true);

        sampleAccount2 = new Account();
        sampleAccount2.setId(302);
        sampleAccount2.setAccountNumber("44445555666");
        sampleAccount2.setAccountName("Business Current");
        sampleAccount2.setBalance(new BigDecimal("125000.00"));
        sampleAccount2.setAccountStatus(AccountStatus.OPEN);
        sampleAccount2.setKycCompliant(false);

        /* --- Account Holders (stand-alone) --- */
        sampleAccountHolder1 = new AccountHolder();
        sampleAccountHolder1.setId(401);
        sampleAccountHolder1.setName("Alice Holder");
        sampleAccountHolder1.setDob(LocalDate.of(1995, 5, 20));
        sampleAccountHolder1.setGender(Gender.FEMALE);
        sampleAccountHolder1.setContactNumber("1234567890");
        sampleAccountHolder1.setEmail("alice.holder@example.com");
        sampleAccountHolder1.setAddress("123 Wonderland Ave");
        sampleAccountHolder1.setPanCardNumber("ABCPT1234Q");
        sampleAccountHolder1.setAadharNumber("123456789012");

        sampleAccountHolder2 = new AccountHolder();
        sampleAccountHolder2.setId(402);
        sampleAccountHolder2.setName("Bob Holder");
        sampleAccountHolder2.setDob(LocalDate.of(1992, 8, 15));
        sampleAccountHolder2.setGender(Gender.MALE);
        sampleAccountHolder2.setContactNumber("0987654321");
        sampleAccountHolder2.setEmail("bob.holder@example.com");
        sampleAccountHolder2.setAddress("456 Example Street");
        sampleAccountHolder2.setPanCardNumber("MKCAK5678Z");
        sampleAccountHolder2.setAadharNumber("987654321098");
        
        
        
        sampleCustomer1 = new Customer();
        sampleCustomer1.setId(101);
        sampleCustomer1.setName("Alice");
        sampleCustomer1.setDob(LocalDate.of(1995, 5, 20));
        sampleCustomer1.setGender(Gender.FEMALE);
        sampleCustomer1.setContactNumber("1234567890");
        sampleCustomer1.setEmail("alice@example.com");
        sampleCustomer1.setAddress("123 Wonderland Avenue");
        
        sampleCustomerAccount1 = new CustomerAccount();
        sampleCustomerAccount1.setId(501);
        sampleCustomerAccount1.setCustomer(sampleCustomer1);                 
        sampleCustomerAccount1.setAccountHolder(sampleAccountHolder1);     
        sampleCustomerAccount1.setAccount(sampleAccount1);                  
        sampleCustomerAccount1.setPin("$2a$10$abcdehashedpin1234567890");
        
        
        
        
    }

    
    
 //========================================== ADD ==========================================================
    @Test
    public void testCreateAccount() throws Exception {
        /* ---------- Common active-user stub ---------- */
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        /* ---------- Prep a valid account object ---------- */
        Account newAccount = new Account();
        newAccount.setAccountName("Flash Savings");
        newAccount.setBranch(sampleBranch1);
        newAccount.setAccountType(sampleAccountType1);

        
           //Case 1: SUCCESS 
        when(accountRepository.getByAccountNumber(Mockito.anyString()))
                .thenReturn(null);                                          
        when(accountRepository.save(newAccount)).thenAnswer(inv -> {
            newAccount.setId(777);                                             
            return newAccount;
        });

        Account created = accountService.createAccount(newAccount, samplePrincipal);

        assertEquals(777, created.getId());
        assertEquals(AccountStatus.OPEN, created.getAccountStatus());
        assertEquals(BigDecimal.ZERO, created.getBalance());
        assertFalse(created.getKycCompliant());
        assertNotNull(created.getAccountNumber());

       
          

       
          // Case 2: INVALID INPUT 
        Account badAccount = new Account();
        badAccount.setBranch(sampleBranch1);         

        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            accountService.createAccount(badAccount, samplePrincipal);
        });
        assertEquals(
            "Null account type object provided. Please provide appropriate account type object...!!!",
            eInvalid.getMessage()
        );
    }

    
    //========================================= get =======================================================
    @Test
    public void testGetAllAccounts() throws Exception {
        /* ---------- Common active-user stub ---------- */
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        Page<Account> mockPage = new PageImpl<>(Arrays.asList(sampleAccount1, sampleAccount2));
        /* ---------- Case 1 – Success: repository returns two accounts ---------- */
        when(accountRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(mockPage);

        List<Account> result =
                accountService.getAllAccounts(0,10,samplePrincipal);

        assertEquals(2, result.size());
        assertEquals(301, result.get(0).getId());
        assertEquals(302, result.get(1).getId());

        /* ---------- Case 2 - Empty list ---------- */
        Page<Account> emptyMockPage= new PageImpl<>(Arrays.asList());
        when(accountRepository.findAll(PageRequest.of(0, 10))).thenReturn(emptyMockPage);

        List<Account> empty = 
                accountService.getAllAccounts(0,10,samplePrincipal);

        assertTrue(empty.isEmpty());

        
    }
    
    
    @Test
    public void testGetAccountById() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        // ---------- Case 1: Success – account exists ----------
        when(accountRepository.findById(301)).thenReturn(Optional.of(sampleAccount1));

        Account found =
                accountService.getAccountById(301, samplePrincipal);

        assertEquals(301, found.getId());
        assertEquals("11112222333", found.getAccountNumber());
        assertEquals(AccountStatus.OPEN, found.getAccountStatus());

        // ---------- Case 2: Account not found ----------
        when(accountRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountById(999, samplePrincipal);
        });
        assertEquals(
            "No account record found with the given id...!!!",
            e.getMessage()
        );
    }


    
    
    @Test
    public void testGetAccountByAccountNumber() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        /* ---------- Case 1: Success – account exists ---------- */
        when(accountRepository.getByAccountNumber("11112222333"))
                .thenReturn(sampleAccount1);

        Account found =
                accountService.getAccountByAccountNumber("11112222333", samplePrincipal);

        assertEquals(301, found.getId());
        assertEquals("11112222333", found.getAccountNumber());
        assertEquals(AccountStatus.OPEN, found.getAccountStatus());

        /* ---------- Case 2: Account not found ---------- */
        when(accountRepository.getByAccountNumber("99990000111")).thenReturn(null);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountByAccountNumber("99990000111", samplePrincipal);
        });
        assertEquals(
            "No account record found with the given account number...!!!",
            e.getMessage()
        );
    }

    
    
    
    @Test
    public void testGetAccountsByBranchId() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        int branchId = 101;  // sampleBranch1 ID

        /* ---------- Case 1: Success - list returned ---------- */
        // Link accounts to branch only inside this test
        sampleAccount1.setBranch(sampleBranch1);
        sampleAccount2.setBranch(sampleBranch1);

        when(accountRepository.getByBranchId(branchId,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList(sampleAccount1, sampleAccount2));

        List<Account> accounts =
                accountService.getAccountsByBranchId(0,10,branchId, samplePrincipal);

        assertEquals(2, accounts.size());
        assertEquals(301, accounts.get(0).getId());
        assertEquals(302, accounts.get(1).getId());

        /* ---------- Case 2: Empty list  ---------- */
        when(accountRepository.getByBranchId(branchId,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountsByBranchId(0,10,branchId, samplePrincipal);
        });
        assertEquals(
            "No account records found with the given branch id...!!!",
            e.getMessage()
        );
    }

    
    
    
    @Test
    public void testGetAccountsByStatus() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        // Ensure our sample has the correct status for this test
        sampleAccount1.setAccountStatus(AccountStatus.OPEN);

        /* ---------- Case 1: Success - list returned for OPEN ---------- */
        when(accountRepository.getByAccountStatus(AccountStatus.OPEN,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList(sampleAccount1));

        List<Account> openAccounts =
                accountService.getAccountsByStatus(0,10,AccountStatus.OPEN, samplePrincipal);

        assertEquals(1, openAccounts.size());
        assertEquals(301, openAccounts.get(0).getId());
        assertEquals(AccountStatus.OPEN, openAccounts.get(0).getAccountStatus());

        /* ---------- Case 2: Empty list ---------- */
        when(accountRepository.getByAccountStatus(AccountStatus.OPEN,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountsByStatus(0,10,AccountStatus.OPEN, samplePrincipal);
        });
        assertEquals(
            "No account records found with the given account status...!!!",
            e.getMessage()
        );
    }

    
    
    
    @Test
    public void testGetAccountsByAccountTypeId() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        int typeId = 201;                                

        // Link sampleAccount1 to that type for this test only
        sampleAccount1.setAccountType(sampleAccountType1);

        /* ---------- Case 1: Success – list returned ---------- */
        when(accountRepository.getByAccountTypeId(typeId,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList(sampleAccount1));

        List<Account> result =
                accountService.getAccountsByAccountTypeId(0,10,typeId, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(301, result.get(0).getId());
        assertEquals(sampleAccountType1, result.get(0).getAccountType());

        /* ---------- Case 2: Empty list  ---------- */
        when(accountRepository.getByAccountTypeId(typeId,PageRequest.of(0, 10)))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountsByAccountTypeId(0,10,typeId, samplePrincipal);
        });
        assertEquals(
            "No account records found with the given account type id...!!!",
            e.getMessage()
        );
    }

 //============================================ UPDATE =====================================================
    @Test
    public void testUpdateAccountName() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        /* ---------- Case 1 – Success: name updated ---------- */
        when(accountRepository.findById(301)).thenReturn(Optional.of(sampleAccount1));
        when(accountRepository.save(sampleAccount1)).thenReturn(sampleAccount1);

        Account updated =
                accountService.updateAccountName(301, "Updated Name", samplePrincipal);

        assertEquals("Updated Name", updated.getAccountName());

        /* ---------- Case 2 – newName is null ---------- */
        when(accountRepository.findById(301)).thenReturn(Optional.of(sampleAccount1));

        InvalidInputException eNull = assertThrows(InvalidInputException.class, () -> {
            accountService.updateAccountName(301, null, samplePrincipal);
        });
        assertEquals("New name cannot be empty", eNull.getMessage());

        

        /* ---------- Case 4 – account ID not found  ---------- */
        when(accountRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException eId = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.updateAccountName(999, "Some Name", samplePrincipal);
        });
        assertEquals(
            "No accountRecord with the givwn id...!!!",
            eId.getMessage()
        );
    }

    
    
    
    @Test
    public void testUpdateAccountStatus() throws Exception {
        // Common active-user stub
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        /* -------- Case 1: success – status updated to CLOSED -------- */
        when(accountRepository.findById(301)).thenReturn(Optional.of(sampleAccount1));
        when(accountRepository.save(sampleAccount1)).thenReturn(sampleAccount1);

        Account closed =
                accountService.updateAccountStatus(301, AccountStatus.CLOSED, samplePrincipal);

        assertEquals(AccountStatus.CLOSED, closed.getAccountStatus());

        /* -------- Case 2: invalid status (null) -> InvalidInputException -------- */
        when(accountRepository.findById(301)).thenReturn(Optional.of(sampleAccount1));

        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            accountService.updateAccountStatus(301, null, samplePrincipal);
        });
        assertEquals(
            "Null account status provided. Account status should not be null...!!!",
            eInvalid.getMessage()
        );

        /* -------- Case 3: account ID not found -> ResourceNotFoundException -------- */
        when(accountRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException eId = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.updateAccountStatus(999, AccountStatus.SUSPENDED, samplePrincipal);
        });
        assertEquals(
            "No account found with the given id...!!!",
            eId.getMessage()
        );
    }

    
    
    
    
    
    
    
    
    
    
    @Test
    public void testWithdraw() throws Exception {
        // Setup: active user and customer
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(customerRepository.getByUserId(sampleUser.getId())).thenReturn(sampleCustomer1);

        // Simulate account ownership
        when(customerAccountService.getByCustomerIdAndAccountId(101, 301, samplePrincipal))
            .thenReturn(sampleCustomerAccount1);

        // PIN match
        when(passwordEncoder.matches("4321", sampleCustomerAccount1.getPin())).thenReturn(true);

        // Setup account with enough balance
        sampleAccount1.setBalance(new BigDecimal("1000.00"));
        when(accountRepository.findById(301)).thenReturn(Optional.of(sampleAccount1));

        
        BigDecimal withdrawAmount = new BigDecimal("250.50");
       
        when(accountRepository.save(sampleAccount1)).thenReturn(sampleAccount1);

        // Success case
        Account updated = accountService.withdraw(301, withdrawAmount, PaymentMedium.UPI, "4321", samplePrincipal);
        assertEquals(new BigDecimal("749.50"), updated.getBalance());

        
        
        // CASE 2 Incorrect PIN case
        when(passwordEncoder.matches("wrong", sampleCustomerAccount1.getPin())).thenReturn(false);

        InvalidActionException ePin = assertThrows(InvalidActionException.class, () -> {
            accountService.withdraw(301, withdrawAmount, PaymentMedium.UPI, "wrong", samplePrincipal);
        });
        assertEquals("Incorrect pin. Please enter correct pin...!!!", ePin.getMessage());

        
        
        
        
        //CASE 3 Insufficient balance
        sampleAccount1.setBalance(new BigDecimal("100.00")); // less than withdrawal
        when(passwordEncoder.matches("4321", sampleCustomerAccount1.getPin())).thenReturn(true);

        NotEnoughFundsException eFunds = assertThrows(NotEnoughFundsException.class, () -> {
            accountService.withdraw(301, withdrawAmount, PaymentMedium.UPI, "4321", samplePrincipal);
        });
        assertEquals("You dont have enough balance to perform transaction...!!!", eFunds.getMessage());
    }

    
    
    
    
    
    
    
    
    
    @Test
    public void testTransfer() throws Exception {
        // ---------- Common active-user / customer stubs ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(customerRepository.getByUserId(sampleUser.getId())).thenReturn(sampleCustomer1);

        
        when(customerAccountService.getByCustomerIdAndAccountId(101, 301, samplePrincipal))
            .thenReturn(sampleCustomerAccount1);

        
        sampleAccount1.setBalance(new BigDecimal("1000.00"));
        sampleAccount1.setAccountStatus(AccountStatus.OPEN);
        when(accountRepository.findById(301)).thenReturn(Optional.of(sampleAccount1));

        // Destination account
        Account destAccount = new Account();
        destAccount.setId(302);
        destAccount.setAccountNumber("44445555666");
        destAccount.setBalance(new BigDecimal("500.00"));
        destAccount.setAccountStatus(AccountStatus.OPEN);

        when(accountRepository.getByAccountNumber("44445555666")).thenReturn(destAccount);
        when(accountRepository.save(sampleAccount1)).thenReturn(sampleAccount1);
        when(accountRepository.save(destAccount)).thenReturn(destAccount);

        // Correct PIN
        when(passwordEncoder.matches("4321", sampleCustomerAccount1.getPin())).thenReturn(true);

        // ---------- Case 1: success ----------
        BigDecimal amount = new BigDecimal("250.00");
        Account updated =
            accountService.transfer(301, "44445555666", amount, "4321", samplePrincipal);

        assertEquals(new BigDecimal("750.00"), updated.getBalance());
        assertEquals(new BigDecimal("750.00"), sampleAccount1.getBalance());
        assertEquals(new BigDecimal("750.00"), destAccount.getBalance());

        // ---------- Case 2: incorrect PIN ----------
        when(passwordEncoder.matches("9999", sampleCustomerAccount1.getPin())).thenReturn(false);

        InvalidActionException ePin = assertThrows(
            InvalidActionException.class,
            () -> accountService.transfer(301, "44445555666", amount, "9999", samplePrincipal)
        );
        assertEquals("Incorrect pin. Please enter correct pin...!!!", ePin.getMessage());

        // ---------- Case 3: insufficient balance ----------
        sampleAccount1.setBalance(new BigDecimal("100.00")); // less than transfer amount
        when(passwordEncoder.matches("4321", sampleCustomerAccount1.getPin())).thenReturn(true);

        NotEnoughFundsException eFunds = assertThrows(
            NotEnoughFundsException.class,
            () -> accountService.transfer(301, "44445555666", amount, "4321", samplePrincipal)
        );
        assertEquals("You don't have enough balance to perform transaction...!!!", eFunds.getMessage());

        // ---------- Case 4: destination account not found ----------
        when(accountRepository.getByAccountNumber("99990000111")).thenReturn(null);

        ResourceNotFoundException eDest = assertThrows(
            ResourceNotFoundException.class,
            () -> accountService.transfer(301, "99990000111", amount, "4321", samplePrincipal)
        );
        assertEquals("No account record found with the given toAccountNumber...!!!", eDest.getMessage());

        // ---------- Case 5: destination account suspended ----------
        destAccount.setAccountStatus(AccountStatus.SUSPENDED);
        when(accountRepository.getByAccountNumber("44445555666")).thenReturn(destAccount);

        InvalidActionException eSusp = assertThrows(
            InvalidActionException.class,
            () -> accountService.transfer(301, "44445555666", amount, "4321", samplePrincipal)
        );
        assertEquals(
            "Reciever bank account nor responding! Please try again after some time...!!!",
            eSusp.getMessage()
        );
    }
    
    
    @Test
    public void testUpdateKyc() throws Exception {
        // ---------- Common: Active user ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        // ---------- Case 1: success ----------
        sampleAccount1.setKycCompliant(false);
        when(accountRepository.findById(301)).thenReturn(Optional.of(sampleAccount1));
        when(accountRepository.save(sampleAccount1)).thenReturn(sampleAccount1);
        Account updated = accountService.updateKyc(301, true, samplePrincipal);

        assertTrue(updated.getKycCompliant());

        // ---------- Case 2: account not found ----------
        when(accountRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.updateKyc(999, true, samplePrincipal);
        });

        assertEquals("No account found with the given id...!!!", e.getMessage());
    }


    
    
    
    
    
    
    
    
    
    
   
    @AfterEach
    public void afterTest() {
        sampleUser = null;
        samplePrincipal = null;
        sampleBranch1 = null;
        sampleAccountType1 = null;
        sampleAccountType2 = null;
        sampleAccount1 = null;
        sampleAccount2 = null;
        sampleAccountHolder1 = null;
        sampleAccountHolder2 = null;
        sampleCustomerAccount1=null;
        sampleCustomer1=null;
    }

}
