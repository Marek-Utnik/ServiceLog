package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.models.Company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    Optional<Company> findByCompanyName(String companyName);
    Page<Company> findAll(Specification<Company> spec, Pageable pageable);


}