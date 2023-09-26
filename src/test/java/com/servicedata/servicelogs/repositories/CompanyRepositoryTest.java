package com.servicedata.servicelogs.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;


import com.servicedata.servicelogs.models.Company;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.*;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class CompanyRepositoryTest {

    private final CompanyRepository companyRepository;

    CompanyRepositoryTest(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Test
    void findByNameReturnsCorrectCompanyObject() {
    	
        // given
    	String companyName = "Pekabex";
    	
    	// when
    	Optional <Company> company = companyRepository.findByCompanyName(companyName);

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