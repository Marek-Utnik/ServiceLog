package com.bazakonserwacji.zeszyt.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bazakonserwacji.zeszyt.models.Company;

@Repository
public interface CompanyRepository  extends JpaRepository<Company, Long>{
	Optional <Company> findByCompanyName(String companyName);

}