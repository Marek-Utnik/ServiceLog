package com.bazakonserwacji.zeszyt.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.bazakonserwacji.zeszyt.enums.AuthorityName;
import com.bazakonserwacji.zeszyt.form.AuthorityForm;
import com.bazakonserwacji.zeszyt.models.Authority;
import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.models.Machine;
import com.bazakonserwacji.zeszyt.models.SystemUser;
import com.bazakonserwacji.zeszyt.repositories.AuthorityRepository;
import com.bazakonserwacji.zeszyt.repositories.CompanyRepository;
import com.bazakonserwacji.zeszyt.repositories.MachineRepository;
import com.bazakonserwacji.zeszyt.repositories.SystemUserRepository;
import com.bazakonserwacji.zeszyt.services.CompanyService;
import com.bazakonserwacji.zeszyt.services.MachineService;
import com.bazakonserwacji.zeszyt.services.SystemUserDetailsService;
import com.bazakonserwacji.zeszyt.services.SystemUserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController{


	  private static final String REDIRECT_TO_COMPANY_LIST = "redirect:/admin/company/list/1";
	  private static final String REDIRECT_TO_USER_LIST = "redirect:/admin/user/list/1";

      private final CompanyRepository companyRepository;
      private final CompanyService companyService;
      private final SystemUserRepository systemUserRepository;
      private final SystemUserService systemUserService;
      private final SystemUserDetailsService systemUserDetailsService;
      private final AuthorityRepository authorityRepository;


      public AdminController(
			  CompanyRepository companyRepository,
			  CompanyService companyService,
			  SystemUserRepository systemUserRepository,
			  SystemUserService systemUserService,
			  SystemUserDetailsService systemUserDetailsService,
			  AuthorityRepository authorityRepository
			  ) {
    		this.companyRepository = companyRepository;
    		this.companyService = companyService;
    		this.systemUserRepository = systemUserRepository;
    		this.systemUserService = systemUserService;
    		this.authorityRepository = authorityRepository;
    		this.systemUserDetailsService = systemUserDetailsService;
      }

	
 	  @RequestMapping("/admin-menu")
	  public String adminMenu(Model model) {
 	 		Set<Company> companies = new HashSet<Company>(companyRepository.findAll());
 	 		model.addAttribute("companies", companies);
			return "admin/admin-menu";
	  }
 	  
 	  @RequestMapping("/company/list/{page}")
	  public String getCompanyList(Model model,
              @SortDefault("companyId") Pageable pageable,
              @PathVariable("page") int page
			  ) {
 		Set<Company> companies = new HashSet<Company>(companyRepository.findAll());
 		model.addAttribute("companies", companies);
 		  
 		pageable = PageRequest.of(page-1, 2);
 		Page<Company> companiesPaged = companyRepository.findAll(pageable);
     	int totalPages = companiesPaged.getTotalPages();

     	if(totalPages > 0) {
     		List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
     		model.addAttribute("pageNumbers", pageNumbers);
     	}
         model.addAttribute("companiesPaged", companiesPaged); 		  
 		  
         	return "admin/company-list";
	  }
 	  
 	  @RequestMapping("/company/edit/{companyId}")
	  public String editCompanyForm(Model model,
			  @PathVariable("companyId") Long companyId
			  ) {
	 		Set<Company> companies = new HashSet<Company>(companyRepository.findAll());
	 		model.addAttribute("companies", companies);
	 		Company company = companyService.findCompanyById(companyId);
 		  	System.out.println(company.getCompanyId());
	 	    model.addAttribute("company", company);

	 		
			return "admin/company-edit";
	  }
 	  
 	  @RequestMapping("/company/addForm")
	  public String addCompanyForm(Model model) { 
 		  	Set<Company> companies = new HashSet<Company>(companyRepository.findAll());
	 		model.addAttribute("companies", companies);
 		  	Company company = new Company();
 		  	model.addAttribute(company);
			return "admin/company-add";
	  }
 	  
 	  @RequestMapping("/company/save")
	  public String saveCompany(Model model,
			  					@ModelAttribute Company company) {
 		  	System.out.println(company.getCompanyId());
 		  	companyRepository.save(company);
			return REDIRECT_TO_COMPANY_LIST;
	  }
 	  
 	  @RequestMapping("/company/delete/{companyId}")
	  public String deleteCompany(Model model,
              			@PathVariable("companyId") Long companyId
			  			) {
 		  	model.addAttribute("comment", "Company deleted");
 		  	Company company = companyService.findCompanyById(companyId);
 		  	companyRepository.delete(company);
			return REDIRECT_TO_COMPANY_LIST;
	  }
 	  
 	  @RequestMapping("/user/list/{page}")
	  public String getUserList(Model model,
              @SortDefault("systemUserId") Pageable pageable,
              @PathVariable("page") int page
			  ) {
 		Set<Company> companies = new HashSet<Company>(companyRepository.findAll());
 		model.addAttribute("companies", companies);
 		  
 		pageable = PageRequest.of(page-1, 2);
 		Page<SystemUser> systemUsersPaged = systemUserRepository.findAll(pageable);
     	int totalPages = systemUsersPaged.getTotalPages();

     	if(totalPages > 0) {
     		List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
     		model.addAttribute("pageNumbers", pageNumbers);
     	}
         model.addAttribute("systemUsersPaged", systemUsersPaged); 		  
 		  
         	return "admin/user-list";
	  }
 	  
 	  @RequestMapping("/user/edit/{systemUserId}")
	  public String editUserForm(Model model,
			  @PathVariable("systemUserId") Long systemUserId
			  ) {
	 		Set<Company> companies = new HashSet<Company>(companyRepository.findAll());
	 		model.addAttribute("companies", companies);
	 		SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
	 	    model.addAttribute("systemUser", systemUser);
			return "admin/user-edit";
	  }
 	  
 	  @RequestMapping("/user/save")
	  public String saveUser(Model model,
			  					@ModelAttribute SystemUser systemUser) {
 		  	SystemUser systemUserToSave= systemUserService.findSystemUserById(systemUser.getSystemUserId());
 		  	systemUserToSave.setUsername(systemUser.getUsername());
 		  	systemUserToSave.setName(systemUser.getName());
 		  	systemUserToSave.setSurname(systemUser.getSurname());
 	    	systemUserRepository.save(systemUserToSave);
			return REDIRECT_TO_USER_LIST;
	  }
 	  
 	  @RequestMapping("/user/password/{systemUserId}")
	  public String passwordUserForm(Model model,
			  					@PathVariable("systemUserId") Long systemUserId) {
	 		List<Company> companies = new ArrayList<Company>(companyRepository.findAll());
	 		model.addAttribute("companies", companies);
	 		SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
	 	    model.addAttribute("systemUser", systemUser);
			return "admin/user-password";
	  }
 	  
 	  @RequestMapping("/user/passwordSave")
	  public String passwordUserForm(Model model,
			  @ModelAttribute SystemUser systemUser
			  ) {
	    	systemUserService.saveSystemUser(systemUser);
			return REDIRECT_TO_USER_LIST;
	  }
 	  
 	  @RequestMapping("/user/delete/{systemUserId}")
	  public String deleteUser(Model model,
              			@PathVariable("systemUserId") Long systemUserId
			  			) {
 		  	model.addAttribute("comment", "System user deleted");
 		  	SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
 		  	systemUserRepository.delete(systemUser);
			return REDIRECT_TO_USER_LIST;
	  }
 	  
 	  @RequestMapping("/user/roles/{systemUserId}")
	  public String rolesUserForm(Model model,
              			@PathVariable("systemUserId") Long systemUserId
			  			) {
	 		List<Company> companies = new ArrayList<Company>(companyRepository.findAll());
	 		model.addAttribute("companies", companies);
 		  
 		  	SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
 		  	
 		  	AuthorityForm authorityForm = new AuthorityForm();
 		  	
 		  	for (Authority authority:systemUser.getAuthorities())
 		  		{
 		  		if (authority.getName()==AuthorityName.ROLE_COMPANYUSER)
 		  			{authorityForm.setCompanyuser(true);}
 		  		else if (authority.getName()==AuthorityName.ROLE_SERVICEMAN)
 		  			{authorityForm.setServiceman(true);}
 		  		else if (authority.getName()==AuthorityName.ROLE_ADMIN)
 		  			{authorityForm.setAdmin(true);}
 		  		}
 		  	authorityForm.setSystemUserId(systemUserId);
 		  			
 		  	
		  	model.addAttribute("authorityForm", authorityForm);

			return "admin/user-roles";
	  }
 	  
 	  
 	  @RequestMapping("/user/rolesSave")
	  public String rolesUserSet(Model model,
			  @ModelAttribute AuthorityForm authorityForm
			  			) {
		  	SystemUser systemUserToSave= systemUserService.findSystemUserById(authorityForm.getSystemUserId());
		  	HashSet <Authority> authorities = new HashSet<>();

		  		if (authorityForm.getCompanyuser()==true)
		  		{authorities.add(authorityRepository.findByName(AuthorityName.ROLE_COMPANYUSER));}
		  		if (authorityForm.getServiceman()==true)
		  		{authorities.add(authorityRepository.findByName(AuthorityName.ROLE_SERVICEMAN));}
		  		if (authorityForm.getAdmin()==true)
		  		{authorities.add(authorityRepository.findByName(AuthorityName.ROLE_ADMIN));}

		  	systemUserToSave.setAuthorities(authorities);
	    	systemUserRepository.save(systemUserToSave);
		  	
			return REDIRECT_TO_USER_LIST;
	  }
 	  
 	  
 	  @RequestMapping("/user/company/{systemUserId}")
	  public String companyUserList(Model model,
			  @PathVariable("systemUserId") Long systemUserId
			  			) {
	 		List<Company> companies = new ArrayList<Company>(companyRepository.findAll());
	 		model.addAttribute("companies", companies);
		  	SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
	 		Set <Company> userCompanies = systemUser.getCompanies();
	 		List<String> userCompaniesList = new ArrayList<>();
	 		for (Company userCompany : userCompanies) {
	 			userCompaniesList.add(userCompany.getCompanyName());
	 		}
	 		model.addAttribute("userCompaniesList", userCompaniesList);
	 		model.addAttribute("systemUserId", systemUserId);


		  	
			return "admin/user-companies" ;
	  }
 	  
 	  @RequestMapping("/user/company/add/{systemUserId}/{companyId}")
	  public String companyUserSet(Model model,
			  @PathVariable("systemUserId") Long systemUserId,
			  @PathVariable("companyId") Long companyId

			  			) {
	 		List<Company> companies = new ArrayList<Company>(companyRepository.findAll());
	 		model.addAttribute("companies", companies);
	 		
		  	SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
	 		Set <Company> userCompanies = systemUser.getCompanies();
	 		userCompanies.add(companyService.findCompanyById(companyId));
	 		systemUser.setCompanies(userCompanies);
	 		systemUserRepository.save(systemUser);
		  	
			return "redirect:/admin/user/company/"+systemUserId;
	  } 	
 	  
 	  @RequestMapping("/user/company/delete/{systemUserId}/{companyId}")
	  public String companyUserDelete(Model model,
			  @PathVariable("systemUserId") Long systemUserId,
			  @PathVariable("companyId") Long companyId

			  			) {
	 		List<Company> companies = new ArrayList<Company>(companyRepository.findAll());
	 		model.addAttribute("companies", companies);
	 		
		  	SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
	 		Set <Company> userCompanies = systemUser.getCompanies();
	 		userCompanies.remove(companyService.findCompanyById(companyId));
	 		systemUser.setCompanies(userCompanies);
	 		systemUserRepository.save(systemUser);
		  	
			return "redirect:/admin/user/company/"+systemUserId;
	  } 	
 	  
}