package com.servicedata.servicelogs.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;


import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.SystemUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.*;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class SystemUserRepositoryTest {

    private final SystemUserRepository systemUserRepository;

    SystemUserRepositoryTest(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    @Test
    void findByUsernameReturnsCorrectSystemUserObject() {
        // given
    	String username = "admin9";
    	
    	// when
    	SystemUser systemUser = systemUserRepository.findByUsername(username);

       	// then
        assertTrue(systemUser!=null);
        assertEquals(username, systemUser.getUsername());   
    }
    
    @Test
    void findByUsernameReturnsNull() {
        // given
        String wrongUsername = "Osram";
        // when
    	SystemUser systemUser = systemUserRepository.findByUsername(wrongUsername);
        // then
        assertFalse(systemUser!=null);
    }

}