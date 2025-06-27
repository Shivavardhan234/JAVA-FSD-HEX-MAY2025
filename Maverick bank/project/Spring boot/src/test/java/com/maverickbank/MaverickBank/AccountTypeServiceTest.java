package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountTypeRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.AccountTypeService;

@SpringBootTest
public class AccountTypeServiceTest {

    @InjectMocks
    private AccountTypeService accountTypeService;

    @Mock
    private AccountTypeRepository accountTypeRepository;

    @Mock
    private UserRepository userRepository;

    User sampleUser;
    Principal samplePrincipal;
    AccountType sampleAccountType1;
    AccountType sampleAccountType2;

    @BeforeEach
    public void init() throws InvalidInputException {
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("admin_user");
        sampleUser.setPassword("adminPassword@123");
        sampleUser.setRole(Role.ADMIN);
        sampleUser.setStatus(ActiveStatus.ACTIVE);

        sampleAccountType1 = new AccountType();
        sampleAccountType1.setAccountTypeId(101);
        sampleAccountType1.setAccountType(BankAccountType.SAVINGS);
        sampleAccountType1.setInterestRate(new BigDecimal("3.5"));
        sampleAccountType1.setMinimumBalance(new BigDecimal("1000.00"));
        sampleAccountType1.setTransactionLimit(10);
        sampleAccountType1.setTransactionAmountLimit(new BigDecimal("50000.00"));
        sampleAccountType1.setWithdrawLimit(5);

        sampleAccountType2 = new AccountType();
        sampleAccountType2.setAccountTypeId(102);
        sampleAccountType2.setAccountType(BankAccountType.CURRENT);
        sampleAccountType2.setInterestRate(new BigDecimal("0.0"));
        sampleAccountType2.setMinimumBalance(new BigDecimal("5000.00"));
        sampleAccountType2.setTransactionLimit(20);
        sampleAccountType2.setTransactionAmountLimit(new BigDecimal("100000.00"));
        sampleAccountType2.setWithdrawLimit(10);

        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("admin_user");
    }
    
    
    
    
    
 //======================================= ADD ============================================
    @Test
    public void testAddAccountType() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("admin_user")).thenReturn(sampleUser);
        when(accountTypeRepository.getAccountTypeByName(BankAccountType.SAVINGS)).thenReturn(null);
        when(accountTypeRepository.save(sampleAccountType1)).thenReturn(sampleAccountType1);

        AccountType result = accountTypeService.addAccountType(sampleAccountType1, samplePrincipal);

        assertEquals(BankAccountType.SAVINGS, result.getAccountType());
        assertEquals(new BigDecimal("3.5"), result.getInterestRate());
        assertEquals(10, result.getTransactionLimit());

        // Case 2: Duplicate account type
        when(accountTypeRepository.getAccountTypeByName(BankAccountType.SAVINGS)).thenReturn(sampleAccountType1);

        ResourceExistsException ex = assertThrows(ResourceExistsException.class, () -> {
            accountTypeService.addAccountType(sampleAccountType1, samplePrincipal);
        });
        assertEquals("Bank Account type already exists...!!! ", ex.getMessage());
    }

    
 //====================================== GET ============================================
    
    @Test
    public void testGetAllAccountType() throws Exception {
        when(userRepository.getByUsername("admin_user")).thenReturn(sampleUser);
        
        List<AccountType> types = Arrays.asList(sampleAccountType1, sampleAccountType2);
        Page<AccountType> mockPage = new PageImpl<>(types);

        when(accountTypeRepository.findAll(PageRequest.of(0, 2))).thenReturn(mockPage);

        List<AccountType> result = accountTypeService.getAllAccountType(0, 2, samplePrincipal);
        
        assertEquals(2, result.size());
        assertEquals(BankAccountType.SAVINGS, result.get(0).getAccountType());
        assertEquals(BankAccountType.CURRENT, result.get(1).getAccountType());
    }
    
    
    @Test
    public void testGetAccountTypeByName() throws Exception {
        when(userRepository.getByUsername("admin_user")).thenReturn(sampleUser);
        when(accountTypeRepository.getAccountTypeByName(BankAccountType.SAVINGS)).thenReturn(sampleAccountType1);

        AccountType result = accountTypeService.getAccountTypeByName(BankAccountType.SAVINGS, samplePrincipal);

        assertEquals(BankAccountType.SAVINGS, result.getAccountType());
        assertEquals(new BigDecimal("3.5"), result.getInterestRate());
    }

    
    
    @Test
    public void testGetAccountTypeById() throws Exception {
        // Case 1: Success
        when(userRepository.getByUsername("admin_user")).thenReturn(sampleUser);
        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(sampleAccountType1));

        AccountType result = accountTypeService.getAccountTypeById(1, samplePrincipal);

        assertEquals(101, result.getAccountTypeId());
        assertEquals(BankAccountType.SAVINGS, result.getAccountType());
        assertEquals(new BigDecimal("3.5"), result.getInterestRate());

        // Case 2: Not Found
        when(accountTypeRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            accountTypeService.getAccountTypeById(99, samplePrincipal);
        });

        assertEquals("No Account type record with given Id...!!!", ex.getMessage());
    }
 
    
 //=============================== UPDATE ===============================================
    
    
    @Test
    public void testUpdateAccountType_AllCases() throws Exception {
        // ---------- Setup ----------
        when(userRepository.getByUsername("admin_user")).thenReturn(sampleUser);
        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(sampleAccountType1));

        AccountType updatedInput = new AccountType();
        updatedInput.setAccountTypeId(1); 
        updatedInput.setInterestRate(new BigDecimal("4.0"));
        updatedInput.setMinimumBalance(new BigDecimal("2000.00"));
        updatedInput.setTransactionLimit(30);
        updatedInput.setTransactionAmountLimit(new BigDecimal("100000.00"));
        updatedInput.setWithdrawLimit(20);

        AccountType expectedUpdated = new AccountType();
        expectedUpdated.setAccountTypeId(1);
        expectedUpdated.setAccountType(BankAccountType.SAVINGS);
        expectedUpdated.setInterestRate(new BigDecimal("4.0"));
        expectedUpdated.setMinimumBalance(new BigDecimal("2000.00"));
        expectedUpdated.setTransactionLimit(30);
        expectedUpdated.setTransactionAmountLimit(new BigDecimal("100000.00"));
        expectedUpdated.setWithdrawLimit(20);

        when(accountTypeRepository.save(any(AccountType.class))).thenReturn(expectedUpdated);

        //Case 1: Success
        AccountType result = accountTypeService.updateAccountType(updatedInput, samplePrincipal);

        assertEquals(new BigDecimal("4.0"), result.getInterestRate());
        assertEquals(new BigDecimal("2000.00"), result.getMinimumBalance());
        assertEquals(30, result.getTransactionLimit());
        assertEquals(new BigDecimal("100000.00"), result.getTransactionAmountLimit());
        assertEquals(20, result.getWithdrawLimit());

        //Case 2: AccountType ID not found 
        when(accountTypeRepository.findById(99)).thenReturn(Optional.empty());

        AccountType inputWithWrongId = new AccountType();
        inputWithWrongId.setAccountTypeId(99);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            accountTypeService.updateAccountType(inputWithWrongId, samplePrincipal);
        });

        assertEquals("No Account type record with given Id...!!!", ex.getMessage());
    }



    
    
    
    
    
    

    @AfterEach
    public void cleanup() {
        sampleUser = null;
        sampleAccountType1 = null;
        sampleAccountType2 = null;
        samplePrincipal = null;
    }
}
