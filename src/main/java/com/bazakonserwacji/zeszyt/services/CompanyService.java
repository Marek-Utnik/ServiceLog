    
    
package com.bazakonserwacji.zeszyt.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bazakonserwacji.zeszyt.exceptions.CompanyNotFoundException;
import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.models.SystemUser;
import com.bazakonserwacji.zeszyt.repositories.CompanyRepository;
import com.bazakonserwacji.zeszyt.repositories.SystemUserRepository;

@Service
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final SystemUserRepository systemUserRepository;
    public CompanyService(CompanyRepository companyRepository, SystemUserRepository systemUserRepository) {
        this.companyRepository = companyRepository;
        this.systemUserRepository = systemUserRepository;
    }    
    
    public Company findCompanyById(long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("No company found with the following ID: %d".formatted(companyId)));
    }
    
    public List<Company> findCompanyByAuthentication(Authentication authentication) {
  		String systemUserName = authentication.getName();
		SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
		String role = "ROLE_ADMIN";
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
		List<Company> companies = (isAdmin) ? new ArrayList<Company>(companyRepository.findAll()) : new ArrayList<Company>(systemUser.getCompanies());    
        companies.sort((Company a, Company b)->a.getCompanyName().compareTo(b.getCompanyName()));
        return companies;
}
}