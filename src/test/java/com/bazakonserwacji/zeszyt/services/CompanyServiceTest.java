package com.bazakonserwacji.zeszyt.services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bazakonserwacji.zeszyt.exceptions.CompanyNotFoundException;
import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.repositories.CompanyRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private CompanyService companyService;

    @Test
    void findCompanyByIdReturnsCorrectCompany() {
        // given
        long companyId = 2;
        Company companyFromDatabase = new Company("Microsoft", "Adres");
        companyFromDatabase.setCompanyId(companyId);
        // when
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(companyFromDatabase));
        Company company = companyService.findCompanyById(companyId);
        // then
        assertAll("company",
                () -> assertEquals(companyFromDatabase.getCompanyId(), company.getCompanyId()),
                () -> assertEquals(companyFromDatabase.getCompanyName(), company.getCompanyName()),
                () -> assertEquals(companyFromDatabase.getCompanyAddress(), company.getCompanyAddress())
        );
    }
    
    @Test
    void findCompanyByIdThrowsProperExceptionWhenIdIsIncorrect() {
        // given
        long companyId = 0;
        // when
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> companyService.findCompanyById(companyId))
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessage("No company found with the following ID: %d".formatted(companyId));
    }


    
    
    
    
}