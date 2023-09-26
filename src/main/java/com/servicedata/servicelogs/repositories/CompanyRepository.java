package com.servicedata.servicelogs.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicedata.servicelogs.models.Company;

@Repository
public interface CompanyRepository  extends JpaRepository<Company, Long>{
	Optional <Company> findByCompanyName(String companyName);

}