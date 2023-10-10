package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.Loader;
import com.servicedata.servicelogs.controllers.CompanyController;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.services.EmailService;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
class SystemUserRepositoryTest {

    @MockBean
    Loader loader;

    @MockBean
    EmailService emailService;

    @MockBean
    CompanyController companyController;
    
    private final SystemUserRepository systemUserRepository;


    @Test
    void findByUsernameReturnsCorrectSystemUserObject() {
        // given
        String username = "admin9";

        // when
        SystemUser systemUser = systemUserRepository.findByUsername(username);

        // then
        assertNotNull(systemUser);
        assertEquals(username, systemUser.getUsername());
    }

    @Test
    void findByUsernameReturnsNull() {
        // given
        String wrongUsername = "Osram";
        // when
        SystemUser systemUser = systemUserRepository.findByUsername(wrongUsername);
        // then
        assertNull(systemUser);
    }

}