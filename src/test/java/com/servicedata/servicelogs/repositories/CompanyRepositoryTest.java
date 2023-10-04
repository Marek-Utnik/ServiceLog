package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.Loader;
import com.servicedata.servicelogs.models.Company;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class CompanyRepositoryTest {

    @MockBean
    private final Loader loader;

    private final CompanyRepository companyRepository;

    CompanyRepositoryTest(CompanyRepository companyRepository, Loader loader) {
        this.companyRepository = companyRepository;
        this.loader = loader;
    }

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