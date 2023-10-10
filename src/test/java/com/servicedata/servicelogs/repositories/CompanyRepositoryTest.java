package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.Loader;
import com.servicedata.servicelogs.controllers.CompanyController;
import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.services.EmailService;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
class CompanyRepositoryTest {

    @MockBean
    private final Loader loader;
    
    @MockBean
    private final EmailService emailService;
    
    @MockBean
    private final CompanyController companyController;
    
    private final CompanyRepository companyRepository;

    @Test
    void findByNameReturnsCorrectCompanyObject() {

        // given
        String companyName = "Pekabex";

        // when
        Optional<Company> company = companyRepository.findByCompanyName(companyName);

        // then
        assertTrue(company.isPresent());
        assertEquals(companyName, company.get().getCompanyName());


    }

    @Test
    void findByCompanyNameReturnsEmptyOptionalWhenCompanyNameIsNotInDatabase() {
        // given
        String wrongCompanyName = "Osram";
        // when
        Optional<Company> company = companyRepository.findByCompanyName(wrongCompanyName);
        // then
        assertFalse(company.isPresent());
    }

}