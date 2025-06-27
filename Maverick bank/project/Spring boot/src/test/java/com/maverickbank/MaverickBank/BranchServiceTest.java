package com.maverickbank.MaverickBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.maverickbank.MaverickBank.enums.Role;
import com.maverickbank.MaverickBank.exception.InvalidInputException;
import com.maverickbank.MaverickBank.exception.ResourceExistsException;
import com.maverickbank.MaverickBank.exception.ResourceNotFoundException;
import com.maverickbank.MaverickBank.model.Branch;
import com.maverickbank.MaverickBank.model.User;
import com.maverickbank.MaverickBank.repository.BranchRepository;
import com.maverickbank.MaverickBank.repository.UserRepository;
import com.maverickbank.MaverickBank.service.BranchService;

@SpringBootTest
public class BranchServiceTest {

    @InjectMocks
    private BranchService branchService;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private UserRepository userRepository;

    private Branch sampleBranch1;
    private Branch sampleBranch2;
    private User sampleUser;
    private Principal samplePrincipal;

    @BeforeEach
    public void init() throws InvalidInputException {
        // Sample User (Principal)
        sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setUsername("new_user");
        sampleUser.setPassword("userPassword@123");
        sampleUser.setRole(Role.TRANSACTION_ANALYST);
        sampleUser.setStatus(ActiveStatus.ACTIVE);

        // Sample Branch 1
        sampleBranch1 = new Branch();
        sampleBranch1.setId(101);
        sampleBranch1.setIfsc("MVRK0000001");
        sampleBranch1.setBranchName("Hyderabad Central");
        sampleBranch1.setAddress("123 Central Ave, Hyderabad");
        sampleBranch1.setContactNumber("9999999999");
        sampleBranch1.setEmail("hydcentral@maverickbank.com");
        sampleBranch1.setStatus(ActiveStatus.ACTIVE);

        // Sample Branch 2
        sampleBranch2 = new Branch();
        sampleBranch2.setId(102);
        sampleBranch2.setIfsc("MVRK0000002");
        sampleBranch2.setBranchName("Mumbai West");
        sampleBranch2.setAddress("456 West Blvd, Mumbai");
        sampleBranch2.setContactNumber("8888888888");
        sampleBranch2.setEmail("mumbaiwest@maverickbank.com");
        sampleBranch2.setStatus(ActiveStatus.INACTIVE);

        samplePrincipal= mock(Principal.class);
        when(samplePrincipal.getName()).thenReturn("new_user");
    }
    
    
 //================================ POST ================================================
    @Test
    public void testAddBranch() throws Exception {
        // Case 1: Success ― brand-new, valid branch
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.getBranchByIfsc(sampleBranch1.getIfsc())).thenReturn(null);
        when(branchRepository.getByContactNumber(sampleBranch1.getContactNumber())).thenReturn(null);
        when(branchRepository.getByEmail(sampleBranch1.getEmail())).thenReturn(null);
        when(branchRepository.getByName(sampleBranch1.getBranchName())).thenReturn(null);
        when(branchRepository.save(sampleBranch1)).thenReturn(sampleBranch1);

        Branch addedBranch = branchService.addBranch(sampleBranch1, samplePrincipal);

        assertEquals(101, addedBranch.getId());
        assertEquals("Hyderabad Central", addedBranch.getBranchName());
        assertEquals("9999999999", addedBranch.getContactNumber());
        assertEquals(ActiveStatus.ACTIVE, addedBranch.getStatus());

        // Case 2: Duplicate IFSC
        when(branchRepository.getBranchByIfsc(sampleBranch1.getIfsc())).thenReturn(sampleBranch1);

        ResourceExistsException eIfsc = assertThrows(ResourceExistsException.class, () -> {
            branchService.addBranch(sampleBranch1, samplePrincipal);
        });
        assertEquals("Branch with the given details already exists...!!!", eIfsc.getMessage());

        // Case 3: Duplicate Contact Number
        when(branchRepository.getBranchByIfsc(sampleBranch1.getIfsc())).thenReturn(null);
        when(branchRepository.getByContactNumber(sampleBranch1.getContactNumber())).thenReturn(sampleBranch1);

        ResourceExistsException eContact = assertThrows(ResourceExistsException.class, () -> {
            branchService.addBranch(sampleBranch1, samplePrincipal);
        });
        assertEquals("Branch with the given details already exists...!!!", eContact.getMessage());

        // Case 4: Duplicate Email
        when(branchRepository.getByContactNumber(sampleBranch1.getContactNumber())).thenReturn(null);
        when(branchRepository.getByEmail(sampleBranch1.getEmail())).thenReturn(sampleBranch1);

        ResourceExistsException eEmail = assertThrows(ResourceExistsException.class, () -> {
            branchService.addBranch(sampleBranch1, samplePrincipal);
        });
        assertEquals("Branch with the given details already exists...!!!", eEmail.getMessage());

        // Case 5: Duplicate Branch Name
        when(branchRepository.getByEmail(sampleBranch1.getEmail())).thenReturn(null);
        when(branchRepository.getByName(sampleBranch1.getBranchName())).thenReturn(sampleBranch1);

        ResourceExistsException eName = assertThrows(ResourceExistsException.class, () -> {
            branchService.addBranch(sampleBranch1, samplePrincipal);
        });
        assertEquals("Branch with the given details already exists...!!!", eName.getMessage());

        // Case 6: Invalid Branch Details
        Branch invalidBranch = new Branch();  

        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            branchService.addBranch(invalidBranch, samplePrincipal);
        });
        assertEquals("Branch Name is Invalid. Please enter appropriate Branch Name...!!!", eInvalid.getMessage());
    }

    
//=========================================== GET ========================================
    @Test
    public void testGetByName() throws Exception {
        // Case 1: Success ― branch with given name exists
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.getByName(sampleBranch1.getBranchName())).thenReturn(sampleBranch1);

        Branch found = branchService.getByName(sampleBranch1.getBranchName(), samplePrincipal);

        assertEquals(101, found.getId());
        assertEquals("Hyderabad Central", found.getBranchName());
        assertEquals("MVRK0000001", found.getIfsc());
        assertEquals(ActiveStatus.ACTIVE, found.getStatus());

        // Case 2: Branch not found ― expect ResourceNotFoundException
        when(branchRepository.getByName("NonExistent")).thenReturn(null);

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.getByName("NonExistent", samplePrincipal);
        });
        assertEquals("No branch record with the given name...!!!", eNotFound.getMessage());
    }


    
    @Test
    public void testGetAll() throws Exception {
        // Case 1 – Success: list returned
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.findAll()).thenReturn(Arrays.asList(sampleBranch1, sampleBranch2));

        List<Branch> allBranches = branchService.getAll(samplePrincipal);

        assertEquals(2, allBranches.size());
        assertEquals("Hyderabad Central", allBranches.get(0).getBranchName());
        assertEquals("Mumbai West", allBranches.get(1).getBranchName());

        // Case 2 
        when(branchRepository.findAll()).thenReturn(null);

        ResourceNotFoundException eNull = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.getAll(samplePrincipal);
        });
        assertEquals("No branch records...!!!", eNull.getMessage());
    }
    
    
    
    @Test
    public void testGetByState() throws Exception {
        // Case 1 
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.getByState("Telangana")).thenReturn(Arrays.asList(sampleBranch1));

        List<Branch> telanganaBranches = branchService.getByState("Telangana", samplePrincipal);

        assertEquals(1, telanganaBranches.size());
        assertEquals("Hyderabad Central", telanganaBranches.get(0).getBranchName());

        // Case 2 
        when(branchRepository.getByState("Kerala")).thenReturn(null);

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.getByState("Kerala", samplePrincipal);
        });
        assertEquals("No branches in the given state...!!!", eNotFound.getMessage());
    }
    
    @Test
    public void testGetById() throws Exception {
        // Case 1 – Success: branch exists for given ID
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.findById(101)).thenReturn(Optional.of(sampleBranch1));

        Branch found = branchService.getById(101, samplePrincipal);

        assertEquals(101, found.getId());
        assertEquals("Hyderabad Central", found.getBranchName());
        assertEquals("MVRK0000001", found.getIfsc());

        // Case 2 – Branch not found → expect ResourceNotFoundException
        when(branchRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.getById(999, samplePrincipal);
        });
        assertEquals("No branch record with given Id...!!!", eNotFound.getMessage());
    }
    @Test
    public void testGetInactiveBranches() throws Exception {
        // Case 1 – Success: inactive branches exist
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.getByStatus(ActiveStatus.INACTIVE))
                .thenReturn(Arrays.asList(sampleBranch2));

        List<Branch> inactive = branchService.getInactiveBranches(samplePrincipal);

        assertEquals(1, inactive.size());
        assertEquals(102, inactive.get(0).getId());
        assertEquals(ActiveStatus.INACTIVE, inactive.get(0).getStatus());

        // Case 2 – Repository returns null → ResourceNotFoundException expected
        when(branchRepository.getByStatus(ActiveStatus.INACTIVE)).thenReturn(null);

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.getInactiveBranches(samplePrincipal);
        });
        assertEquals("No INACTIVE branches found...!!!", eNotFound.getMessage());
    }

    
    
    @Test
    public void testGetActiveBranches() throws Exception {
        // Case 1 
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.getByStatus(ActiveStatus.ACTIVE))
                .thenReturn(Arrays.asList(sampleBranch1));

        List<Branch> active = branchService.getActiveBranches(samplePrincipal);

        assertEquals(1, active.size());
        assertEquals(101, active.get(0).getId());
        assertEquals(ActiveStatus.ACTIVE, active.get(0).getStatus());

        // Case 2 
        when(branchRepository.getByStatus(ActiveStatus.ACTIVE)).thenReturn(null);

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.getActiveBranches(samplePrincipal);
        });
        assertEquals("No ACTIVE branches found...!!!", eNotFound.getMessage());
    }

    
    
    @Test
    public void testGetActiveBranchesByState() throws Exception {
        // Case 1
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.getByStateAndStatus("Telangana", ActiveStatus.ACTIVE))
                .thenReturn(Arrays.asList(sampleBranch1));

        List<Branch> telanganaActive = branchService.getActiveBranchesByState("Telangana", samplePrincipal);

        assertEquals(1, telanganaActive.size());
        assertEquals(101, telanganaActive.get(0).getId());
        assertEquals("Hyderabad Central", telanganaActive.get(0).getBranchName());
        assertEquals(ActiveStatus.ACTIVE, telanganaActive.get(0).getStatus());

        // Case 2 
        when(branchRepository.getByStateAndStatus("Kerala", ActiveStatus.ACTIVE)).thenReturn(null);

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.getActiveBranchesByState("Kerala", samplePrincipal);
        });
        assertEquals("No ACTIVE branches found in the given state...!!!", eNotFound.getMessage());
    }


    @Test
    public void testGetgetInactiveBranchesByState() throws Exception {
        // Case 1 
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.getByStateAndStatus("Maharashtra", ActiveStatus.INACTIVE))
                .thenReturn(Arrays.asList(sampleBranch2));

        List<Branch> maharashtraInactive = branchService.getgetInactiveBranchesByState("Maharashtra", samplePrincipal);

        assertEquals(1, maharashtraInactive.size());
        assertEquals(102, maharashtraInactive.get(0).getId());
        assertEquals("Mumbai West", maharashtraInactive.get(0).getBranchName());
        assertEquals(ActiveStatus.INACTIVE, maharashtraInactive.get(0).getStatus());

        // Case 2 
        when(branchRepository.getByStateAndStatus("Kerala", ActiveStatus.INACTIVE)).thenReturn(null);

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.getgetInactiveBranchesByState("Kerala", samplePrincipal);
        });
        assertEquals("No INACTIVE branches found in the given state...!!!", eNotFound.getMessage());
    }
    
    
    @Test
    public void testDeactivateBranch() throws Exception {
        // Case 1 
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.findById(101)).thenReturn(Optional.of(sampleBranch1));
        when(branchRepository.save(sampleBranch1)).thenReturn(sampleBranch1);

        Branch deactivated = branchService.deactivateBranch(101, samplePrincipal);

        assertEquals(101, deactivated.getId());
        assertEquals(ActiveStatus.INACTIVE, deactivated.getStatus());
        assertEquals("Hyderabad Central", deactivated.getBranchName());

        // Case 2 
        when(branchRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.deactivateBranch(999, samplePrincipal);
        });
        assertEquals("No branch record with given Id...!!!", eNotFound.getMessage());
    }

    
    @Test
    public void testActivateBranch() throws Exception {
        // Case 1 
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.findById(102)).thenReturn(Optional.of(sampleBranch2));
        when(branchRepository.save(sampleBranch2)).thenReturn(sampleBranch2);

        Branch activated = branchService.activateBranch(102, samplePrincipal);

        assertEquals(102, activated.getId());
        assertEquals(ActiveStatus.ACTIVE, activated.getStatus());
        assertEquals("Mumbai West", activated.getBranchName());

        // Case 2 
        when(branchRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.activateBranch(999, samplePrincipal);
        });
        assertEquals("No branch record with given Id...!!!", eNotFound.getMessage());
    }
    
    
    @Test
    public void testUpdateBranchContactNumber() throws Exception {
        // Case 1 
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.findById(101)).thenReturn(Optional.of(sampleBranch1));
        when(branchRepository.save(sampleBranch1)).thenReturn(sampleBranch1);

        Branch updated = branchService.updateBranchContactNumber(
                101,
                "7777777777",
                samplePrincipal
        );

        assertEquals(101, updated.getId());
        assertEquals("7777777777", updated.getContactNumber());

        // Case 2 
        when(branchRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.updateBranchContactNumber(999, "7777777777", samplePrincipal);
        });
        assertEquals("No branch record with given Id...!!!", eNotFound.getMessage());

        // Case 3 – Invalid contact number ⇒ InvalidInputException
        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            branchService.updateBranchContactNumber(101, "123", samplePrincipal); // too short / invalid
        });
        assertEquals(
                "Contact number is Invalid. Please enter appropriate 10 digit Contact number...!!!",
                eInvalid.getMessage()
        );
    }



    
    
    @Test
    public void testUpdateEmail() throws Exception {
        // Case 1 
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.findById(101)).thenReturn(Optional.of(sampleBranch1));
        when(branchRepository.save(sampleBranch1)).thenReturn(sampleBranch1);

        Branch updated = branchService.updateEmail(101, "updated@maverickbank.com", samplePrincipal);

        assertEquals(101, updated.getId());
        assertEquals("updated@maverickbank.com", updated.getEmail());

        // Case 2 
        when(branchRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.updateEmail(999, "updated@maverickbank.com", samplePrincipal);
        });
        assertEquals("No branch record with given Id...!!!", eNotFound.getMessage());

        // Case 3 
        InvalidInputException eInvalid = assertThrows(InvalidInputException.class, () -> {
            branchService.updateEmail(101, "invalid-email", samplePrincipal);
        });
        assertEquals("Email is Invalid. Please enter appropriate Email...!!!", eInvalid.getMessage());
    }
    
    
    @Test
    public void testUpdateBranch() throws Exception {
        // Case 1: 
        when(userRepository.getByUsername("new_user")).thenReturn(sampleUser);
        when(branchRepository.findById(101)).thenReturn(Optional.of(sampleBranch1));
        when(branchRepository.save(sampleBranch1)).thenReturn(sampleBranch1);

        Branch updateRequest = new Branch();
        updateRequest.setId(101);
        updateRequest.setIfsc("MVRK0000101");
        updateRequest.setBranchName("Hyderabad South");
        updateRequest.setContactNumber("7777777777");
        updateRequest.setEmail("hydsouth@maverickbank.com");
        updateRequest.setAddress("789 South Ave, Hyderabad");

        Branch updated = branchService.updateBranch(updateRequest, samplePrincipal);

        assertEquals(101, updated.getId());
        assertEquals("Hyderabad South", updated.getBranchName());
        assertEquals("MVRK0000101", updated.getIfsc());
        assertEquals("7777777777", updated.getContactNumber());
        assertEquals("hydsouth@maverickbank.com", updated.getEmail());
        assertEquals("789 South Ave, Hyderabad", updated.getAddress());

        

        // Case 2: Branch not found 
        when(branchRepository.findById(555)).thenReturn(Optional.empty());
        Branch notFoundRequest = new Branch();
        notFoundRequest.setId(555);
        notFoundRequest.setIfsc("MVRK0000555");
        notFoundRequest.setBranchName("Ghost Branch");
        notFoundRequest.setContactNumber("6666666666");
        notFoundRequest.setEmail("ghost@maverickbank.com");
        notFoundRequest.setAddress("Nowhere Street");

        ResourceNotFoundException eNotFound = assertThrows(ResourceNotFoundException.class, () -> {
            branchService.updateBranch(notFoundRequest, samplePrincipal);
        });
        assertEquals("No branch record with given Id...!!!", eNotFound.getMessage());

    }





    
    
    
    
    
    
    
    
    
    
    
    @AfterEach
    public void afterTest() {
        sampleBranch1 = null;
        sampleBranch2 = null;
        sampleUser = null;
        samplePrincipal = null;
    }

    
}

