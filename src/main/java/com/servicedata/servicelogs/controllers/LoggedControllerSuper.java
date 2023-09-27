package com.servicedata.servicelogs.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.services.CompanyService;

public class LoggedControllerSuper {
	
    private final CompanyService companyService;

	public LoggedControllerSuper(
			CompanyService companyService) {
		this.companyService = companyService;
	}

	@ModelAttribute(name = "companies")
	public List <Company> addCompaniesToView(Authentication authentication) {
    	return companyService.findCompanyByAuthentication(authentication);
	}
}