package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.*;
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
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.maverickbank.MaverickBank.dto.CustomerAccountOpeningInputDto;
import com.maverickbank.MaverickBank.enums.ActiveStatus;
import com.maverickbank.MaverickBank.enums.ApplicationStatus;
import com.maverickbank.MaverickBank.enums.BankAccountType;
import com.maverickbank.MaverickBank.enums.Gender;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.AccountHolder;
import com.maverickbank.MaverickBank.model.AccountOpeningApplication;
import com.maverickbank.MaverickBank.model.AccountType;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.CustomerAccountOpeningApplication;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.model.users.Customer;
import com.maverickbank.MaverickBank.repository.AccountHolderRepository;
import com.maverickbank.MaverickBank.repository.CustomerAccountOpeningApplicationRepository;
import com.maverickbank.MaverickBank.repository.CustomerRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.AccountOpeningApplicationService;
import com.maverickbank.MaverickBank.service.CustomerAccountOpeningApplicationService;


@SpringBootTest
class CustomerAccountOpeningApplicationServiceTest {

    
    @InjectMocks
    private CustomerAccountOpeningApplicationService customerAccountOpeningApplicationService;

    
    @Mock 
    private CustomerAccountOpeningApplicationRepository customerAccountOpeningApplicationRepository;
    @Mock 
    private UserRepository                          userRepository;
    @Mock 
    private AccountHolderRepository                  accountHolderRepository;
    @Mock 
    private AccountOpeningApplicationService         accountOpeningApplicationService;
    @Mock 
    private CustomerRepository                       customerRepository;

    
    private User sampleUser;
    private Principal samplePrincipal;

    private Branch sampleBranch1;
    private AccountType sampleAccountType1;

    private Customer sampleCustomer1;
    private AccountHolder sampleAccountHolder1;

    private AccountOpeningApplication sampleApplication1;
    private CustomerAccountOpeningApplication sampleCustApp1;

    @BeforeEach
    public void init() throws InvalidInputException {
        /* --- sample user & principal --- */
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("new_user");
        sampleUser.setPassword("userPassword@123");
        sampleUser.setRole(Role.CUSTOMER);
        sampleUser.setStatus(ActiveStatus.ACTIVE);

        samplePrincipal = mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("new_user");

        /* --- sample branch --- */
        sampleBranch1 = new Branch();
        sampleBranch1.setId(101);
        sampleBranch1.setIfsc("MVRK0000001");
        sampleBranch1.setBranchName("Hyderabad Central");
        sampleBranch1.setAddress("123 Central Ave, Hyderabad");
        sampleBranch1.setContactNumber("9999999999");
        sampleBranch1.setEmail("hydcentral@maverickbank.com");
        sampleBranch1.setStatus(ActiveStatus.ACTIVE);

        /* --- sample account type --- */
        sampleAccountType1 = new AccountType();
        sampleAccountType1.setAccountTypeId(101);
        sampleAccountType1.setAccountType(BankAccountType.JOINT_SAVINGS);
        sampleAccountType1.setInterestRate(new BigDecimal("3.5"));
        sampleAccountType1.setMinimumBalance(new BigDecimal("1000.00"));
        sampleAccountType1.setTransactionLimit(10);
        sampleAccountType1.setTransactionAmountLimit(new BigDecimal("50000.00"));
        sampleAccountType1.setWithdrawLimit(5);

        /* --- sample customer --- */
        sampleCustomer1 = new Customer();
        sampleCustomer1.setId(201);
        sampleCustomer1.setName("Alice");
        sampleCustomer1.setDob(LocalDate.of(1995, 5, 20));
        sampleCustomer1.setGender(Gender.FEMALE);
        sampleCustomer1.setContactNumber("1234567890");
        sampleCustomer1.setEmail("alice@example.com");
        sampleCustomer1.setAddress("123 Wonderland Avenue");
        sampleCustomer1.setUser(sampleUser);

        /* --- sample account holder --- */
        sampleAccountHolder1 = new AccountHolder();
        sampleAccountHolder1.setId(301);
        sampleAccountHolder1.setName("John Doe");
        sampleAccountHolder1.setDob(LocalDate.of(1990, 1, 1));
        sampleAccountHolder1.setGender(Gender.MALE);
        sampleAccountHolder1.setContactNumber("9876543210");
        sampleAccountHolder1.setEmail("john.doe@example.com");
        sampleAccountHolder1.setAddress("456 Example Street");
        sampleAccountHolder1.setPanCardNumber("ABCPT1234Q");
        sampleAccountHolder1.setAadharNumber("123456789012");

        /* --- sample account-opening application --- */
        sampleApplication1 = new AccountOpeningApplication();
        sampleApplication1.setId(401);
        sampleApplication1.setBranch(sampleBranch1);
        sampleApplication1.setAccountType(sampleAccountType1);
        sampleApplication1.setAccountName("Alice Joint Savings");
        sampleApplication1.setPurpose("Joint Savings");
        sampleApplication1.setCustomerApprovalStatus(ApplicationStatus.PENDING);
        sampleApplication1.setEmployeeApprovalStatus(ApplicationStatus.PENDING);
        sampleApplication1.setApplicationDateTime(LocalDateTime.now());

        /* --- sample customer-account-opening application --- */
        sampleCustApp1 = new CustomerAccountOpeningApplication();
        sampleCustApp1.setId(501);
        sampleCustApp1.setCustomer(sampleCustomer1);
        sampleCustApp1.setAccountHolder(sampleAccountHolder1);
        sampleCustApp1.setAccountOpeningApplication(sampleApplication1);
        sampleCustApp1.setCustomerApproval(ApplicationStatus.PENDING);
    }
    
    
  //======================================= ADD ==========================================
    
    @Test
    public void testAddcustomerAccountOpeningApplication() throws Exception {
        /* ---------- Common stubs ---------- */
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        /* ---------- SUCCESS PATH ---------- */
        // ‣ Two holders (joint account)
        AccountHolder holder2 = new AccountHolder();
        holder2.setId(302);
        holder2.setName("Bob B");
        holder2.setDob(LocalDate.of(1994, 8, 15));
        holder2.setGender(Gender.MALE);
        holder2.setContactNumber("0987654321");
        holder2.setEmail("bob@example.com");
        holder2.setAddress("789 Example Blvd");
        holder2.setPanCardNumber("MKCAK5678Z");
        holder2.setAadharNumber("987654321098");

        // customer record for second holder
        Customer sampleCustomer2 = new Customer();
        sampleCustomer2.setId(202);
        sampleCustomer2.setName("Bob B");
        sampleCustomer2.setContactNumber("0987654321");

        // DTO with two holders
        CustomerAccountOpeningInputDto dto = new CustomerAccountOpeningInputDto();
        dto.setAccountHolderList(Arrays.asList(sampleAccountHolder1, holder2));
        dto.setAccountOpeningApplication(sampleApplication1);

        // repository look-ups
        when(customerRepository.getByContactNumber("9876543210")).thenReturn(sampleCustomer1);
        when(customerRepository.getByContactNumber("0987654321")).thenReturn(sampleCustomer2);
        when(customerRepository.getByUsername("new_user")).thenReturn(sampleCustomer1);

        // saveAll of holders returns same list
        when(accountHolderRepository.saveAll(dto.getAccountHolderList()))
                .thenReturn(dto.getAccountHolderList());

        // add application 
        when(accountOpeningApplicationService.addAccountOpeningApplication(
                sampleApplication1, samplePrincipal))
                .thenReturn(sampleApplication1);

        // list saved back from CAOA repo
        CustomerAccountOpeningApplication customerAccountOpeningApplication1 = new CustomerAccountOpeningApplication();
        customerAccountOpeningApplication1.setId(601);
        customerAccountOpeningApplication1.setCustomer(sampleCustomer1);
        customerAccountOpeningApplication1.setAccountHolder(sampleAccountHolder1);
        customerAccountOpeningApplication1.setAccountOpeningApplication(sampleApplication1);
        customerAccountOpeningApplication1.setCustomerApproval(ApplicationStatus.ACCEPTED);

        CustomerAccountOpeningApplication customerAccountOpeningApplication2 = new CustomerAccountOpeningApplication();
        customerAccountOpeningApplication2.setId(602);
        customerAccountOpeningApplication2.setCustomer(sampleCustomer2);
        customerAccountOpeningApplication2.setAccountHolder(holder2);
        customerAccountOpeningApplication2.setAccountOpeningApplication(sampleApplication1);
        customerAccountOpeningApplication2.setCustomerApproval(ApplicationStatus.PENDING);

        when(customerAccountOpeningApplicationRepository.saveAll(Mockito.anyList()))
                .thenReturn(Arrays.asList(customerAccountOpeningApplication1, customerAccountOpeningApplication2));

        // update approval status (return value not used here)
        when(accountOpeningApplicationService.updateCustomerApprovalStatus(
                sampleApplication1.getId(), samplePrincipal))
                .thenReturn(sampleApplication1);

        // 
        List<CustomerAccountOpeningApplication> created =
                customerAccountOpeningApplicationService.addcustomerAccountOpeningApplication(dto, samplePrincipal);

        assertEquals(2, created.size());
        assertEquals(601, created.get(0).getId());
        assertEquals(602, created.get(1).getId());

        /* ---------- FAILURE: holder list size < 2 (joint) ---------- */
        CustomerAccountOpeningInputDto smallDto = new CustomerAccountOpeningInputDto();
        smallDto.setAccountHolderList(Arrays.asList(sampleAccountHolder1));       // only one
        smallDto.setAccountOpeningApplication(sampleApplication1);

        InvalidInputException eSize = assertThrows(InvalidInputException.class, () -> {
            customerAccountOpeningApplicationService.addcustomerAccountOpeningApplication(smallDto, samplePrincipal);
        });

        assertEquals(
        	    "Number of account holders are invalid...!!!",
        	    eSize.getMessage()
        	);
        /* ---------- FAILURE: phone number not found ---------- */
        when(customerRepository.getByContactNumber("0987654321")).thenReturn(null);  // second holder missing

        CustomerAccountOpeningInputDto missingDto = new CustomerAccountOpeningInputDto();
        missingDto.setAccountHolderList(Arrays.asList(sampleAccountHolder1, holder2));
        missingDto.setAccountOpeningApplication(sampleApplication1);

        ResourceNotFoundException ePhone = assertThrows(ResourceNotFoundException.class, () -> {
            customerAccountOpeningApplicationService.addcustomerAccountOpeningApplication(missingDto, samplePrincipal);
        });
        assertEquals(
        	    "Cannot submit application,No customer exist with given phone number0987654321",
        	    ePhone.getMessage()
        	);

        
    }
    
    
    
 //============================================= GET ==========================================   
    
    @Test
    public void testGetAllApplications() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        // Case 1: Success  
        when(customerAccountOpeningApplicationRepository.findAll())
                .thenReturn(Arrays.asList(sampleCustApp1));

        List<CustomerAccountOpeningApplication> applications =
                customerAccountOpeningApplicationService.getAllApplications(samplePrincipal);

        assertEquals(1, applications.size());
        assertEquals(501, applications.get(0).getId());

       

        
    }
    
    
    
    
    @Test
    public void testGetById() throws Exception {
        /* ---------- Case 1: Success – application exists ---------- */
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(customerAccountOpeningApplicationRepository.findById(501))
                .thenReturn(Optional.of(sampleCustApp1));

        CustomerAccountOpeningApplication found =
                customerAccountOpeningApplicationService.getById(501, samplePrincipal);

        assertEquals(501, found.getId());
        assertEquals(sampleCustomer1, found.getCustomer());
        assertEquals(sampleApplication1, found.getAccountOpeningApplication());

        /* ---------- Case 2: Application ID not found ---------- */
        when(customerAccountOpeningApplicationRepository.findById(999))
                .thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            customerAccountOpeningApplicationService.getById(999, samplePrincipal);
        });
        assertEquals(
            "No customer account opening Application found with the given id...!!! ",
            e.getMessage()
        );
    }

    
    
    
    @Test
    public void testGetByCustomerId() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        // ---------- Case 1: Success – list returned ----------
        when(customerAccountOpeningApplicationRepository.getByCustomerId(201))
                .thenReturn(Arrays.asList(sampleCustApp1));

        List<CustomerAccountOpeningApplication> result =
                customerAccountOpeningApplicationService.getByCustomerId(201, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(501, result.get(0).getId());

        // ---------- Case 2: Empty list ----------
        when(customerAccountOpeningApplicationRepository.getByCustomerId(201))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            customerAccountOpeningApplicationService.getByCustomerId(201, samplePrincipal);
        });
        assertEquals(
            "No application records found with given customer id...!!!",
            e.getMessage()
        );

        
    }
    
    
    
    
    
    @Test
    public void testGetByAccountHolderId() throws Exception {
        // ---------- Common setup ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        // ---------- Case 1: Success ----------
        when(customerAccountOpeningApplicationRepository.getByAccountHolderId(301))
                .thenReturn(Arrays.asList(sampleCustApp1));

        List<CustomerAccountOpeningApplication> result =
                customerAccountOpeningApplicationService.getByAccountHolderId(301, samplePrincipal);

        assertEquals(1, result.size());
        assertEquals(501, result.get(0).getId());

        // ---------- Case 2: Empty List ----------
        when(customerAccountOpeningApplicationRepository.getByAccountHolderId(301))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            customerAccountOpeningApplicationService.getByAccountHolderId(301, samplePrincipal);
        });
        assertEquals("No application records found with the given account holder id...!!!", e.getMessage());

       
        
    }

    
    
    @Test
    public void testGetByAccountOpeningApplicationId() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        // ---------- Case 1: Success – list returned ----------
        when(customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(401))
                .thenReturn(Arrays.asList(sampleCustApp1));

        List<CustomerAccountOpeningApplication> ok =
                customerAccountOpeningApplicationService.getByAccountOpeningApplicationId(
                        401, samplePrincipal);

        assertEquals(1, ok.size());
        assertEquals(501, ok.get(0).getId());

        // ---------- Case 2: Empty list ----------
        when(customerAccountOpeningApplicationRepository.getByAccountOpeningApplicationId(401))
                .thenReturn(Arrays.asList());

       
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            customerAccountOpeningApplicationService.getByAccountOpeningApplicationId(
                    401, samplePrincipal);
        });
        assertEquals(
            "No application records found with the given account opening application id...!!!",
            e.getMessage()
        );
    }
    
    
    
    @Test
    public void testGetByCustomerIdAndStatus() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        int customerId = 201;                       // sampleCustomer1 ID
        ApplicationStatus status = ApplicationStatus.PENDING;

        // ---------- Case 1: Success – list returned ----------
        sampleCustApp1.setCustomerApproval(status);
        when(customerAccountOpeningApplicationRepository.getByCustomerIdAndStatus(customerId, status))
                .thenReturn(Arrays.asList(sampleCustApp1));

        List<CustomerAccountOpeningApplication> ok =
                customerAccountOpeningApplicationService.getByCustomerIdAndStatus(
                        customerId, status, samplePrincipal);

        assertEquals(1, ok.size());
        assertEquals(501, ok.get(0).getId());
        assertEquals(status, ok.get(0).getCustomerApproval());

        // ---------- Case 2: Empty list ----------
        when(customerAccountOpeningApplicationRepository.getByCustomerIdAndStatus(customerId, status))
                .thenReturn(Arrays.asList());

       
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            customerAccountOpeningApplicationService.getByCustomerIdAndStatus(
                    customerId, status, samplePrincipal);
        });
        assertEquals(
            "No application records found with the given customer id and status...!!!",
            e.getMessage()
        );
    }

    
    
    
    
    @Test
    public void testGetCustomerApprovalPending() throws Exception {
        // ---------- Common active-user stub ----------
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(customerRepository.getByUserId(sampleUser.getId())).thenReturn(sampleCustomer1);

        int customerId = sampleCustomer1.getId();

        // ---------- Case 1: Success – list returned ----------
        sampleCustApp1.setCustomerApproval(ApplicationStatus.PENDING);
        when(customerAccountOpeningApplicationRepository
                .getCustomerApprovalPending(customerId, ApplicationStatus.PENDING))
                .thenReturn(Arrays.asList(sampleCustApp1));

        List<CustomerAccountOpeningApplication> pending =
                customerAccountOpeningApplicationService.getCustomerApprovalPending(samplePrincipal);

        assertEquals(1, pending.size());
        assertEquals(501, pending.get(0).getId());
        assertEquals(ApplicationStatus.PENDING, pending.get(0).getCustomerApproval());

        // ---------- Case 2: Empty list ----------
        when(customerAccountOpeningApplicationRepository
                .getCustomerApprovalPending(customerId, ApplicationStatus.PENDING))
                .thenReturn(Arrays.asList());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, ()->customerAccountOpeningApplicationService.getCustomerApprovalPending(samplePrincipal));
                

        assertEquals("No pending application records found with the given customer id...!!!",e.getMessage() );
        
    }
    
    
    
    
    
    
    
 //========================================= UPDATE =======================================================
    @Test
    public void testUpdateCustomerAccountOpeningApplication() throws Exception {
        /* ---------- Common active-user stub ---------- */
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

        /* ---------- Case 1: Success – update customerApproval & accountHolder ---------- */
        // Current record returned by repo
        when(customerAccountOpeningApplicationRepository.findById(501))
                .thenReturn(Optional.of(sampleCustApp1));
        when(customerAccountOpeningApplicationRepository.save(sampleCustApp1))
                .thenReturn(sampleCustApp1);

        // Build update request
        CustomerAccountOpeningApplication updateReq = new CustomerAccountOpeningApplication();
        updateReq.setId(501);
        updateReq.setCustomerApproval(ApplicationStatus.ACCEPTED);
        updateReq.setAccountHolder(sampleAccountHolder1);      

        CustomerAccountOpeningApplication updated =
                customerAccountOpeningApplicationService
                        .updateCustomerAccountOpeningApplication(updateReq, samplePrincipal);

        assertEquals(501, updated.getId());
        assertEquals(ApplicationStatus.ACCEPTED, updated.getCustomerApproval());
        assertEquals(sampleAccountHolder1, updated.getAccountHolder());

        /* ---------- Case 2: ID not found ---------- */
        when(customerAccountOpeningApplicationRepository.findById(999))
                .thenReturn(Optional.empty());

        CustomerAccountOpeningApplication badIdReq = new CustomerAccountOpeningApplication();
        badIdReq.setId(999);

        ResourceNotFoundException eId = assertThrows(ResourceNotFoundException.class, () -> {
            customerAccountOpeningApplicationService
                    .updateCustomerAccountOpeningApplication(badIdReq, samplePrincipal);
        });
        assertEquals(
            "No application record found with the provided id...!!!",
            eId.getMessage()
        );

        
    }






    
    
    
    
    
    
    

    @AfterEach
    public void afterTest() {
        sampleUser = null;
        samplePrincipal = null;
        sampleBranch1 = null;
        sampleAccountType1 = null;
        sampleCustomer1 = null;
        sampleAccountHolder1 = null;
        sampleApplication1 = null;
        sampleCustApp1 = null;
    }

    /* ===== test methods will be added below, one per service method ===== */
}

