package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.Loader;
import com.servicedata.servicelogs.models.SystemUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class SystemUserRepositoryTest {

    @MockBean
    private final Loader loader;
    private final SystemUserRepository systemUserRepository;

    SystemUserRepositoryTest(SystemUserRepository systemUserRepository, Loader loader) {
        this.systemUserRepository = systemUserRepository;
        this.loader = loader;
    }

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