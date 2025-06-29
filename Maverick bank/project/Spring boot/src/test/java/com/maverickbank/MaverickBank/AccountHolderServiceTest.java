package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountHolderRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.AccountHolderService;

@SpringBootTest
class AccountHolderServiceTest {

    @InjectMocks
    private AccountHolderService accountHolderService;

    @Mock
    private AccountHolderRepository accountHolderRepository;

    @Mock
    private UserRepository userRepository;

    private User sampleUser;
    private Principal samplePrincipal;
    private AccountHolder sampleHolder1;
    private AccountHolder sampleHolder2;

    @BeforeEach
    public void init() throws InvalidInputException {
        // Sample User (Principal)
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("new_user");
        sampleUser.setPassword("userPassword@123");
        sampleUser.setRole(Role.CUSTOMER);
        sampleUser.setStatus(ActiveStatus.ACTIVE);

        // Sample Principal
        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("new_user");

        // Sample Account Holder 1
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

        // Sample Account Holder 2
        sampleHolder2 = new AccountHolder();
        sampleHolder2.setId(102);
        sampleHolder2.setName("Sita Rani");
        sampleHolder2.setDob(LocalDate.of(1990, 7, 22));
        sampleHolder2.setGender(Gender.FEMALE);
        sampleHolder2.setContactNumber("8765432109");
        sampleHolder2.setEmail("sita.rani@example.com");
        sampleHolder2.setAddress("456 North Ave, Mumbai");
        sampleHolder2.setPanCardNumber("MKCAK5678Z");
        sampleHolder2.setAadharNumber("987654321098");
    }

    
  //===================================== ADD ===========================================  
    
    
    
    @Test
    public void testAddAccountHolder() throws Exception {
        // ---------- Case 1: Success – all details valid ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountHolderRepository.save(sampleHolder1)).thenReturn(sampleHolder1);

        AccountHolder added = accountHolderService.addAccountHolder(sampleHolder1, samplePrincipal);

        assertEquals(101, added.getId());
        assertEquals("Ravi Kumar", added.getName());
        assertEquals("9876543210", added.getContactNumber());
        assertEquals("ABCPT1234Q", added.getPanCardNumber());
        assertEquals("123456789012", added.getAadharNumber());

        // ---------- Case 2: Null object  InvalidInputException ----------
        InvalidInputException eNull = assertThrows(InvalidInputException.class, () -> {
            accountHolderService.addAccountHolder(null, samplePrincipal);
        });
        assertEquals("provided account holder object is null", eNull.getMessage());

      
    }
    
    
    
 //================================= GET ================================================
    @Test
    public void testGetAllAccountHolder() throws Exception {
        // ---------- Case 1: Success – repository returns two holders ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountHolderRepository.findAll())
                .thenReturn(Arrays.asList(sampleHolder1, sampleHolder2));

        List<AccountHolder> allHolders = accountHolderService.getAllAccountHolder(samplePrincipal);

        assertEquals(2, allHolders.size());
        assertEquals("Ravi Kumar",  allHolders.get(0).getName());
        assertEquals("Sita Rani",   allHolders.get(1).getName());

        // ---------- Case 2: Empty list ----------
        when(accountHolderRepository.findAll()).thenReturn(Arrays.asList());

        List<AccountHolder> emptyList = accountHolderService.getAllAccountHolder(samplePrincipal);

        assertTrue(emptyList.isEmpty());

        // ---------- Case 3: Repository returns null ----------
        when(accountHolderRepository.findAll()).thenReturn(null);

        List<AccountHolder> nullResult = accountHolderService.getAllAccountHolder(samplePrincipal);

        assertNull(nullResult);
    }
    
    
    
    @Test
    public void testGetAccountHolderById() throws Exception {
        // Case 1
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountHolderRepository.findById(101)).thenReturn(Optional.of(sampleHolder1));

        AccountHolder found = accountHolderService.getAccountHolderById(101, samplePrincipal);

        assertEquals(101, found.getId());
        assertEquals("Ravi Kumar", found.getName());
        assertEquals("ABCPT1234Q", found.getPanCardNumber());

        //Case 2
        when(accountHolderRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            accountHolderService.getAccountHolderById(999, samplePrincipal);
        });
        assertEquals("No Account holder record with goven id...!!!", eNotFound.getMessage());
    }
    
    
    @Test
    public void testGetAccountHolderByEmail() throws Exception {
        // ---------- Case 1: Success – list returned ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountHolderRepository.getByEmail(sampleHolder1.getEmail()))
                .thenReturn(Arrays.asList(sampleHolder1));

        List<AccountHolder> found =
                accountHolderService.getAccountHolderByEmail(
                        sampleHolder1.getEmail(), samplePrincipal);

        assertEquals(1, found.size());
        assertEquals("Ravi Kumar", found.get(0).getName());
        assertEquals("ravi.kumar@example.com", found.get(0).getEmail());

        // ---------- Case 2: No record  ResourceNotFoundException ----------
        when(accountHolderRepository.getByEmail("ghost@example.com")).thenReturn(Arrays.asList());

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            accountHolderService.getAccountHolderByEmail("ghost@example.com", samplePrincipal);
        });
        assertEquals("No Account holder record with goven email...!!!", eNotFound.getMessage());

        // ---------- Case 3: Invalid email  InvalidInputException ----------
        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            accountHolderService.getAccountHolderByEmail("bad-email", samplePrincipal);
        });
        assertEquals("Email is Invalid. Please enter appropriate Email...!!!", eInvalid.getMessage());
    }

    
    
    @Test
    public void testGetAccountHolderByContactNumber() throws Exception {
        // ---------- Case 1: Success  holders with given contact number ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountHolderRepository.getByContactNumber(sampleHolder1.getContactNumber()))
                .thenReturn(Arrays.asList(sampleHolder1));

        List<AccountHolder> found =
                accountHolderService.getAccountHolderByContactNumber(
                        sampleHolder1.getContactNumber(), samplePrincipal);

        assertEquals(1, found.size());
        assertEquals("Ravi Kumar", found.get(0).getName());
        assertEquals("9876543210", found.get(0).getContactNumber());

        // ---------- Case 2: No record  ResourceNotFoundException ----------
        when(accountHolderRepository.getByContactNumber("0000000000")).thenReturn(Arrays.asList());

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            accountHolderService.getAccountHolderByContactNumber("0000000000", samplePrincipal);
        });
        assertEquals("No Account holder record with goven contact number...!!!", eNotFound.getMessage());

        // ---------- Case 3: Invalid contact number  InvalidInputException ----------
        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            accountHolderService.getAccountHolderByContactNumber("123", samplePrincipal);  // too short / invalid
        });
        assertEquals(
            "Contact number is Invalid. Please enter appropriate 10 digit Contact number...!!!",
            eInvalid.getMessage()
        );
    }

    
    
    
    @Test
    public void testGetAccountHolderByAadharNumber() throws Exception {
        // ---------- Case 1: Success – list returned ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountHolderRepository.getByAadharNumber(sampleHolder1.getAadharNumber()))
                .thenReturn(Arrays.asList(sampleHolder1));

        List<AccountHolder> found =
                accountHolderService.getAccountHolderByAadharNumber(
                        sampleHolder1.getAadharNumber(), samplePrincipal);

        assertEquals(1, found.size());
        assertEquals("Ravi Kumar", found.get(0).getName());
        assertEquals("123456789012", found.get(0).getAadharNumber());

        // ---------- Case 2: No record  ResourceNotFoundException ----------
        when(accountHolderRepository.getByAadharNumber("111111111111")).thenReturn(Arrays.asList());

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            accountHolderService.getAccountHolderByAadharNumber("111111111111", samplePrincipal);
        });
        assertEquals("No Account holder record with goven aadhar number...!!!", eNotFound.getMessage());

        // ---------- Case 3: Invalid Aadhar  InvalidInputException ----------
        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            accountHolderService.getAccountHolderByAadharNumber("123", samplePrincipal);  // too short / invalid
        });
        assertEquals(
            "Invalid Aadhar card number. please enter appropriate Aadhar card number...!!!",
            eInvalid.getMessage()
        );
    }

    
    
    
    @Test
    public void testGetAccountHolderByPanCardNumber() throws Exception {
        // ---------- Case 1: Success  list returned ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountHolderRepository.getByPancardNumber(sampleHolder1.getPanCardNumber()))
                .thenReturn(Arrays.asList(sampleHolder1));

        List<AccountHolder> found =
                accountHolderService.getAccountHolderByPanCardNumber(
                        sampleHolder1.getPanCardNumber(), samplePrincipal);

        assertEquals(1, found.size());
        assertEquals("Ravi Kumar", found.get(0).getName());
        assertEquals("ABCPT1234Q", found.get(0).getPanCardNumber());

        // ---------- Case 2: No record  ResourceNotFoundException ----------
        when(accountHolderRepository.getByPancardNumber("ZZZPA9999Z")).thenReturn(Arrays.asList());

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            accountHolderService.getAccountHolderByPanCardNumber("ZZZPA9999Z", samplePrincipal);
        });
        assertEquals("No Account holder record with goven pan card number...!!!", eNotFound.getMessage());

        // ---------- Case 3: Invalid PAN  InvalidInputException ----------
        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            accountHolderService.getAccountHolderByPanCardNumber("BADPAN123", samplePrincipal); // invalid pattern
        });
        assertEquals(
            "Invalid PAN card number. please enter appropriate PAN card number...!!!",
            eInvalid.getMessage()
        );
    }

    
//========================================= UPDATE =======================================
    
    @Test
    public void testUpdateAccountHolder() throws Exception {
        // ---------- Case 1: Success  selective fields updated ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(accountHolderRepository.findById(101)).thenReturn(Optional.of(sampleHolder1));
        when(accountHolderRepository.save(sampleHolder1)).thenReturn(sampleHolder1);

        AccountHolder updateRequest = new AccountHolder();
        updateRequest.setId(101);
        updateRequest.setName("Ravi K Kumar");          
        updateRequest.setEmail("ravi.kumar@update.com"); 
        updateRequest.setContactNumber("9999999999");    

        AccountHolder updated =
                accountHolderService.updateAccountHolder(updateRequest, samplePrincipal);

        assertEquals(101, updated.getId());
        assertEquals("Ravi K Kumar", updated.getName());
        assertEquals("ravi.kumar@update.com", updated.getEmail());
        assertEquals("9999999999", updated.getContactNumber());

        // ---------- Case 2: ID not found  ResourceNotFoundException ----------
        when(accountHolderRepository.findById(555)).thenReturn(Optional.empty());

        AccountHolder notFoundRequest = new AccountHolder();
        notFoundRequest.setId(555);

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            accountHolderService.updateAccountHolder(notFoundRequest, samplePrincipal);
        });
        assertEquals("No account holder record with given id...!!!", eNotFound.getMessage());

        
    }




    
    
    
    
    
    
    
    
    
    
    
    
    
    @AfterEach
    public void afterTest() {
        sampleUser = null;
        samplePrincipal = null;
        sampleHolder1 = null;
        sampleHolder2 = null;
    }
}

