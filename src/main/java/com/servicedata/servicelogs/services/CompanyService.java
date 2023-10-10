package com.servicedata.servicelogs.services;

import com.servicedata.servicelogs.excelgenerators.CompanyExcelGenerator;
import com.servicedata.servicelogs.exceptions.CompanyNotFoundException;
import com.servicedata.servicelogs.forms.CompanyFilterData;
import com.servicedata.servicelogs.forms.ConservationLogFilterData;
import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.CompanyRepository;
import com.servicedata.servicelogs.repositories.MachineRepository;
import com.servicedata.servicelogs.repositories.SystemUserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final MachineRepository machineRepository;
    private final SystemUserRepository systemUserRepository;
	private final ConservationLogService conservationLogService;
    

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
    									 CompanyFilterData filterData
    ) {
        boolean companyIdCheck = filterData.getCompanyId() != null;
        boolean companyNameCheck = filterData.getCompanyName() != null && !filterData.getCompanyName().isBlank();
        boolean companyAddressCheck = filterData.getCompanyAddress() != null && !filterData.getCompanyAddress().isBlank();

        Specification<Company> specification = Specification.where(null);

        if (companyIdCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("companyId"), filterData.getCompanyId()));
        }
        if (companyNameCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("companyName")), "%" + filterData.getCompanyName().toUpperCase() + "%"));
        }
        if (companyAddressCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("companyAddress")), "%" + filterData.getCompanyAddress().toUpperCase() + "%"));
        }
        
        Page<Company> page = companyRepository.findAll(specification, pageable);
        return page;
    }
    
    public void generateExcel(HttpServletResponse response,     
    						  ConservationLogFilterData filterData,
    						  Company company) {    	
    	SortedMap<Machine, List<ConservationLog>> logs = new TreeMap<>();
    	List<Machine> machinesL = machineRepository.findAllByCompany(company);
    	for (Machine machine : machinesL) {
    		List <ConservationLog> log = conservationLogService.filteredConservationLogExcel(machine, filterData);
    		logs.put(machine, log);
    	}
    	CompanyExcelGenerator file = new CompanyExcelGenerator(company, logs);

    	try {
    		file.generateExcelFile(response);
    	} catch(IOException e){
    		log.error("Error: ", e);
    	}

}

}