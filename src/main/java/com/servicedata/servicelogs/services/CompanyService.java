    
    
package com.servicedata.servicelogs.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicedata.servicelogs.exceptions.CompanyNotFoundException;
import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.CompanyRepository;
import com.servicedata.servicelogs.repositories.SystemUserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final SystemUserRepository systemUserRepository;

    //Obiekt zarządzający Enitities
    private final EntityManager entityManager;
    
    public CompanyService(CompanyRepository companyRepository, SystemUserRepository systemUserRepository, EntityManager entityManager) {
        this.companyRepository = companyRepository;
        this.systemUserRepository = systemUserRepository;
        this.entityManager = entityManager;
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
    
    public Page<Company> filteredCompany(Pageable pageable, 
    		Long companyId, 
    		String companyName,
    		String companyAddress
    		){
        boolean companyIdCheck = companyId != null;
        boolean companyNameCheck = companyName != null && !companyName.isBlank();
        boolean companyAddressCheck = companyAddress != null && !companyAddress.isBlank();
        
        System.out.println(companyId+" l "+companyName+" l "+companyAddress);
        System.out.println(companyIdCheck+" l "+companyNameCheck+" l "+companyAddressCheck);

        //Obiekt budujący zapytanie do bazy
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        //Obiekt zapytania do bazy - jakie będą zwracane obiekty
        CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
        //Gdzie będziemy szukać
        Root<Company> companyRoot = criteriaQuery.from(Company.class);
        //Określamy że to będzie select
        CriteriaQuery<Company> select = criteriaQuery.select(companyRoot);
        //Pusta lista warunków
        List<Predicate> predicates = new ArrayList<>();
        
        if (companyIdCheck) {
            predicates.add(criteriaBuilder.equal(companyRoot.get("companyId"), companyId));
        }
        if (companyNameCheck) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(companyRoot.get("companyName")), "%" + companyName.toUpperCase() + "%"));
        }
        if (companyAddressCheck) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(companyRoot.get("companyAddress")), "%" + companyAddress.toUpperCase() + "%"));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), companyRoot, criteriaBuilder));

        List <Company> filteredCompanies = entityManager.createQuery(criteriaQuery).getResultList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredCompanies.size());
        List<Company> pageContent = filteredCompanies.subList(start, end);        
        Page <Company> page = new PageImpl(pageContent, pageable, filteredCompanies.size());
        return page;
    }
}