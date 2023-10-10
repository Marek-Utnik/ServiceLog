package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.Loader;
import com.servicedata.servicelogs.controllers.CompanyController;
import com.servicedata.servicelogs.enums.AuthorityName;
import com.servicedata.servicelogs.models.Authority;
import com.servicedata.servicelogs.services.EmailService;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
class AuthorityRepositoryTest {

    @MockBean
    Loader loader;
    
    @MockBean
    EmailService emailService;
    
    @MockBean
    CompanyController companyController;

    private final AuthorityRepository authorityRepository;



    @Test
    void findByNameReturnsCorrectAuthorityObject() {
        // given
        List<AuthorityName> authorityNames = Arrays.asList(AuthorityName.ROLE_SERVICEMAN,
                AuthorityName.ROLE_COMPANYUSER,
                AuthorityName.ROLE_ADMIN);
        Authority authority = null;
        for (AuthorityName authorityName : authorityNames) {
            // when
            authority = authorityRepository.findByName(authorityName);
            // then
            assertNotNull(authority);
            assertEquals(authorityName, authority.getName());
        }
    }

}