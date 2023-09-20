package com.bazakonserwacji.zeszyt.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import com.bazakonserwacji.zeszyt.enums.AuthorityName;
import com.bazakonserwacji.zeszyt.models.Authority;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.*;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class AuthorityRepositoryTest {

    private final AuthorityRepository authorityRepository;

    AuthorityRepositoryTest(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Test
    void findByNameReturnsCorrectAuthorityObject() {
        // given
        List <AuthorityName> authorityNames = Arrays.asList(AuthorityName.ROLE_SERVICEMAN,
        													AuthorityName.ROLE_COMPANYUSER,
        													AuthorityName.ROLE_ADMIN);
        Authority authority = null;
        for (AuthorityName authorityName : authorityNames)
        {
        	// when
        	authority = authorityRepository.findByName(authorityName);
        	// then
            assertFalse(authority==null);
            assertEquals(authorityName, authority.getName());
        }
    }

}