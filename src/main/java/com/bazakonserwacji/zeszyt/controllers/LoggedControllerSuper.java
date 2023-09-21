package com.bazakonserwacji.zeszyt.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.services.CompanyService;

@Controller
public class LoggedControllerSuper {
	
    private final CompanyService companyService;

	
	public LoggedControllerSuper(
			CompanyService companyService
			){
		this.companyService = companyService;
	}


	@ModelAttribute(name = "companies")
	public List <Company> addCompaniesToView(Authentication authentication){
    	return companyService.findCompanyByAuthentication(authentication);

	}
}