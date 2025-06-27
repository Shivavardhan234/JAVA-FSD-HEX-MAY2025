package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
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
import com.maverickbank.MaverickBank.enums.PaymentMedium;
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.enums.TransactionType;
import com.maverickbank.MaverickBank.exception.InvalidActionException;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Transaction;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.AccountRepository;
import com.maverickbank.MaverickBank.repository.TransactionRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.TransactionService;


@SpringBootTest
class TransactionServiceTest {

	 @InjectMocks
	    private TransactionService transactionService;

	    @Mock
	    private TransactionRepository transactionRepository;

	    @Mock
	    private UserRepository userRepository;

	    @Mock
	    private AccountRepository accountRepository;

	    // ------------------------ Sample Data ------------------------
	    private Transaction sampleTransaction1;
	    private  Transaction sampleTransaction2;
	    private  User sampleUser;
	    private  Principal samplePrincipal;

	    @BeforeEach
	    public void init() throws InvalidInputException {
	        sampleTransaction1 = new Transaction();
	        sampleTransaction1.setTransactionId(1);
	        sampleTransaction1.setFromDetails("1234567890");
	        sampleTransaction1.setFromPaymentMedium(PaymentMedium.ACCOUNT);
	        sampleTransaction1.setToDetails("wallet@upi");
	        sampleTransaction1.setToPaymentMedium(PaymentMedium.UPI);
	        sampleTransaction1.setTransactionType(TransactionType.DEBT);
	        sampleTransaction1.setTransactionAmount(new BigDecimal("1500.00"));
	        sampleTransaction1.setTransactionDate(LocalDate.of(2024, 12, 25));
	        sampleTransaction1.setTransactionTime(LocalTime.of(10, 30));
	        sampleTransaction1.setTransactionDescription("Rent Payment");

	        sampleTransaction2 = new Transaction();
	        sampleTransaction2.setTransactionId(2);
	        sampleTransaction2.setFromDetails("wallet@upi");
	        sampleTransaction2.setFromPaymentMedium(PaymentMedium.UPI);
	        sampleTransaction2.setToDetails("1234567890");
	        sampleTransaction2.setToPaymentMedium(PaymentMedium.ACCOUNT);
	        sampleTransaction2.setTransactionType(TransactionType.CREDIT);
	        sampleTransaction2.setTransactionAmount(new BigDecimal("2000.00"));
	        sampleTransaction2.setTransactionDate(LocalDate.of(2025, 1, 1));
	        sampleTransaction2.setTransactionTime(LocalTime.of(12, 15));
	        sampleTransaction2.setTransactionDescription("Refund");
	        
	        sampleUser = new User();
	        sampleUser.setId(1);
	        sampleUser.setUsername("new_user");
	        sampleUser.setPassword("userPassword@123");
	        sampleUser.setRole(Role.TRANSACTION_ANALYST);
	        sampleUser.setStatus(ActiveStatus.ACTIVE);
	        
	        
	        samplePrincipal = mock(Principal.class);
	        when(samplePrincipal.getName()).thenReturn("new_user");

	    }

//===================================== ADD ===============================================
	    
	    @Test
	    public void testCreateTransaction() throws Exception {
	        // Case 1: Success
	        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

	        Transaction validTransaction = new Transaction();
	        validTransaction.setFromDetails("1234567890");
	        validTransaction.setFromPaymentMedium(PaymentMedium.ACCOUNT);
	        validTransaction.setToDetails("wallet@upi");
	        validTransaction.setToPaymentMedium(PaymentMedium.UPI);
	        validTransaction.setTransactionType(TransactionType.DEBT);
	        validTransaction.setTransactionAmount(new BigDecimal("1000.00"));
	        validTransaction.setTransactionDescription("Bill payment");

	        Transaction savedTransaction = new Transaction();
	        savedTransaction.setTransactionId(10);
	        savedTransaction.setFromDetails(validTransaction.getFromDetails());
	        savedTransaction.setFromPaymentMedium(validTransaction.getFromPaymentMedium());
	        savedTransaction.setToDetails(validTransaction.getToDetails());
	        savedTransaction.setToPaymentMedium(validTransaction.getToPaymentMedium());
	        savedTransaction.setTransactionType(validTransaction.getTransactionType());
	        savedTransaction.setTransactionAmount(validTransaction.getTransactionAmount());
	        savedTransaction.setTransactionDescription(validTransaction.getTransactionDescription());
	        savedTransaction.setTransactionDate(LocalDate.now());
	        savedTransaction.setTransactionTime(LocalTime.now());

	        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

	        Transaction result = transactionService.createTransaction(validTransaction, samplePrincipal);

	        assertEquals(10, result.getTransactionId());
	        assertEquals("1234567890", result.getFromDetails());
	        assertEquals("wallet@upi", result.getToDetails());
	        assertEquals(TransactionType.DEBT, result.getTransactionType());
	        assertEquals(PaymentMedium.ACCOUNT, result.getFromPaymentMedium());
	        assertEquals(PaymentMedium.UPI, result.getToPaymentMedium());
	        assertEquals(new BigDecimal("1000.00"), result.getTransactionAmount());
	        assertEquals("Bill payment", result.getTransactionDescription());

	        // Case 2: From and To Details are same
	        Transaction invalidSameDetails = new Transaction();
	        invalidSameDetails.setFromDetails("1234567890");
	        invalidSameDetails.setToDetails("1234567890");
	        invalidSameDetails.setFromPaymentMedium(PaymentMedium.ACCOUNT);
	        invalidSameDetails.setToPaymentMedium(PaymentMedium.WALLET);
	        invalidSameDetails.setTransactionAmount(new BigDecimal("500.00"));
	        invalidSameDetails.setTransactionType(TransactionType.DEBT);

	        when(userRepository.getByUsername("cio_user")).thenReturn(sampleUser);

	        InvalidActionException e1 = assertThrows(InvalidActionException.class, () -> {
	            transactionService.createTransaction(invalidSameDetails, samplePrincipal);
	        });
	        assertEquals("FAILED TO STORE TRANSACTION. From and to details are same...!!!", e1.getMessage());

	        // Case 3: Neither from nor to medium is ACCOUNT
	        Transaction invalidMediums = new Transaction();
	        invalidMediums.setFromDetails("wallet@upi");
	        invalidMediums.setToDetails("bank@xyz");
	        invalidMediums.setFromPaymentMedium(PaymentMedium.UPI);
	        invalidMediums.setToPaymentMedium(PaymentMedium.BANK);
	        invalidMediums.setTransactionAmount(new BigDecimal("600.00"));
	        invalidMediums.setTransactionType(TransactionType.CREDIT);

	        when(userRepository.getByUsername("cio_user")).thenReturn(sampleUser);

	        InvalidActionException e2 = assertThrows(InvalidActionException.class, () -> {
	            transactionService.createTransaction(invalidMediums, samplePrincipal);
	        });
	        assertEquals("Invalid transaction provided...!!!", e2.getMessage());
	    }
	    
	    
//====================================== GET ALL ========================================
	    @Test
	    public void testGetAllTransactions() throws Exception {
	        
	        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
	        when(transactionRepository.findAll()).thenReturn(Arrays.asList(sampleTransaction1, sampleTransaction2));

	        List<Transaction> result = transactionService.getAllTransactions(samplePrincipal);
	        assertEquals(Arrays.asList(sampleTransaction1, sampleTransaction2), result);

	       
	    }

	    @Test
	    public void testGetTransactionById() throws Exception {
	        // Case 1: Success
	        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
	        when(transactionRepository.findById(1)).thenReturn(Optional.of(sampleTransaction1));

	        Transaction result = transactionService.getById(1, samplePrincipal);
	        assertEquals(sampleTransaction1, result);

	        // Case 2: Transaction not found
	        when(transactionRepository.findById(99)).thenReturn(Optional.empty());
	        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () ->
	            transactionService.getById(99, samplePrincipal)
	        );
	        assertEquals("No Transaction found with the given id...!!! ", e.getMessage());
	    }

	    
	    @Test
	    public void testGetTransactionsByDateRange() throws Exception {
	        // Arrange
	        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

	        LocalDate startDate = LocalDate.of(2024, 12, 1);
	        LocalDate endDate = LocalDate.of(2025, 1, 10);

	        // Case 1:
	        String accNum = "1234567890";
	        when(transactionRepository.findByAccountNumberAndDateRange(accNum, startDate, endDate))
	            .thenReturn(Arrays.asList(sampleTransaction1, sampleTransaction2)); // Both refer to 1234567890

	        List<Transaction> resultWithAcc = transactionService.getTransactionsByDateRange(Optional.of(accNum), startDate, endDate, samplePrincipal);

	        assertEquals(2, resultWithAcc.size());
	        assertEquals(TransactionType.DEBT, resultWithAcc.get(0).getTransactionType());
	        assertEquals(TransactionType.CREDIT, resultWithAcc.get(1).getTransactionType());

	        // Case 2: Without account number 
	        when(transactionRepository.findByDateRange(startDate, endDate))
	            .thenReturn(Arrays.asList(sampleTransaction1, sampleTransaction2));

	        List<Transaction> resultWithoutAcc = transactionService.getTransactionsByDateRange(Optional.empty(), startDate, endDate, samplePrincipal);

	        assertEquals(Arrays.asList(sampleTransaction1, sampleTransaction2), resultWithoutAcc);
	    }

	    
	    
	    @Test
	    public void testGetCreditTransactions() throws Exception {
	        // Arrange
	        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

	        String accNum = "1234567890";
	        LocalDate startDate = LocalDate.of(2024, 12, 1);
	        LocalDate endDate = LocalDate.of(2025, 1, 10);

	        when(transactionRepository.findCredits(accNum, startDate, endDate))
	            .thenReturn(Arrays.asList(sampleTransaction2)); 

	        
	        List<Transaction> result = transactionService.getCreditTransactions(accNum, startDate, endDate, samplePrincipal);

	        
	        assertEquals(Arrays.asList(sampleTransaction2), result);
	        assertEquals(TransactionType.CREDIT, result.get(0).getTransactionType());
	    }

	    
	    
	    @Test
	    public void testGetDebitTransactions() throws Exception {
	        
	        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

	        String accNum = "1234567890";
	        LocalDate startDate = LocalDate.of(2024, 12, 1);
	        LocalDate endDate = LocalDate.of(2025, 1, 10);

	        when(transactionRepository.findDebits(accNum, startDate, endDate))
	            .thenReturn(Arrays.asList(sampleTransaction1)); 

	        
	        List<Transaction> result = transactionService.getDebitTransactions(accNum, startDate, endDate, samplePrincipal);

	        
	        assertEquals(Arrays.asList(sampleTransaction1), result);
	        assertEquals(TransactionType.DEBT, result.get(0).getTransactionType());
	    }


	    
	    @Test
	    public void testGetNumberOfTransactions() throws Exception {
	        
	        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);

	        String accountNumber = "1234567890";
	        int count = 1;

	        
	        List<Transaction> allTransactions = Arrays.asList(sampleTransaction1, sampleTransaction2);

	        when(transactionRepository.findAllByAccountNumber(accountNumber)).thenReturn(allTransactions);

	        
	        List<Transaction> result = transactionService.getNumberOfTransactions(accountNumber, count, samplePrincipal);

	        
	        assertEquals(count, result.size());
	        assertEquals(Arrays.asList(sampleTransaction1), result);
	        assertEquals(TransactionType.DEBT, result.get(0).getTransactionType());
	    }

	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    @AfterEach
	    public void afterTest() {
	        sampleTransaction1 = null;
	        sampleTransaction2 = null;
	        sampleUser=null;
	    }

}
