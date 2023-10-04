package com.servicedata.servicelogs.services;

import com.servicedata.servicelogs.exceptions.CompanyNotFoundException;
import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.CompanyRepository;
import com.servicedata.servicelogs.repositories.SystemUserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final SystemUserRepository systemUserRepository;

    public Company findCompanyById(long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("No company found with the following ID: %d".formatted(companyId)));
    }

    public List<Company> findCompanyByAuthentication(Authentication authentication) {
        String systemUserName = authentication.getName();
        SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
        List<Company> companies = new ArrayList<Company>(systemUser.getCompanies());    // (isAdmin) ? new ArrayList<Company>(companyRepository.findAll()) :
        companies.sort((Company a, Company b) -> a.getCompanyName().compareTo(b.getCompanyName()));
        return companies;
    }
    
    public Page<Company> filteredCompany(Pageable pageable,
                                            Long companyId,
                                            String companyName,
                                            String companyAddress
    ) {
        boolean companyIdCheck = companyId != null;
        boolean companyNameCheck = companyName != null && !companyName.isBlank();
        boolean companyAddressCheck = companyAddress != null && !companyAddress.isBlank();

        Specification<Company> specification = Specification.where(null);

        if (companyIdCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("companyId"), companyId));
        }
        if (companyNameCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("companyName")), "%" + companyName.toUpperCase() + "%"));
        }
        if (companyAddressCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("companyAddress")), "%" + companyAddress.toUpperCase() + "%"));
        }
        
        Page<Company> page = companyRepository.findAll(specification, pageable);
        return page;
    }

}