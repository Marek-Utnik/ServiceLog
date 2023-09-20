    
    
package com.bazakonserwacji.zeszyt.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bazakonserwacji.zeszyt.exceptions.CompanyNotFoundException;
import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.repositories.CompanyRepository;

@Service
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }    
    
    public Company findCompanyById(long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("No company found with the following ID: %d".formatted(companyId)));
    }
    
}